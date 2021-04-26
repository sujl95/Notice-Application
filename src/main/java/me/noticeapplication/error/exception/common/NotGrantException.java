package me.noticeapplication.error.exception.common;

import me.noticeapplication.common.exception.ExceptionStatus;
import me.noticeapplication.common.exception.NoticeException;

public class NotGrantException extends NoticeException {
	public NotGrantException() {
		super(ExceptionStatus.NOT_GRANT);
	}
}
