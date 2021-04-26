package me.noticeapplication.notice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.noticeapplication.persistence.notice.entity.NoticeEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDto {
	private Long id;

	private String title;

	private String content;
	private String username;

	private Long userId;

	private LocalDateTime regDate;

	private LocalDateTime modDate;
	private Long fileId;

	@Builder
	public NoticeDto(Long id, String title, String content, String username, Long userId, Long fileId) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.username = username;
		this.userId = userId;
		this.fileId = fileId;
	}

}
