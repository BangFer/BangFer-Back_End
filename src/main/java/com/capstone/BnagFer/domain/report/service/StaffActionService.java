package com.capstone.BnagFer.domain.report.service;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class StaffActionService {
    private final UserJpaRepository userRepository;

    public UserResponseDto grantStaffAuthority(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if(user.getIsStaff().equals(Boolean.FALSE) && !user.getUserActivity().equals(UserActivity.BAN)
                && !user.getUserActivity().equals(UserActivity.FLAGGED) && user.getId().equals(userId)) {
            user.grantStaffAuthority();
            userRepository.save(user);
        }
        else {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        return UserResponseDto.from(user);
    }
    public UserResponseDto revokeStaffAuthority(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if(user.getIsStaff().equals(Boolean.TRUE) && user.getId().equals(userId)) {
            user.revokeStaffAuthority();
            userRepository.save(user);
        }
        else {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        return UserResponseDto.from(user);
    }
    public UserResponseDto changeUserActivity(UserActivity userActivity, Long reportedUserId) {
        User reportedUser = userRepository.findById(reportedUserId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        reportedUser.changeActivity(userActivity);
        return UserResponseDto.from(reportedUser);
    }
}
