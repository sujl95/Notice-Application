package me.noticeapplication.user;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.error.exception.user.UserNotFoundException;
import me.noticeapplication.user.dto.UserCreate;
import me.noticeapplication.user.dto.UsernameCheckRequest;
import me.noticeapplication.controller.response.ResponseInfo;
import me.noticeapplication.error.exception.user.UserDuplicateUserNameException;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	@Transactional
	public long create(UserCreate create) {
		Optional<UserEntity> userEntityInfo = userRepository.findByUsername(create.getUsername());
		if (userEntityInfo.isPresent()) {
			throw new UserDuplicateUserNameException();
		}
		create.setPassword(bCryptPasswordEncoder.encode(create.getPassword()));
		final UserEntity userEntity = UserEntity.from(create);
		final UserEntity savedUserEntity = userRepository.save(userEntity);
		return savedUserEntity.getId();
	}

	@Transactional(readOnly = true)
	public ResponseInfo idCheck(UsernameCheckRequest usernameCheckRequest) {
		final UserEntity userEntity = userRepository.findByUsername(usernameCheckRequest.getUsername()).orElse(null);
		String response;
		if (ObjectUtils.isEmpty(userEntity)) {
			response = "사용 가능한 아이디입니다";
		} else {
			response = "사용 불가능한 아이디 입니다.";
		}
		return ResponseInfo.from(response);
	}

	@Transactional(readOnly = true)
	public UserEntity getUser(String username) {
		return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

}
