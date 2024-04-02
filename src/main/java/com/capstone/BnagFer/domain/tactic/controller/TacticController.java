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

    @GetMapping
    public ApiResponse<List<TacticResponse.TacticList>> getTacticList(){
        List<TacticResponse.TacticList> tacticLists = tacticQueryService.getTactics();
        return ApiResponse.onSuccess(tacticLists);
    }

    @GetMapping("/{tacticId}")
    public ApiResponse<TacticDetailResponse> getTacticDetail(@PathVariable Long tacticId) {
        TacticDetailResponse tacticDetail = tacticQueryService.getTacticById(tacticId);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @GetMapping("/mylist")
    public ApiResponse<List<TacticResponse.TacticList>> getUserTactic() {
        List<TacticResponse.TacticList> userTacticLists = tacticQueryService.getUserTactics();
        return ApiResponse.onSuccess(userTacticLists);
    }

    @PostMapping
    public ApiResponse<TacticResponse> createTactic(@RequestBody TacticCreateRequest request){
        TacticResponse tacticDetail = tacticService.createTactic(request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @PutMapping("/{tacticId}")
    public ApiResponse<TacticResponse> updateTactic(@PathVariable Long tacticId, @RequestBody TacticUpdateRequest request) {
        TacticResponse tacticDetail = tacticService.updateTactic(tacticId, request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> deleteTactic(@PathVariable Long tacticId){
        tacticService.deleteTactic(tacticId);
        return ApiResponse.noContent();
    }

    @PostMapping("/{tacticId}/comment")
    public ApiResponse<CommentResponse> createComment(@PathVariable Long tacticId, @RequestBody CommentCreateRequest request) {
        CommentResponse commentDetail = tacticService.createComment(tacticId, request);
        return ApiResponse.onSuccess(commentDetail);
    }

    @PutMapping("/comment/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        CommentResponse commentDetail = tacticService.updateComment(commentId, request);
        return ApiResponse.onSuccess(commentDetail);
    }

    @DeleteMapping("/comment/{commentId}")
    public ApiResponse<Object> deleteComment(@PathVariable Long commentId){
        tacticService.deleteComment(commentId);
        return ApiResponse.noContent();
    }

    @PostMapping("/{tacticId}/like")
    public ApiResponse<Object> likeToggle(@PathVariable Long tacticId) {
        return tacticService.likeButton(tacticId);
    }

}
