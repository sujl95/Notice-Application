package me.noticeapplication.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(null);
		if (ObjectUtils.isEmpty(userEntity)) {
			return null;
		}
		return new PrincipalDetails(userEntity);
	}
}
