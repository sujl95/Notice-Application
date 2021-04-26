package me.noticeapplication.persistence.user.entity;

import static me.noticeapplication.user.model.User.Role.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.noticeapplication.user.dto.UserCreate;
import me.noticeapplication.user.model.User;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private User.Role role;

	@Column(name = "provider")
	private String provider;

	@Column(name = "provider_id")
	private String providerId;

	@CreationTimestamp
	@Column(name = "reg_date", updatable = false)
	private LocalDateTime regDate;

	@Builder
	public UserEntity(String username, String password, String email, User.Role role, String provider,
			String providerId, LocalDateTime regDate) {
		this.username 	= username;
		this.password 	= password;
		this.email 		= email;
		this.role 		= role;
		this.provider 	= provider;
		this.providerId = providerId;
		this.regDate 	= regDate;
	}

	public static UserEntity from(UserCreate create) {
		return UserEntity.builder()
				.username(create.getUsername())
				.password(create.getPassword())
				.email(create.getEmail())
				.role(create.getRole())
				.provider(null)
				.providerId(null)
				.build();
	}

	public String getRole() {
		return role.toString();
	}

	public boolean isGrant() {
		return role != ROLE_ADMIN;
	}
}
