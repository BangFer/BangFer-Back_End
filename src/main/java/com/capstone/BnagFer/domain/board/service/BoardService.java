package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.CreateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.UpdateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.CommentResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.CreateBoardResponseDto;
import com.capstone.BnagFer.domain.board.entity.*;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardCommentRepository;
import com.capstone.BnagFer.domain.board.repository.BoardImageRepository;
import com.capstone.BnagFer.domain.board.repository.BoardLikeRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.domain.notification.event.CommentCreatedEvent;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.global.util.s3.S3Provider;
import com.capstone.BnagFer.global.util.s3.dto.S3UploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountsCommonService accountsCommonService;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardImageRepository boardImageRepository;
    private final RedisUtil redisUtil;
    private final S3Provider s3Provider;
    private final ApplicationEventPublisher eventPublisher;

    public CreateBoardResponseDto createBoard(BoardRequestDto request, User user, List<MultipartFile> images) {

        accountsCommonService.checkUserActivity(user);
        accountsCommonService.checkUserProfile(user);
        Board board = request.toEntity(user);
        boardRepository.save(board);

        uploadBoard(user, images, board);

        return CreateBoardResponseDto.from(board);
    }

    public CreateBoardResponseDto updateBoard(Long boardId, BoardRequestDto request, User user, List<MultipartFile> images) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }

        // 기존 이미지 삭제 후 새 이미지 추가
        List<BoardImage> currentImages = board.getImages();
        currentImages.clear();
        uploadBoard(user, images, board);

        board.updateBoard(request);
        boardRepository.save(board);
        return CreateBoardResponseDto.from(board);
    }

    private void uploadBoard(User user, List<MultipartFile> images, Board board) {

        if (images != null && !images.isEmpty()) {

            if (board.getImages().size() + images.size() > 10) {
                throw new BoardExceptionHandler(ErrorCode.TOO_MUCH_IMAGE);
            }

            List<BoardImage> boardImages = images.stream()
                    .map(image -> {
                        String imageUrl = s3Provider.uploadFile(image,
                                S3UploadRequest.builder()
                                        .userId(user.getId())
                                        .dirName("freeBoard")
                                        .build());
                        return BoardImage.builder()
                                .imageUrl(imageUrl)
                                .board(board)
                                .build();
                    })
                    .collect(Collectors.toList());

            boardImageRepository.saveAll(boardImages);
        }
    }

    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        boardRepository.deleteById(boardId);
    }

    public ApiResponse<Object> likeButton(Long boardId, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));

        Optional<Like> like = boardLikeRepository.findByUserAndBoard(user, board);

        Long likeCountFromRedis = redisUtil.boardGetLikeCount(boardId);
        long likeCount;

        if (likeCountFromRedis == null) {
            likeCount = boardLikeRepository.countByBoard(board);
            redisUtil.boardSaveLikeCount(boardId, likeCount);
        } else {
            likeCount = likeCountFromRedis;
        }

        if (like.isPresent()) {
            boardLikeRepository.delete(like.get());
            likeCount--;
            redisUtil.boardSaveLikeCount(boardId, likeCount);
            return ApiResponse.CANCELED_LIKE();
        } else {
            boardLikeRepository.save(new Like(user, board));
            likeCount++;
            redisUtil.boardSaveLikeCount(boardId, likeCount);
            return ApiResponse.SUCCESS_LIKE();
        }
    }

    public CommentResponseDto createComment(Long boardId, CreateCommentRequestDto request, User user, Long parentCommentId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        // 기본값 설정을 위한 수정된 부분
        Long commentCountObj = redisUtil.boardGetCommentCount(boardId);
        long commentCount = (commentCountObj != null) ? commentCountObj : 0L;

        Comment parent = null;
        if (parentCommentId != null) {
            parent = boardCommentRepository.findById(parentCommentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = request.toEntity(user, board, parent);
        accountsCommonService.checkUserProfile(user);
        boardCommentRepository.save(comment);

        commentCount++;
        redisUtil.boardSaveCommentCount(boardId, commentCount);

        // FCM 알림 전송
        if (parentCommentId == null) {
            // 새 댓글인 경우
            if (!user.getId().equals(board.getUser().getId())) {
                // 게시글 작성자가 댓글을 단 경우가 아닐 때만 알림 발송
                eventPublisher.publishEvent(new CommentCreatedEvent(user.getId(), board.getUser().getId(), CommentCreatedEvent.NotificationType.NEW_COMMENT));
            }
        } else {
            // 대댓글인 경우
            if (!user.getId().equals(board.getUser().getId())) {
                // 게시글 작성자가 대댓글을 단 경우가 아닐 때 게시글 작성자에게 알림
                eventPublisher.publishEvent(new CommentCreatedEvent(user.getId(), board.getUser().getId(), CommentCreatedEvent.NotificationType.NEW_COMMENT));
            }

            if (!user.getId().equals(parent.getUser().getId()) && !parent.getUser().getId().equals(board.getUser().getId())) {
                // 부모 댓글 작성자가 대댓글을 단 경우가 아니고, 부모 댓글 작성자가 게시글 작성자가 아닐 때 부모 댓글 작성자에게 알림
                eventPublisher.publishEvent(new CommentCreatedEvent(user.getId(), parent.getUser().getId(), CommentCreatedEvent.NotificationType.NEW_REPLY));
            }
        }

        return CommentResponseDto.from(comment);
    }

    public CommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto request, User user) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        if (!comment.getUser().getId().equals(user.getId()))
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        comment.updateComment(request);
        Comment updatedComment = boardCommentRepository.save(comment);
        return CommentResponseDto.from(updatedComment);
    }

    public void deleteComment(Long commentId, User user) {

        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        comment.deleteComment();
    }
}


