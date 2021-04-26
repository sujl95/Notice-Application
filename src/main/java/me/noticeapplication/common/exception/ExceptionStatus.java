package me.noticeapplication.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {
	//USER
	USER_NAME_ALREADY_EXISTS(4001, "이미 존재하는 유저가 있습니다.", BAD_REQUEST),

	//Notice
	NOTICE_NOT_MATCH_USER_ID(4101, "공지글이 존재하지 않거나 작성자와 일치하지 않습니다", BAD_REQUEST),


	USER_NOT_FOUND(4401, "유저를 찾을 수 없습니다.", NOT_FOUND),
	NOTICE_NOT_FOUND(4402, "공지글을 찾을 수 없습니다.", NOT_FOUND),

	// Common
	USER_NOT_AUTH(401, "로그인을 진행해주세요.", UNAUTHORIZED),
	NOT_GRANT(403, "관리자만 등록, 수정, 삭제가 가능합니다.", FORBIDDEN),
	RUN_TIME_EXCEPTION(500, "런타임 에러", INTERNAL_SERVER_ERROR),
	NOT_FOUND_EXCEPTION(404, "요청한 리소스가 존재하지 않습니다.", NOT_FOUND),
	INVALID_TYPE_VALUE_EXCEPTION(400, "유효하지 않는 Type의 값입니다. 입력 값을 확인 해주세요.", BAD_REQUEST),
	INVALID_FORMAT_EXCEPTION(400, "유효하지 않는 Type 입니다. Type을 확인 해주세요.", BAD_REQUEST),
	INVALID_INPUT_VALUE(400, "유효하지 않는 입력 값입니다.", BAD_REQUEST),
	METHOD_NOT_SUPPORT(405, "지원하지 않은 HTTP Method 입니다.", METHOD_NOT_ALLOWED);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

}
