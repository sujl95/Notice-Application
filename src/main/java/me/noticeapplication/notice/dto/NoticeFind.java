package me.noticeapplication.notice.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.noticeapplication.persistence.notice.entity.NoticeEntity;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeFind {

	private Long id;
	private Long userId;
	private String username;
	private String title;
	private String content;
	private LocalDateTime modDate;

	@Builder
	public NoticeFind(Long id, Long userId, String username, String title, String content, LocalDateTime modDate) {
		this.id 		= id;
		this.userId 	= userId;
		this.username 	= username;
		this.title 		= title;
		this.content 	= content;
		this.modDate 	= modDate;
	}

	public static NoticeFind from (NoticeEntity noticeEntity) {
		return NoticeFind.builder()
				.id(noticeEntity.getId())
				.userId(noticeEntity.getUserId())
				.username(noticeEntity.getUsername())
				.title(noticeEntity.getTitle())
				.content(noticeEntity.getContent())
				.modDate(noticeEntity.getModDate())
				.build();
	}
}
