package me.noticeapplication.notice.web;

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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.noticeapplication.config.auth.PrincipalDetails;
import me.noticeapplication.notice.dto.NoticeWrite;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.user.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@Transactional
@DisplayName("공지사항 API 관련 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeRestControllerIntegrationTest {

	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext ctx;

	NoticeWrite noticeWrite;
	NoticeWrite badNoticeWrite;
	NoticeWrite updateNoticeWrite;

	UserEntity adminEntity;

	UserEntity userEntity;

	String url = "/api/notices";

	MockMultipartFile mockMultipartFile;

	PrincipalDetails adminPrincipalDetails;

	PrincipalDetails userPrincipalDetails;


	@BeforeEach
	void setUp() throws Exception {
		noticeWrite = NoticeWrite.builder()
				.title("제목 테스트")
				.content("내용 테스트")
				.build();

		badNoticeWrite = NoticeWrite.builder()
				.title("")
				.content("")
				.build();

		updateNoticeWrite = NoticeWrite.builder()
				.title("제목 수정")
				.content("내용 수정")
				.build();

		mockMvc = MockMvcBuilders
				.webAppContextSetup(ctx)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.apply(springSecurity())
				.alwaysDo(print())
				.build();

		adminEntity = UserEntity.builder()
				.providerId(null)
				.provider(null)
				.role(User.Role.ROLE_ADMIN)
				.username("admintest")
				.email("admintest@gmail.com")
				.password("admintest")
				.regDate(LocalDateTime.now())
				.build();

		userEntity = UserEntity.builder()
				.providerId(null)
				.provider(null)
				.role(User.Role.ROLE_USER)
				.username("usertest")
				.email("usertest@gmail.com")
				.password("usertest")
				.regDate(LocalDateTime.now())
				.build();

		adminPrincipalDetails = new PrincipalDetails(adminEntity);

		userPrincipalDetails = new PrincipalDetails(userEntity);


		// 파일
		mockMultipartFile = new MockMultipartFile("file", "filename.txt", MediaType.TEXT_PLAIN_VALUE,
				"filecontents".getBytes());

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 파일 저장 - 권한 관리자 - 성공")
	void noticeFileSaveAuthAdminSuccessTest() throws Exception{
		mockMvc.perform(post(url)
				.param("title", noticeWrite.getTitle())
				.param("content", noticeWrite.getContent())
				.param("file", Arrays.toString(mockMultipartFile.getBytes()))
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 파일 저장 - 권한 유저 - 실패")
	void noticeFileSaveAuthUserFailureTest() throws Exception{
		mockMvc.perform(post(url)
				.param("title", noticeWrite.getTitle())
				.param("content", noticeWrite.getContent())
				.param("file", Arrays.toString(mockMultipartFile.getBytes()))
				.with(user(userPrincipalDetails)))
				.andExpect(status().isForbidden());
	}

	@Test
	@Order(2)
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 저장 - 권한 관리자 - 성공")
	void noticeSaveAuthAdminSuccessTest() throws Exception{
		mockMvc.perform(post(url)
				.param("title", noticeWrite.getTitle())
				.param("content", noticeWrite.getContent())
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 저장 - 권한 유저 - 실패")
	void noticeSaveAuthUserFailureTest() throws Exception{
		mockMvc.perform(post(url)
				.param("title", noticeWrite.getTitle())
				.param("content", noticeWrite.getContent())
				.with(user(userPrincipalDetails)))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 저장 - 권한 관리자 - 제목 내용 미입력 - 실패")
	void noticeSaveAuthAdminEmptyTitleAndContentFailureTest() throws Exception{
		mockMvc.perform(post(url)
				.param("title", "")
				.param("content", "")
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 조회 - 권한 유저 - 성공")
	void getNoticeAuthUserSuccessTest() throws Exception {
		mockMvc.perform(get(url)
				.with(user(userPrincipalDetails)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 조회 - 권한 관리자 - 성공")
	void getNoticeAuthAdminSuccessTest() throws Exception {
		mockMvc.perform(get(url)
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("공지 조회 - 권한 X - 성공")
	void getNoticeSuccessTest() throws Exception {
		mockMvc.perform(get(url))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 상세 조회 - 권한 유저 - 성공")
	void getDetailNoticeAuthUserSuccessTest() throws Exception {
		mockMvc.perform(get(url + "/{id}", 1)
				.with(user(userPrincipalDetails)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 상세 조회 - 권한 관리자 - 성공")
	void getDetailNoticeAuthAdminSuccessTest() throws Exception {
		mockMvc.perform(get(url + "/{id}", 1)
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("공지 상세 조회 - 권한 X - 성공")
	void getDetailNoticeSuccessTest() throws Exception {
		mockMvc.perform(get(url + "/{id}", 1))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("공지 상세 조회 - 권한 X - 없는 글 - 실패")
	void getDetailNoticeNotFoundFailureTest() throws Exception {
		mockMvc.perform(get(url + "/{id}", -1))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 수정 - 권한 관리자 - 성공")
	void updateNoticeAuthAdminSuccessTest() throws Exception {
		mockMvc.perform(put(url + "/{id}", 1)
				.content(objectMapper.writeValueAsString(updateNoticeWrite))
				.contentType(MediaType.APPLICATION_JSON)
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 수정 - 권한 유저 - 실패")
	void updateNoticeAuthUserFailureTest() throws Exception {
		mockMvc.perform(put(url + "/{id}", 1)
				.content(objectMapper.writeValueAsString(updateNoticeWrite))
				.contentType(MediaType.APPLICATION_JSON)
				.with(user(userPrincipalDetails)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("공지 수정 - 권한 X - 실패")
	void updateNoticeNoAuthFailureTest() throws Exception {
		mockMvc.perform(put(url + "/{id}", 1)
				.content(objectMapper.writeValueAsString(updateNoticeWrite))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 삭제 - 권한 관리자 - 성공")
	void deleteNoticeAuthAdminSuccessTest() throws Exception {
		mockMvc.perform(delete(url + "/{id}", 1)
				.with(user(adminPrincipalDetails)))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("공지 삭제 - 권한 유저 - 실패")
	void deleteNoticeAuthUserFailureTest() throws Exception {
		mockMvc.perform(delete(url + "/{id}", 1)
				.with(user(userPrincipalDetails)))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("공지 삭제 - 권한 X - 실패")
	void deleteNoticeNoAuthFailureTest() throws Exception {
		mockMvc.perform(delete(url + "/{id}", 1))
				.andExpect(status().is3xxRedirection());
	}

}
