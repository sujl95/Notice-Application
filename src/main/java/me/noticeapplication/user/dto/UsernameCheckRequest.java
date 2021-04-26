package me.noticeapplication.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsernameCheckRequest {

	@NotBlank
	@Size(min = 4, max = 20)
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,20}$")
	private String username;
}
