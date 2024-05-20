package com.capstone.BnagFer.domain.myteam.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.myteam.dto.response.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamTacticQueryService {

    private final AccountsCommonService accountsCommonService;

    public List<CreateTeamTacticResponseDto.MyTacticList> getMyTacticList(){
        User user = accountsCommonService.getCurrentUser();
        List<Tactic> tactics = user.getTactics();
        return CreateTeamTacticResponseDto.MyTacticList.from(tactics);
    }
}
