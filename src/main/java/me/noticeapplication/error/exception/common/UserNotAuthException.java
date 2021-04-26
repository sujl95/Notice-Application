package me.noticeapplication.error.exception.common;

import me.noticeapplication.common.exception.ExceptionStatus;
import me.noticeapplication.common.exception.NoticeException;

public class UserNotAuthException extends NoticeException {

	public UserNotAuthException() {
		super(ExceptionStatus.USER_NOT_AUTH);
	}
}
