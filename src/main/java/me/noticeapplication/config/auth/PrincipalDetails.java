package me.noticeapplication.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.persistence.user.entity.UserEntity;


@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

	// 일반 로그인
	private final UserEntity user;

	// OAuth 로그인
	private Map<String, Object> attributes;

	public PrincipalDetails(UserEntity user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	// 해당 User 의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new SimpleGrantedAuthority(user.getRole()));
		return collect;
	}

	public Long getId() {
		return user.getId();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
