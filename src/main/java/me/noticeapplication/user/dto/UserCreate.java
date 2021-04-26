package me.noticeapplication.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.noticeapplication.user.model.User;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCreate {

	@NotBlank
	@Size(min = 4, max = 20)
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,20}$")
	private String username;

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	@Email
	private String email;

	private User.Role role;

	@Builder
	public UserCreate(String username, String password, String email, User.Role role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}
}
