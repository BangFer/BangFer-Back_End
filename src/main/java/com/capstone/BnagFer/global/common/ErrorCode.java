package com.capstone.BnagFer.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Accounts 관련 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER401", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER402", "닉네임은 필수 입니다."),
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "USER403", "비밀번호가 일치하지 않습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER404", "사용자가 이미 존재합니다."),
    USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER405", "사용자가 맞지 않습니다. 권한이 없습니다."),
    EMAIL_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER406", "사용자 이메일이 존재하지 않습니다."),
    PROFILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER407", "사용자 프로필이 존재하지 않습니다."),
    NO_USER_AUTHORIZATION(HttpStatus.BAD_REQUEST, "USER408", "사용자 권한이 없습니다."),
    USER_IS_DELETED(HttpStatus.BAD_REQUEST, "USER409", "탈퇴된 회원입니다."),
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "USER410", "이메일을 전송할 수 없습니다."),
    CODE_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "USER411", "유효하지 않은 코드입니다."),

    // Profile 관련 에러
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "PROFILE401", "해당 닉네임이 이미 존재합니다."),
    PROFILE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "PROFILE402", "프로필이 이미 존재합니다."),
    PROFILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PROFILE403", "해당 프로필이 존재하지 않습니다."),
    PROFILE_AND_USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "PROFILE404", "자신의 프로필이 아닙니다. 권한이 없습니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "PROFILE404", "해당 이메일이 이미 존재합니다."),

    // Tactic 관련 에러
    TACTIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "TACTIC401", "전술이 없습니다."),
    TACTIC_EMPTY_ID(HttpStatus.BAD_REQUEST, "TACTIC402", "전술 아이디 값을 확인해주세요."),
    TACTIC_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "TACTIC403", "본인 전술만 적용할 수 있습니다."),
    CANNOT_COPY_MYSELF(HttpStatus.BAD_REQUEST, "TACTIC404", "본인이 직접 작성한 전술은 카피할 수 없습니다."),

    // Comment 관련 에러
    Comment_NOT_FOUND(HttpStatus.BAD_REQUEST, "TACTIC401", "댓글이 없습니다."),
    USERANDTACTIC_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER402", "사용자와 전술게시물이 맞지 않습니다. 권한이 없습니다."),

    //TEAM 관련 에러
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM401", "팀이 없습니다."),
    NO_AUTHORIZATION(HttpStatus.BAD_REQUEST,"TEAM402", "권한이 없습니다."),
    //TEAM_MEMBER 관련 에러
    CANNOT_INVITE(HttpStatus.BAD_REQUEST, "TEAMMEMBER401", "팀원을 초대할 수 없습니다."),
    TEAMMEMBER_EXISTS(HttpStatus.BAD_REQUEST, "TEAMMEMBER402", "이미 초대된 인원입니다."),
    CANNOT_FIND_TEAMMEMBER(HttpStatus.BAD_REQUEST, "TEAMMEMBER403", "해당 팀원이 존재하지 않습니다."),
    ALREAY_KICKED_OUT(HttpStatus.BAD_REQUEST, "TEAMMEMBER404", "이미 강퇴된 회원입니다."),
    POSITION_ALREADY_ALLOCATED(HttpStatus.BAD_REQUEST, "TEAMMEMBER406", "이미 포지션이 할당되었습니다."),
    POSITION_CANNOT_BE_DUPLIACTED(HttpStatus.BAD_REQUEST, "TEAMMEMBER407", "포지션은 중복될 수 없습니다."),
    POSITION_ALREADY_DEALLOCATED(HttpStatus.BAD_REQUEST, "TEAMMEMBER408", "이미 포지션이 할당 해제되었습니다."),
    CANNOT_ALLOCATE(HttpStatus.BAD_REQUEST, "TEAMMEMBER409", "포지션 할당 권한이 없습니다."),
    CANNOT_DEALLOCATE(HttpStatus.BAD_REQUEST, "TEAMMEMBER410", "포지션 할당 해제 권한이 없습니다."),
    //CALENDAR_EVENT 관련 에러
    MATCH_EVENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "CALENDAREVENT401", "매치 일정이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
