package me.noticeapplication.notice.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.noticeapplication.persistence.notice.entity.NoticeEntity;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDetail {

	private Long id;
	private String username;
	private String title;
	private String content;
	private LocalDateTime regDate;
	private LocalDateTime modDate;

	@Builder
	public NoticeDetail(Long id, String username, String title, String content, LocalDateTime regDate,
			LocalDateTime modDate) {
		this.id = id;
		this.username = username;
		this.title = title;
		this.content = content;
		this.regDate = regDate;
		this.modDate = modDate;
	}

	public static NoticeDetail from (NoticeEntity noticeEntity) {
		return NoticeDetail.builder()
				.id(noticeEntity.getId())
				.username(noticeEntity.getUsername())
				.title(noticeEntity.getTitle())
				.content(noticeEntity.getContent())
				.modDate(noticeEntity.getModDate())
				.regDate(noticeEntity.getRegDate())
				.build();
	}

}
