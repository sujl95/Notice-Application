package me.noticeapplication.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.error.exception.common.NotGrantException;
import me.noticeapplication.error.exception.notice.NoticeNotFoundException;
import me.noticeapplication.error.exception.user.UserNotFoundException;
import me.noticeapplication.file.FileService;
import me.noticeapplication.notice.dto.NoticeDetail;
import me.noticeapplication.notice.dto.NoticeFind;
import me.noticeapplication.notice.dto.NoticeWrite;
import me.noticeapplication.persistence.notice.entity.NoticeEntity;
import me.noticeapplication.persistence.notice.repository.NoticeRepository;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final static Integer PAGE_SIZE = 20;

	private final NoticeRepository noticeRepository;
	private final UserRepository userRepository;
	private final FileService fileService;

	private UserEntity available(String username) {
		final UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		if (userEntity.isNotGrant()) {
			throw new NotGrantException();
		}
		return userEntity;
	}

	@Transactional
	public long addNotice(NoticeWrite noticeWrite, Long userId, String username, MultipartFile[] files) {
		NoticeEntity noticeEntity = noticeRepository.save(
				NoticeEntity.builder()
				.userId(userId)
				.username(username)
				.title(noticeWrite.getTitle())
				.content(noticeWrite.getContent())
				.build()
		);

		if (files != null) {
			fileService.addFiles(files, noticeEntity.getId());
		}
		return noticeEntity.getId();
	}

	@Transactional(readOnly = true)
	public Page<NoticeFind> getNotices(Integer page) {
		return noticeRepository.findAll(
				PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending())
		).map(NoticeFind::from);
	}

	@Transactional(readOnly = true)
	public NoticeDetail getNotice(Long id) {
		final NoticeEntity noticeEntity = noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
		return NoticeDetail.from(noticeEntity);
	}

	@Transactional(readOnly = true)
	public NoticeDetail getEditNotice(Long id, String username) {
		available(username);
		final NoticeEntity noticeEntity = noticeRepository.findById(id).orElseThrow();
		return NoticeDetail.from(noticeEntity);
	}

	@Transactional
	public void updateNotice(Long id, NoticeWrite noticeWrite, String username) {
		NoticeEntity noticeEntity = noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
		noticeEntity.changeNotice(noticeWrite);
	}

	@Transactional
	public void deleteNotice(Long id) {
		noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
		noticeRepository.deleteById(id);

	}
}
