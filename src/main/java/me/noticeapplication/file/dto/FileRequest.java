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
public class FileRequest {

	private String origFilename;
	private String filename;
	private String filePath;
	private Long noticeId;

	@Builder
	public FileRequest(String origFilename, String filename, String filePath, Long noticeId) {
		this.origFilename = origFilename;
		this.filename = filename;
		this.filePath = filePath;
		this.noticeId = noticeId;
	}


	public FileEntity toEntity() {
		return FileEntity.builder()
				.origFilename(origFilename)
				.filename(filename)
				.filePath(filePath)
				.noticeId(noticeId)
				.build();
	}
}
