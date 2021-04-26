package me.noticeapplication.persistence.notice.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.noticeapplication.notice.dto.NoticeWrite;
import me.noticeapplication.persistence.user.entity.UserEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name = "notice")
public class NoticeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username")
	private String username;

	@Column(name = "title", length = 150, nullable = false)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@CreationTimestamp
	@Column(name = "reg_date", updatable = false)
	private LocalDateTime regDate;

	@CreationTimestamp
	@Column(name = "mod_date")
	private LocalDateTime modDate;

	@Builder
	public NoticeEntity(Long id, Long userId, String username, String title, String content, Long fileId) {
		this.id 		= id;
		this.userId 	= userId;
		this.username 	= username;
		this.title 		= title;
		this.content 	= content;
	}

	@Builder
	public static NoticeEntity from(NoticeWrite noticeWrite, UserEntity userEntity) {
		return NoticeEntity.builder()
				.userId(userEntity.getId())
				.username(userEntity.getUsername())
				.title(noticeWrite.getTitle())
				.content(noticeWrite.getContent())
				.build();
	}
}
