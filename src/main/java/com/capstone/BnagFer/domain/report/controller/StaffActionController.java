package com.capstone.BnagFer.domain.report.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.service.StaffActionQueryService;
import com.capstone.BnagFer.domain.report.service.StaffActionService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "staff 전용 API")
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffActionController {

    private final StaffActionService staffActionService;
    private final StaffActionQueryService staffActionQueryService;
    @Operation(summary = "UserActivity값을 기준으로 한 조회")
    @GetMapping("/users")
    public ApiResponse<List<UserResponseDto>> getUserByActivity(@RequestParam UserActivity activity) {
        List<UserResponseDto> userActivityPage = staffActionQueryService.getUserByActivity(activity);
        return ApiResponse.onSuccess(userActivityPage);
    }
    @Operation(summary = "특정 User의 신고 기록 조회")
    @GetMapping("/users/{userId}")
    public ApiResponse<List<UserReportResponseDto>> getUserReportRecord(@PathVariable Long userId) {
        List<UserReportResponseDto> userReportPage = staffActionQueryService.getUserReportRecord(userId);
        return ApiResponse.onSuccess(userReportPage);
    }
    @Operation(summary = "UserActivity값 변경",  description="staff 권한을 가진 사용자가 신고 받은 사용자의 UserActivity를 FLAGGED에서 BAN으로 변경 혹은 차단 해제 시, BAN에서 NORMAL으로 변경")
    @PutMapping("users/{userId}")
    public ApiResponse<UserResponseDto> changeUserActivity(@PathVariable Long userId, @RequestParam UserActivity userActivity) {
        UserResponseDto userDto = staffActionService.changeUserActivity(userActivity, userId);
        return ApiResponse.onSuccess(userDto);
    }
    @Operation(summary = "스태프 권한 부여")
    @PostMapping("/grant-staff/{userId}")
    public ApiResponse<UserResponseDto> grantStaffAuthority(@PathVariable Long userId) {
        // isStaff 값을 true로 설정하여 grantStaffAuthority 호출
        UserResponseDto responseDto = staffActionService.grantStaffAuthority(userId);
        return ApiResponse.onSuccess(responseDto);
    }
    @Operation(summary = "스태프 권한 해제")
    @PostMapping("/revoke-staff/{userId}")
    public ApiResponse<UserResponseDto> revokeStaffAuthority(@PathVariable Long userId) {
        // isStaff 값을 false로 설정하여 revokeStaffAuthority 호출
        UserResponseDto responseDto = staffActionService.revokeStaffAuthority(userId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "악의적 게시글 삭제")
    @DeleteMapping("/{boardId}")
    public ApiResponse<Object> adminDeleteBoard(@PathVariable(name = "boardId") Long boardId) {
        staffActionService.deleteBoard(boardId);
        return ApiResponse.noContent();
    }

    @Operation(summary = "악의적 게시글 댓글 삭제")
    @DeleteMapping("/{boardCommentId}")
    public ApiResponse<Object> adminDeleteComment(@PathVariable(name = "boardCommentId") Long boardCommentId) {
        staffActionService.deleteComment(boardCommentId);
        return ApiResponse.noContent();
    }

    @Operation(summary = "악의적 전술 댓글 삭제")
    @DeleteMapping("/{tacticCommentId}")
    public ApiResponse<Object> adminDeleteTacticComment(@PathVariable(name = "tacticCommentId") Long tacticCommentId) {
        staffActionService.deleteTacticComment(tacticCommentId);
        return ApiResponse.noContent();
    }

    @Operation(summary = "악의적 전술 삭제")
    @DeleteMapping("/{tacticId}")
    public ApiResponse<Object> adminDeleteTactic(@PathVariable(name = "tacticId") Long tacticId) {
        staffActionService.deleteTactic(tacticId);
        return ApiResponse.noContent();
    }
}
