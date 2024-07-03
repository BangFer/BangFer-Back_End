package com.capstone.BnagFer.domain.tactic.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.dto.*;
import com.capstone.BnagFer.domain.tactic.service.TacticQueryService;
import com.capstone.BnagFer.domain.tactic.service.TacticService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "전술 API")
@RequestMapping("/api/v1/tactics")
public class TacticController {

    private final TacticService tacticService;
    private final TacticQueryService tacticQueryService;

    @Operation(summary = "전술 목록 조회", description = "전체 전술 목록을 조회합니다. 단, 공개(anonymous가 false) 인 전술만 조회. 페이징 적용, 생성 날짜 기준 내림차순 정렬.")
    @GetMapping
    public ApiResponse<Page<TacticResponse.TacticList>> getTacticList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<TacticResponse.TacticList> tacticLists = tacticQueryService.getTactics(pageable);
        return ApiResponse.onSuccess(tacticLists);
    }

    @Operation(summary = "개별 전술 조회", description = "단일 전술의 내용을 조회합니다.")
    @GetMapping("/{tacticId}")
    public ApiResponse<TacticDetailResponse> getTacticDetail(@PathVariable(name = "tacticId") Long tacticId) {
        TacticDetailResponse tacticDetail = tacticQueryService.getTacticById(tacticId);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @Operation(summary = "내 전술 목록 조회", description = "자신이 작성한 전술, 가져온 전술 조회. 페이징 적용, 생성 날짜 기준 내림차순 정렬.")
    @GetMapping("/mylist")
    public ApiResponse<Page<TacticResponse.TacticList>> getUserTactic(
            @LoginUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<TacticResponse.TacticList> userTacticLists = tacticQueryService.getUserTactics(user, pageable);
        return ApiResponse.onSuccess(userTacticLists);
    }

    @Operation(summary = "전술 생성", description = "전술을 생성합니다. 프로필이 생성이 된 후에 작성 가능.")
    @PostMapping
    public ApiResponse<TacticResponse> createTactic(@Valid @RequestBody TacticCreateRequest request, @LoginUser User user){
        TacticResponse tacticDetail = tacticService.createTactic(request, user);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @Operation(summary = "전술 수정", description = "자신이 작성한 전술을 수정합니다. 전술 작성자 만이 수정 가능.")
    @PutMapping("/{tacticId}")
    public ApiResponse<TacticResponse> updateTactic(@PathVariable(name = "tacticId") Long tacticId,
                                                    @Valid @RequestBody TacticUpdateRequest request,
                                                    @LoginUser User user) {
        TacticResponse tacticDetail = tacticService.updateTactic(tacticId, request, user);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @Operation(summary = "전술 복사후 가져오기", description = "다른 사람의 전술을 복사해서 작성자를 자신으로 하여 저장.")
    @PostMapping("/{tacticId}")
    public ApiResponse<TacticResponse> copyTactic(@PathVariable(name = "tacticId") Long tacticId, @LoginUser User user){
        TacticResponse tacticDetail = tacticService.copyTactic(tacticId, user);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @Operation(summary = "전술 삭제", description = "자신의 전술 삭제. 전술 작성자 만이 삭제 가능.")
    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> deleteTactic(@PathVariable(name = "tacticId") Long tacticId, @LoginUser User user){
        tacticService.deleteTactic(tacticId, user);
        return ApiResponse.noContent();
    }

    @Operation(summary = "전술 댓글 달기", description = "전술 게시글에 댓글을 다는 기능. 프로필 생성 후에 작성 가능.")
    @PostMapping("/{tacticId}/comment")
    public ApiResponse<CommentResponse> createComment(@PathVariable(name = "tacticId") Long tacticId,
                                                      @Valid @RequestBody CommentCreateRequest request, @LoginUser User user) {
        CommentResponse commentDetail = tacticService.createComment(tacticId, request, user, null);
        return ApiResponse.onSuccess(commentDetail);
    }

    @Operation(summary = "전술 대댓글 달기", description = "전술 댓글에 대댓글을 다는 기능. 프로필 생성 후에 작성 가능.")
    @PostMapping("/{tacticId}/comment/{parentCommentId}")
    public ApiResponse<CommentResponse> createReplyComment(@PathVariable(name = "tacticId") Long tacticId,
                                                           @PathVariable(name = "parentCommentId") Long parentCommentId,
                                                           @Valid @RequestBody CommentCreateRequest request,
                                                           @LoginUser User user) {
        CommentResponse commentDetail = tacticService.createComment(tacticId, request, user, parentCommentId);
        return ApiResponse.onSuccess(commentDetail);
    }

    @Operation(summary = "전술 댓글 수정", description = "자신의 댓글을 수정하는 기능. 댓글 작성자 만이 수정 가능.")
    @PutMapping("/comment/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable(name = "commentId") Long commentId,
                                                      @Valid @RequestBody CommentUpdateRequest request,
                                                      @LoginUser User user) {
        CommentResponse commentDetail = tacticService.updateComment(commentId, request, user);
        return ApiResponse.onSuccess(commentDetail);
    }

    @Operation(summary = "전술 댓글 삭제", description = "자신의 댓글을 삭제하는 기능. 댓글 작성자 만이 삭제 가능.")
    @DeleteMapping("/comment/{commentId}")
    public ApiResponse<Object> deleteComment(@PathVariable(name = "commentId") Long commentId, @LoginUser User user){
        tacticService.deleteComment(commentId, user);
        return ApiResponse.noContent();
    }

    @Operation(summary = "전술 좋아요 & 좋아요 취소", description = "전술에 좋아요를 누르는 기능. 한번 더누르면 좋아요 취소.")
    @PostMapping("/{tacticId}/like")
    public ApiResponse<Object> likeToggle(@PathVariable(name = "tacticId") Long tacticId, @LoginUser User user) {
        return tacticService.likeButton(tacticId, user);
    }
}
