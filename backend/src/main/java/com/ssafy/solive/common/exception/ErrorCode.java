package com.ssafy.solive.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

    /* HttpStatus로 할 지, 임의로 Customg한 코드(Int type)으로 할 지 결정해야 함. 우선은 HttpStatus */

    /**
     * 비즈니스 에러 (확인 가능한 예외 상황)
     */
    UN_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    UNAUTHORIZED_ROLE(HttpStatus.UNAUTHORIZED, "현재 유저 권한으로는 접근할 수 없는 리소스 요청입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임 입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NO_IMAGE(HttpStatus.BAD_REQUEST, "요청에 이미지가 없습니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "이미지 업로드를 실패했습니다."),

    /**
     * 서버 에러 (서버 장애 상황)
     */
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류입니다. 잠시 후 다시 이용해주세요.");
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
