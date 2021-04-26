package me.noticeapplication.error.exception.user;

import me.noticeapplication.common.exception.NoticeException;
import me.noticeapplication.common.exception.ExceptionStatus;

public class UserDuplicateUserNameException extends NoticeException {
	public UserDuplicateUserNameException() {
		super(ExceptionStatus.USER_NAME_ALREADY_EXISTS);
	}
}
