package me.noticeapplication.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.user.dto.UserCreate;
import me.noticeapplication.user.dto.UsernameCheckRequest;
import me.noticeapplication.controller.response.ResponseInfo;
import me.noticeapplication.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public long create(@RequestBody @Valid UserCreate create) {
		return userService.create(create);
	}

	@PostMapping("/id-check")
	public ResponseInfo idCheck(@RequestBody @Valid UsernameCheckRequest usernameCheckRequest) {
		return userService.idCheck(usernameCheckRequest);
	}

}
