package com.capstone.BnagFer.domain.tactic.controller;

import com.capstone.BnagFer.domain.tactic.dto.TacticRequest;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
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
    public ApiResponse<TacticResponse> getTacticDetail(@PathVariable Long tacticId) {
        TacticResponse tacticDetail = tacticQueryService.getTacticById(tacticId);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @PostMapping
    public ApiResponse<TacticResponse> createTactic(@RequestBody TacticRequest.CreateDTO request){
        TacticResponse tacticDetail = tacticService.createTactic(request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @PutMapping("/{tacticId}")
    public ApiResponse<TacticResponse> updateMyTeam(@PathVariable Long tacticId, @RequestBody TacticRequest.UpdateDTO request) {
        TacticResponse tacticDetail = tacticService.updateTactic(tacticId, request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> deleteTactic(@PathVariable Long tacticId){
        tacticService.deleteTactic(tacticId);
        return ApiResponse.noContent();
    }

}
