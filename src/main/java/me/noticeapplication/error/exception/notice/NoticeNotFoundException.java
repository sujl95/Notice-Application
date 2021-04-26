package me.noticeapplication.error.exception.notice;

import me.noticeapplication.common.exception.NoticeException;
import me.noticeapplication.common.exception.ExceptionStatus;

public class NoticeNotFoundException extends NoticeException {

	public NoticeNotFoundException() {
		super(ExceptionStatus.NOTICE_NOT_FOUND);
	}
}
