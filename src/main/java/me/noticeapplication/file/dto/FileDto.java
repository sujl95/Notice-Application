package me.noticeapplication.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.noticeapplication.persistence.file.entity.FileEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
	private Long id;
	private String origFilename;
	private String filename;
	private String filePath;
	private Long noticeId;

	@Builder
	public FileDto(Long id, String origFilename, String filename, String filePath, Long noticeId) {
		this.id = id;
		this.origFilename = origFilename;
		this.filename = filename;
		this.filePath = filePath;
		this.noticeId = noticeId;
	}
}
