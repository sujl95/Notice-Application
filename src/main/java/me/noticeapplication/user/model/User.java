package me.noticeapplication.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class User {

	private final int id;
	private final String userId;
	private final String password;
	private final String email;
	private final Role role;
	private final String provider;
	private final String providerId;

	@Getter
	@RequiredArgsConstructor
	public enum Role {
		ROLE_USER("사용자"), ROLE_ADMIN("관리자");

		private final String description;
	}

}
