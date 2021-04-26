package me.noticeapplication.user.service;

import static me.noticeapplication.user.model.User.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import me.noticeapplication.controller.response.ResponseInfo;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;
import me.noticeapplication.user.UserService;
import me.noticeapplication.user.dto.UserCreate;
import me.noticeapplication.user.dto.UsernameCheckRequest;
import me.noticeapplication.user.model.User;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("유저 서비스 관련 테스트")
public class UserServiceTest {

	private UserService userService;
	static UserEntity userEntity;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeEach
	void setUp() {
		userService = new UserService(userRepository, bCryptPasswordEncoder);
	}

	@Test
	@Order(0)
	@DisplayName("유저 저장 - 성공")
	public void saveUserSuccessTest() {
		//given
		long fakeUserId = 1L;
		String username = "admintest";
		String email = "admintest@gmail.com";
		String password = "admintest";
		User.Role role = ROLE_ADMIN;
		UserCreate userCreate = UserCreate.builder()
				.username(username)
				.email(email)
				.password(password)
				.role(role)
				.build();

		userEntity = UserEntity.from(userCreate);
		ReflectionTestUtils.setField(userEntity, "id", fakeUserId);

		given(userRepository.save(any())).willReturn(userEntity);
		given(userRepository.findById(Math.toIntExact(fakeUserId))).willReturn(Optional.ofNullable(userEntity));

		//when
		userService.create(userCreate);

		//then
		UserEntity savedUserEntity = userRepository.findById((int)fakeUserId).get();

		assertEquals(userEntity.getId(), savedUserEntity.getId());
		assertEquals(userEntity.getUsername(), savedUserEntity.getUsername());
		assertEquals(userEntity.getEmail(), savedUserEntity.getEmail());
		assertEquals(userEntity.getRole(), savedUserEntity.getRole());
	}

	@Test
	@Order(1)
	@DisplayName("유저 중복 체크 - 성공")
	public void userIdCheckSuccessTest() {
		//given
		String username = userEntity.getUsername();
		UsernameCheckRequest usernameCheckRequest = UsernameCheckRequest.builder()
				.username(username)
				.build();
		given(userRepository.findByUsername(any())).willReturn(Optional.ofNullable(userEntity));

		//when
		ResponseInfo responseInfo = userService.idCheck(usernameCheckRequest);

		//then
		assertEquals(responseInfo.getResponse(), "사용 불가능한 아이디 입니다.");
	}

}
