package com.capstone.BnagFer.domain.board.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.CreateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.UpdateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.*;
import com.capstone.BnagFer.domain.board.service.BoardBlockQueryService;
import com.capstone.BnagFer.domain.board.service.BoardBlockService;
import com.capstone.BnagFer.domain.board.service.BoardQueryService;
import com.capstone.BnagFer.domain.board.service.BoardService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.capstone.BnagFer.global.common.ApiResponse.onSuccess;

@RestController
@RequiredArgsConstructor
@Tag(name = "자유 게시판 API")
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final BoardBlockService boardBlockService;
    private final BoardQueryService boardQueryService;
    private final BoardBlockQueryService boardBlockQueryService;


    @GetMapping //게시판 리스트 조회
    @Operation(summary = "게시판 목록 조회", description = "전체 게시판 목록을 조회합니다. 페이징 적용, 생성 날짜 기준 내림차순 정렬.")
    public ApiResponse<Page<BoardListDto>> getBoardsList(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @LoginUser User user)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardListDto> boardsList = boardQueryService.getBoards(user, pageable);
        return ApiResponse.onSuccess(boardsList);

    }

    @Operation(summary = "개별 게시물 조회", description = "단일 게시물의 내용을 조회합니다.")
    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailResponseDto> getBoard (@LoginUser User user, @PathVariable(name = "boardId") Long boardId) {
        BoardDetailResponseDto board = boardQueryService.getBoard(user, boardId);
        return ApiResponse.onSuccess(board);
    }

    @Operation(summary = "내 게시물 목록 조회", description = "자신이 작성한 게시글 목록 조회. 페이징 적용, 생성 날짜 기준 내림차순 정렬.")
    @GetMapping("/myboards")
    public ApiResponse<Page<BoardListDto>> getMyBoardsList(
            @LoginUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardListDto> myBoardsList = boardQueryService.getMyBoards(user, pageable);
        return ApiResponse.onSuccess(myBoardsList);
    }

    @Operation(summary = "사용자 게시물 목록 조회", description = "특정 사람이 작성한 게시글 목록 조회. 페이징 적용, 생성 날짜 기준 내림차순 정렬.")
    @GetMapping("/users/{userId}/boards")
    public ApiResponse<Page<BoardListDto>> getUserBoardsList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @LoginUser User user)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardListDto> userBoardsList = boardQueryService.getUserBoards(user, userId, pageable);
        return ApiResponse.onSuccess(userBoardsList);
    }

    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다. 프로필이 생성이 된 후에 작성 가능.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<CreateBoardResponseDto> createBoard(@LoginUser User user,
                                                           @Valid @RequestPart("request") BoardRequestDto request,
                                                           @RequestPart(name = "image", required = false) List<MultipartFile> images) {
        CreateBoardResponseDto board = boardService.createBoard(request, user, images);
        return ApiResponse.onSuccess(board);
    }

    @Operation(summary = "게시글 수정", description = "자신의 게시글을 수정합니다. 게시글 작성자 만이 수정 가능")
    @PutMapping(value ="/{boardId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<CreateBoardResponseDto> updateBoard(@LoginUser User user,
                                                           @PathVariable(name = "boardId") long boardId,
                                                           @Valid @RequestPart("request") BoardRequestDto request,
                                                           @RequestPart(name = "image", required = false) List<MultipartFile> images) {
        CreateBoardResponseDto updatedBoard = boardService.updateBoard(boardId, request, user, images);
        return ApiResponse.onSuccess(updatedBoard);
    }

    @Operation(summary = "게시글 삭제", description = "자신의 게시글을 삭제합니다. 게시글 작성자 만이 삭제 가능")
    @DeleteMapping("/{boardId}")
    public ApiResponse<Object> deleteBoard(@PathVariable(name = "boardId") Long boardId, @LoginUser User user) {
        boardService.deleteBoard(boardId, user);
        return ApiResponse.noContent();
    }

    @Operation(summary = "게시글 좋아요 & 좋아요 취소", description = "게시글에 좋아요를 누르는 기능. 한번 더누르면 좋아요 취소.")
    @PostMapping("/{boardId}/like")
    public ApiResponse<Object> likeButton(@PathVariable(name = "boardId") Long boardId, @LoginUser User user) {
        return boardService.likeButton(boardId, user);
    }

    @Operation(summary = "게시글 댓글 달기", description = "게시글에 댓글을 다는 기능. 프로필 생성 후에 작성 가능.")
    @PostMapping("/{boardId}/comment")
    public ApiResponse<CommentResponseDto> postComment(@PathVariable(name = "boardId") Long boardId,
                                                       @Valid @RequestBody CreateCommentRequestDto request,
                                                       @LoginUser User user) {
        CommentResponseDto comment = boardService.createComment(boardId, request, user, null);
        return ApiResponse.onSuccess(comment);
    }

    @Operation(summary = "게시글 대댓글 달기", description = "전술 댓글에 대댓글을 다는 기능. 프로필 생성 후에 작성 가능.")
    @PostMapping("/{boardId}/comment/{parentCommentId}")
    public ApiResponse<CommentResponseDto> postComment(@PathVariable(name = "boardId") Long boardId,
                                                       @PathVariable(name = "parentCommentId") Long parentCommentId,
                                                       @Valid @RequestBody CreateCommentRequestDto request,
                                                       @LoginUser User user) {
        CommentResponseDto comment = boardService.createComment(boardId, request, user, parentCommentId);
        return ApiResponse.onSuccess(comment);
    }

    @Operation(summary = "게시글 댓글 수정", description = "자신의 댓글을 수정하는 기능. 댓글 작성자 만이 수정 가능.")
    @PutMapping("/comment/{commentId}")
    public ApiResponse<CommentResponseDto> updateComment(@PathVariable(name = "commentId") Long commentId,
                                                         @Valid @RequestBody UpdateCommentRequestDto request,
                                                         @LoginUser User user) {
        CommentResponseDto updatedComment = boardService.updateComment(commentId, request, user);
        return ApiResponse.onSuccess(updatedComment);
    }

    @Operation(summary = "게시글 댓글 삭제", description = "자신의 댓글을 삭제하는 기능. 댓글 작성자 만이 삭제 가능.")
    @DeleteMapping("/comment/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable(name = "commentId") Long commentId, @LoginUser User user) {
        boardService.deleteComment(commentId, user);
        return ApiResponse.noContent();
    }
    @Operation(summary = "자유게시판 사용자 차단", description = "차단을 하게되면, 차단한 사용자의 댓글, 게시글을 볼 수 없다.")
    @PostMapping("/block/{isBlockedUserId}")
    public ApiResponse<BoardBlockResponseDto> blockUser(@LoginUser User user, @PathVariable(name = "isBlockedUserId") Long isBlockedUserId) {
        BoardBlockResponseDto boardBlockResponseDto = boardBlockService.blockUser(user, isBlockedUserId);
        return ApiResponse.onSuccess(boardBlockResponseDto);
    }

    @Operation(summary = "자유게시판 사용자 차단해제", description = "차단을 해제하는 기능.")
    @DeleteMapping("/block/{isBlockedUserId}")
    public ApiResponse<Void> unblockUser(@LoginUser User user, @PathVariable(name = "isBlockedUserId") Long isBlockedUserId) {
        boardBlockService.unblockUser(user, isBlockedUserId);
        return ApiResponse.noContent();
    }
    @Operation(summary = "내가 차단한 사용자 조회", description = "내가 차단한 사용자를 조회해주는 기능")
    @GetMapping("/blocked-users")
    public ApiResponse<List<GetMyBoardBlockResponseDto>> getBlockedUsers(@LoginUser User currentUser) {
        List<GetMyBoardBlockResponseDto> blockedUsers = boardBlockQueryService.getBlockedUsers(currentUser);
        return ApiResponse.onSuccess(blockedUsers);
    }

}
