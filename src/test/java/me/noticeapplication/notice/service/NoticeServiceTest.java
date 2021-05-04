package me.noticeapplication.notice.service;

import static me.noticeapplication.user.model.User.Role.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import me.noticeapplication.error.exception.notice.NoticeNotFoundException;
import me.noticeapplication.file.FileService;
import me.noticeapplication.notice.NoticeService;
import me.noticeapplication.notice.dto.NoticeDetail;
import me.noticeapplication.notice.dto.NoticeFind;
import me.noticeapplication.notice.dto.NoticeWrite;
import me.noticeapplication.persistence.file.repository.FileRepository;
import me.noticeapplication.persistence.notice.entity.NoticeEntity;
import me.noticeapplication.persistence.notice.repository.NoticeRepository;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;
import me.noticeapplication.user.UserService;
import me.noticeapplication.user.dto.UserCreate;


@Transactional
@DataJpaTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("공지 서비스 관련 테스트")
public class NoticeServiceTest {

	private NoticeService noticeService;
	private UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private FileService fileService;

	static UserEntity userEntity;
	static long fakeUserId = 2L;

	@Autowired
	private NoticeRepository noticeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileRepository fileRepository;

	@BeforeEach
	public void setUp() {
		//given
		userService = new UserService(userRepository, bCryptPasswordEncoder);

		UserCreate userCreate = UserCreate.builder()
				.username("admintest")
				.email("admintest@gmail.com")
				.password("admintest")
				.role(ROLE_ADMIN)
				.build();

		userEntity = UserEntity.from(userCreate);
		ReflectionTestUtils.setField(userEntity, "id", fakeUserId);
		noticeService = new NoticeService(noticeRepository, userRepository, fileService);
		fileService = new FileService(fileRepository);
	}

	@Test
	@Order(1)
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 저장 - 권한 관리자 - 성공")
	public void saveNoticeAuthAdminSuccessTest() {
		//given
		NoticeWrite noticeWrite = NoticeWrite.builder()
				.title("제목 테스트")
				.content("내용 테스트")
				.build();

		NoticeEntity noticeEntity = NoticeEntity.from(noticeWrite, userEntity);
		ReflectionTestUtils.setField(noticeEntity, "id", fakeUserId);

		//when
		long result = noticeService.addNotice(noticeWrite, 1L,"admintest", null);

		//then
		assertEquals(result, 2L);
	}

	@Test
	@Order(2)
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 조회 - 권한 관리자 - 성공")
	public void getNoticesAuthAdminSuccessTest() {
		//given
		NoticeWrite noticeWrite = NoticeWrite.builder()
				.title("제목 테스트")
				.content("내용 테스트")
				.build();
		noticeService.addNotice(noticeWrite, 1L,"admintest", null);

		NoticeWrite noticeWrite1 = NoticeWrite.builder()
				.title("제목 테스트1")
				.content("내용 테스트1")
				.build();
		noticeService.addNotice(noticeWrite1, 1L,"admintest", null);

		//when
		Page<NoticeFind> notices = noticeService.getNotices(1);

		//then
		assertEquals(notices.getContent().get(1).getTitle(), "제목 테스트");
		assertEquals(notices.getContent().get(1).getContent(), "내용 테스트");
	}

	@Test
	@Order(3)
	@DisplayName("공지 상세 조회 - 권한 X - 성공")
	public void getNoticeNoAuthSuccess() {

		NoticeDetail noticeDetail = noticeService.getNotice(1L);

		assertEquals(noticeDetail.getTitle(), "제목1");
		assertEquals(noticeDetail.getContent(), "내용1");
	}

	@Test
	@Order(4)
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 수정 - 권한 관리자 - 성공")
	public void updateNoticeAuthAdminSuccess() {
		//given
		NoticeWrite noticeWrite = NoticeWrite.builder()
				.title("제목 수정")
				.content("내용 수정")
				.build();

		//when
		noticeService.updateNotice(1L, noticeWrite, "admintest");

		//then
		NoticeDetail noticeDetail = noticeService.getNotice(1L);
		assertEquals(noticeDetail.getTitle(), "제목 수정");
		assertEquals(noticeDetail.getContent(), "내용 수정");
	}

	@Test
	@Order(5)
	@WithMockUser(roles = "ADMIN")
	@DisplayName("공지 삭제 - 권한 관리자 - 성공")
	public void deleteNoticeAuthAdminSuccess() {
		//given
		noticeService.deleteNotice(1L);

		try {
			//when
			noticeService.getNotice(1L);
		} catch (NoticeNotFoundException exception) {
			//then
			assertEquals(exception.getMessage(), "공지글을 찾을 수 없습니다.");
		}
	}
}
