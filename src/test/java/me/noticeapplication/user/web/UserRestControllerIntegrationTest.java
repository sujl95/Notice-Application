package me.noticeapplication.user.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.noticeapplication.config.auth.PrincipalDetails;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.user.dto.UserCreate;
import me.noticeapplication.user.dto.UsernameCheckRequest;
import me.noticeapplication.user.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@Transactional
@DisplayName("User API 관련 테스트")
public class UserRestControllerIntegrationTest {

	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext ctx;

	UserEntity adminEntity;

	UserEntity userEntity;

	String url = "/api/users";

	PrincipalDetails adminPrincipalDetails;

	PrincipalDetails userPrincipalDetails;


	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(ctx)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
				.apply(springSecurity())
				.alwaysDo(print())
				.build();

		adminEntity = UserEntity.builder()
				.providerId(null)
				.provider(null)
				.role(User.Role.ROLE_ADMIN)
				.username("admintest1")
				.email("admintest1@gmail.com")
				.password("admintest1")
				.regDate(LocalDateTime.now())
				.build();

		userEntity = UserEntity.builder()
				.providerId(null)
				.provider(null)
				.role(User.Role.ROLE_USER)
				.username("usertest1")
				.email("usertest1@gmail.com")
				.password("usertest1")
				.regDate(LocalDateTime.now())
				.build();

		adminPrincipalDetails = new PrincipalDetails(adminEntity);

		userPrincipalDetails = new PrincipalDetails(userEntity);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("유저 저장 - 권한 관리자 - 성공")
	void userSaveAuthAdminSuccessTest() throws Exception{
		UserCreate userCreate = UserCreate.builder()
				.username("createtest")
				.email("createtest@gmail.com")
				.password("createtest")
				.role(User.Role.ROLE_USER)
				.build();

		mockMvc.perform(post(url)
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON)
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("유저 저장 - 권한 유저 - 성공")
	void userSaveAuthUserSuccessTest() throws Exception{
		UserCreate userCreate = UserCreate.builder()
				.username("createtest")
				.email("createtest@gmail.com")
				.password("createtest")
				.role(User.Role.ROLE_USER)
				.build();

		mockMvc.perform(post(url)
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON)
				.with(user(userPrincipalDetails)))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("유저 저장 - 권한 X - 성공")
	void userSaveNoAuthSuccessTest() throws Exception{
		UserCreate userCreate = UserCreate.builder()
				.username("createtest")
				.email("createtest@gmail.com")
				.password("createtest")
				.role(User.Role.ROLE_USER)
				.build();

		mockMvc.perform(post(url)
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("이미 존재하는 유저 저장 - 권한 X - 실패")
	void userSaveNoAuthAlreadyUserFailureTest() throws Exception{
		UserCreate userCreate = UserCreate.builder()
				.username("usertest")
				.email("usertest@gmail.com")
				.password("usertest")
				.role(User.Role.ROLE_USER)
				.build();

		mockMvc.perform(post(url)
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("중복 유저 없을때 체크 - 권한 X - 성공")
	void useridCheckNoAuthSuccessTest() throws Exception{
		UsernameCheckRequest userCreate = UsernameCheckRequest.builder()
				.username("usertest")
				.build();

		mockMvc.perform(post(url+"/id-check")
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("중복 유저 있을때 체크 - 권한 X - 실패")
	void useridCheckNoAuthAlreadyUserFailureTest() throws Exception{
		UsernameCheckRequest userCreate = UsernameCheckRequest.builder()
				.username("usertest")
				.build();

		mockMvc.perform(post(url+"/id-check")
				.content(objectMapper.writeValueAsString(userCreate))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}


}
