package me.noticeapplication.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noticeapplication.config.auth.PrincipalDetails;
import me.noticeapplication.error.exception.common.UserNotAuthException;
import me.noticeapplication.file.FileService;
import me.noticeapplication.notice.NoticeService;
import me.noticeapplication.notice.dto.NoticeDetail;
import me.noticeapplication.notice.dto.NoticeFind;
import me.noticeapplication.notice.dto.NoticeWrite;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeRestController {

	private final NoticeService noticeService;
	private final FileService fileService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public long addNotice(@RequestParam(value = "file", required = false) MultipartFile[] file, @Valid NoticeWrite noticeWrite,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		long noticeId = noticeService.addNotice(noticeWrite, principalDetails.getId(), principalDetails.getUsername(), file);
		if (!ObjectUtils.isEmpty(file)) {
			fileService.addFiles(file, noticeId);
		}
		return noticeId;
	}

	@GetMapping
	public Page<NoticeFind> getNotices(@RequestParam(defaultValue = "1") Integer page) {
		return noticeService.getNotices(page);
	}

	@GetMapping("/{id}")
	public NoticeDetail getNotice(@PathVariable("id") Long id) {
		return noticeService.getNotice(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateNotice(@PathVariable("id") Long id, @RequestBody NoticeWrite noticeWrite,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		noticeService.updateNotice(id, noticeWrite, principalDetails.getUsername());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNotice(@PathVariable("id") Long id) {
		noticeService.deleteNotice(id);
	}

}
