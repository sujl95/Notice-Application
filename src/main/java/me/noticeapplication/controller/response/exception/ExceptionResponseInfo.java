package me.noticeapplication.controller.response.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.noticeapplication.common.exception.NoticeException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponseInfo {

	private final String status;
	private final String message;

	public static ExceptionResponseInfo from(NoticeException noticeException) {
		return new ExceptionResponseInfo(noticeException.getStatus(), noticeException.getMessage());
	}
}
