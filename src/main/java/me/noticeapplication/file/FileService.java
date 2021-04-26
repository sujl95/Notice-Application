package me.noticeapplication.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noticeapplication.common.util.MD5Generator;
import me.noticeapplication.file.dto.FileDto;
import me.noticeapplication.file.dto.FileRequest;
import me.noticeapplication.persistence.file.entity.FileEntity;
import me.noticeapplication.persistence.file.repository.FileRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;

	@Transactional(readOnly = true)
	public FileDto getFile(Long id) {
		FileEntity fileEntity = fileRepository.findById(id).orElseThrow();
		return FileDto.builder()
				.id(id)
				.origFilename(fileEntity.getOrigFilename())
				.filename(fileEntity.getFilename())
				.filePath(fileEntity.getFilePath())
				.build();
	}

	@Transactional(readOnly = true)
	public List<FileDto> getFiles(Long id) {
		List<FileEntity> fileEntities = fileRepository.findByNoticeId(id);
		return fileEntities.stream()
				.map(FileEntity::from)
				.collect(Collectors.toList());
	}

	@Transactional
	public void addFiles(MultipartFile[] files, long noticeId) {
		Arrays.stream(files)
				.map((MultipartFile file) -> addFile(file, noticeId))
				.collect(Collectors.toList());
	}

	@Transactional
	public FileDto addFile(MultipartFile file, long noticeId) {
		FileEntity fileEntity = null;
		try {
			String originFilename = file.getOriginalFilename();
			assert originFilename != null;
			String filename = new MD5Generator(originFilename).toString();
			String savePath = System.getProperty("user.dir") + "\\files";
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdir();
				} catch (Exception e) {
					log.info("file save Exception");
				}
			}
			String filePath = savePath + "\\" + filename;
			file.transferTo(new File(filePath));
			FileRequest fileRequest = FileRequest.builder()
					.noticeId(noticeId)
					.origFilename(originFilename)
					.filename(filename)
					.filePath(filePath)
					.build();
			fileEntity = fileRepository.save(fileRequest.toEntity());
		} catch (Exception e) {
			log.info("file write exception");
		}
		assert fileEntity != null;
		return FileEntity.from(fileEntity);
	}
}
