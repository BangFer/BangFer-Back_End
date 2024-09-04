package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardCommentRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.entity.NotificationTemplate;
import com.capstone.BnagFer.domain.notification.event.CommentCreatedEvent;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommentEventHandler extends BaseNotificationEventHandler<CommentCreatedEvent> {
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final UserJpaRepository userJpaRepository;

    public CommentEventHandler(FcmNotificationService fcmNotificationService,
                               BoardRepository boardRepository,
                               BoardCommentRepository boardCommentRepository,
                               UserJpaRepository userJpaRepository) {
        super(fcmNotificationService);
        this.boardRepository = boardRepository;
        this.boardCommentRepository = boardCommentRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    protected FcmNotificationRequestDto createNotificationRequest(CommentCreatedEvent event) {
        String commenterNickname = userJpaRepository.findById(event.getAuthorId())
                .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.USER_NOT_FOUND))
                .getProfile().getNickname();

        Map<String, String> params = new HashMap<>();
        params.put("nickname", commenterNickname);
        params.put("contentType", "게시글");

        NotificationTemplate template = event.getParentCommentId() == null ?
                NotificationTemplate.NEW_COMMENT : NotificationTemplate.NEW_REPLY;

        return new FcmNotificationRequestDto(
                template.getTitle(),
                template.getBody(params)
        );
    }

    @Override
    protected Long getRecipientId(CommentCreatedEvent event) {
        if (event.getParentCommentId() == null) {
            return boardRepository.findById(event.getBoardId())
                    .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND))
                    .getUser().getId();
        } else {
            return boardCommentRepository.findById(event.getParentCommentId())
                    .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND))
                    .getUser().getId();
        }
    }
}