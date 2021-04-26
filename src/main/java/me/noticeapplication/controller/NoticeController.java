package me.noticeapplication.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noticeapplication.config.auth.PrincipalDetails;
import me.noticeapplication.file.FileService;
import me.noticeapplication.file.dto.FileDto;
import me.noticeapplication.notice.NoticeService;
import me.noticeapplication.notice.dto.NoticeDetail;
import me.noticeapplication.notice.dto.NoticeFind;
import me.noticeapplication.notice.dto.NoticeWrite;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.user.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;
	private final FileService fileService;
	private final UserService userService;

	private void loginStatus(Model model,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if (!ObjectUtils.isEmpty(principalDetails)) {
			final UserEntity user = userService.getUser(principalDetails.getUsername());
			model.addAttribute("role", user.getRole());
		}
	}

	@GetMapping("/notices")
	public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		final Page<NoticeFind> notices = noticeService.getNotices(page);
		loginStatus(model, principalDetails);
		model.addAttribute("notices", notices);
		return "noticeList";
	}

	@GetMapping("/notices/write")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String noticeWrite(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		loginStatus(model, principalDetails);
		return "noticeWrite";
	}

	@GetMapping("/notices/{id}")
	public String getNotice(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		final NoticeDetail noticeDetail = noticeService.getNotice(id);
		final List<FileDto> files = fileService.getFiles(noticeDetail.getId());
		loginStatus(model, principalDetails);
		model.addAttribute("notice", noticeDetail);
		model.addAttribute("files", files);
		return "noticeDetail";
	}

	@PostMapping("/notices")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String addNotice(@RequestParam("file") MultipartFile[] file, @Valid NoticeWrite noticeWrite,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if (ObjectUtils.isEmpty(principalDetails)) {
			return "redirect:/login-from";
		}
		final long noticeId = noticeService.addNotice(noticeWrite, principalDetails.getUsername());
		fileService.addFiles(file, noticeId);
		return "redirect:/notices";
	}

	@GetMapping("/notices/{id}/edit")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String edit(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		loginStatus(model, principalDetails);
		final NoticeDetail noticeDetail = noticeService.getEditNotice(id, principalDetails.getUsername());
		model.addAttribute("notice", noticeDetail);
		return "noticeModify";
	}

	@PutMapping("/notices/{id}")
	public String updateNotice(@PathVariable("id") Long id, NoticeWrite noticeWrite, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		noticeService.updateNotice(id, noticeWrite, principalDetails.getUsername());
		return "redirect:/notices";
	}

	@DeleteMapping("/notices/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteNotice(@PathVariable("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		noticeService.deleteNotice(id, principalDetails.getUsername());
		return "redirect:/notices";
	}

	@GetMapping("/download/{fileId}")
	@ResponseBody
	public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
		final FileDto fileDto = fileService.getFile(fileId);
		final Path path = Paths.get(fileDto.getFilePath());
		final Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream; charset=utf-8"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileDto.getOrigFilename().getBytes(
						StandardCharsets.UTF_8), "ISO8859_1") + "\"")
				.body(resource);
	}
}
