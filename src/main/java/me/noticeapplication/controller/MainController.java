package me.noticeapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

	@GetMapping("/login-form")
	public String login() {
		return "loginForm";
	}

	@GetMapping("/join-form")
	public String joinForm() {
		return "joinForm";
	}

	@GetMapping({"/",""})
	public String main() {
		return "redirect:/notices";
	}

}
