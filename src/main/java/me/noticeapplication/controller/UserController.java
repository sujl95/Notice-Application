package me.noticeapplication.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.user.UserService;
import me.noticeapplication.user.dto.UserCreate;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserService userService;

	@PostMapping("/join")
	public String join(UserCreate user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userService.create(user);
		return "redirect:/login-form";
	}

}
