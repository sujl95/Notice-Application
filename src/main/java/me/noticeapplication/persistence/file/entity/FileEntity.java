package me.noticeapplication.persistence.file.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.noticeapplication.file.dto.FileDto;

@Getter
@Setter
@Entity
@Table(
		name = "file",
		indexes = @Index(name = "idx_notice_id", columnList = "notice_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "notice_id")
	private Long noticeId;

	@Column(name = "orig_filename")
	private String origFilename;

	@Column(name = "filename")
	private String filename;

	@Column(name = "file_path")
	private String filePath;

	@CreationTimestamp
	@Column(name = "reg_date")
	private LocalDateTime regDate;

	@Builder
	public FileEntity(Long id, Long noticeId, String origFilename, String filename, String filePath,
			LocalDateTime regDate) {
		this.id = id;
		this.noticeId = noticeId;
		this.origFilename = origFilename;
		this.filename = filename;
		this.filePath = filePath;
		this.regDate = regDate;
	}

	public static FileDto from (FileEntity fileEntity) {
		return FileDto.builder()
				.id(fileEntity.getId())
				.noticeId(fileEntity.getNoticeId())
				.origFilename(fileEntity.getOrigFilename())
				.filename(fileEntity.getFilename())
				.filePath(fileEntity.getFilePath())
				.build();
	}
}
