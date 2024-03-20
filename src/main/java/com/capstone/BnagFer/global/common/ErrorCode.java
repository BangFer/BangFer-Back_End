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

    // 멤버 관련 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER401", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER402", "닉네임은 필수 입니다."),
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "USER403", "비밀번호가 일치하지 않습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER404", "사용자가 이미 존재합니다."),
    USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER405", "사용자가 맞지 않습니다. 권한이 없습니다."),

    // Tactic 관련 에러
    TACTIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "TACTIC401", "전술이 없습니다."),
    TACTIC_EMPTY_ID(HttpStatus.BAD_REQUEST, "USER402", "전술 아이디 값을 확인해주세요."),

    //TEAM 관련 에러
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM401", "팀이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
