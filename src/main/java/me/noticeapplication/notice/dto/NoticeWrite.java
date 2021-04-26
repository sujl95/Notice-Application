package me.noticeapplication.notice.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeWrite {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@Builder
	public NoticeWrite(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
