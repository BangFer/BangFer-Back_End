package com.capstone.BnagFer.domain.tactic.controller;

import com.capstone.BnagFer.domain.tactic.dto.TacticRequest;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.service.TacticService;
import com.capstone.BnagFer.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tactics")
public class TacticController {
    private final TacticService tacticService;

    @PostMapping
    public ApiResponse<TacticResponse.tacticDetail> createTactic(@RequestBody TacticRequest.CreateDTO request){
        TacticResponse.tacticDetail tacticDetail = tacticService.createTactic(request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @PutMapping("/{tacticId}")
    public ApiResponse<TacticResponse.tacticDetail> updateMyTeam(@PathVariable Long tacticId, String userEmail, @RequestBody TacticRequest.UpdateDTO request) {
        TacticResponse.tacticDetail tacticDetail = tacticService.updateTactic(request);
        return ApiResponse.onSuccess(tacticDetail);
    }

    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> deleteTactic(@PathVariable Long tacticId){
        tacticService.deleteTactic(tacticId);
        return ApiResponse.noContent();
    }

}
