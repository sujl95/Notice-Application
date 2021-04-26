package me.noticeapplication.common.exception;

import org.springframework.http.HttpStatus;

public class NoticeException extends RuntimeException{

	private final ExceptionStatus exceptionStatus;

	public NoticeException(ExceptionStatus exceptionStatus) {
		super(exceptionStatus.getMessage());
		this.exceptionStatus = exceptionStatus;
	}

	public String getStatus() {
		return String.valueOf(exceptionStatus.getStatus());
	}

	public HttpStatus getHttpStatus() {
		return exceptionStatus.getHttpStatus();
	}

}
