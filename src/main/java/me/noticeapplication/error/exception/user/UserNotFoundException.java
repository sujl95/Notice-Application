package me.noticeapplication.error.exception.user;

import me.noticeapplication.common.exception.NoticeException;
import me.noticeapplication.common.exception.ExceptionStatus;

public class UserNotFoundException extends NoticeException {

	public UserNotFoundException() {
		super(ExceptionStatus.USER_NOT_FOUND);
	}
}
