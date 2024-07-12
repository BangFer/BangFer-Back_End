package com.capstone.BnagFer.domain.myteam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationStatus {
    PENDING("초대가 발송되었으나 아직 수락되거나 거절되지 않은 상태"),
    ACCEPTED("초대가 수락된 상태"),
    REJECTED("초대가 거절된 상태");
    private final String description;
}
