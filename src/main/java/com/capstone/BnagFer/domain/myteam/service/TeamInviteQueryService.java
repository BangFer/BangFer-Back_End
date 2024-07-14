package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.response.TeamInviteResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamInviteRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamInviteQueryService {
    private final TeamInviteRepository teamInviteRepository;
    public List<TeamInviteResponseDto> getMyInvitations(User user) {
        List<TeamInvite> teamInvites = teamInviteRepository.findByInvitedUser(user);

        if (teamInvites.isEmpty()) {
            throw new TeamMemberExceptionHandler(ErrorCode.INVITE_NOT_FOUND);
        }
        return teamInvites.stream()
                .map(TeamInviteResponseDto::from)
                .collect(Collectors.toList());
    }
}
