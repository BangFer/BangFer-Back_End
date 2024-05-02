package com.capstone.BnagFer.domain.tactic.controller;

import com.capstone.BnagFer.domain.tactic.dto.*;
import com.capstone.BnagFer.domain.tactic.service.TacticQueryService;
import com.capstone.BnagFer.domain.tactic.service.TacticService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tactics")
public class TacticController {
    private final TacticService tacticService;
    private final TacticQueryService tacticQueryService;

    // 전체 전술 게시판 조회
    @GetMapping
    public ApiResponse<List<TacticResponse.TacticList>> getTacticList(){
        List<TacticResponse.TacticList> tacticLists = tacticQueryService.getTactics();
        return ApiResponse.onSuccess(tacticLists);
    }

    // 개별 전술 게시판 조회
    @GetMapping("/{tacticId}")
    public ApiResponse<TacticDetailResponse> getTacticDetail(@PathVariable Long tacticId) {
        TacticDetailResponse tacticDetail = tacticQueryService.getTacticById(tacticId);
        return ApiResponse.onSuccess(tacticDetail);
    }

    // 자신의 전술 게시물 조회
    @GetMapping("/mylist")
    public ApiResponse<List<TacticResponse.TacticList>> getUserTactic() {
        List<TacticResponse.TacticList> userTacticLists = tacticQueryService.getUserTactics();
        return ApiResponse.onSuccess(userTacticLists);
    }

    // 전술 게시물 생성
    @PostMapping
    public ApiResponse<TacticResponse> createTactic(@RequestBody TacticCreateRequest request){
        TacticResponse tacticDetail = tacticService.createTactic(request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    // 자신의 전술 게시물 수정
    @PutMapping("/{tacticId}")
    public ApiResponse<TacticResponse> updateTactic(@PathVariable Long tacticId, @RequestBody TacticUpdateRequest request) {
        TacticResponse tacticDetail = tacticService.updateTactic(tacticId, request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    // 다른 사람의 전술 게시물을 그대로 복사하여 자기 전술 리스트에 새로 생성
    @PostMapping("/{tacticId}")
    public ApiResponse<TacticResponse> copyTactic(@PathVariable Long tacticId){
        TacticResponse tacticDetail = tacticService.copyTactic(tacticId);
        return ApiResponse.onSuccess(tacticDetail);
    }

    // 전술 게시물 삭제
    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> deleteTactic(@PathVariable Long tacticId){
        tacticService.deleteTactic(tacticId);
        return ApiResponse.noContent();
    }

    // 전술 게시물에 댓글 달기
    @PostMapping("/{tacticId}/comment")
    public ApiResponse<CommentResponse> createComment(@PathVariable Long tacticId, @RequestBody CommentCreateRequest request) {
        CommentResponse commentDetail = tacticService.createComment(tacticId, request);
        return ApiResponse.onSuccess(commentDetail);
    }

    // 전술 게시물 댓글 조희
    @PutMapping("/comment/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        CommentResponse commentDetail = tacticService.updateComment(commentId, request);
        return ApiResponse.onSuccess(commentDetail);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ApiResponse<Object> deleteComment(@PathVariable Long commentId){
        tacticService.deleteComment(commentId);
        return ApiResponse.noContent();
    }

    // 좋아요 누르기 및 취소하기
    @PostMapping("/{tacticId}/like")
    public ApiResponse<Object> likeToggle(@PathVariable Long tacticId) {
        return tacticService.likeButton(tacticId);
    }

}
