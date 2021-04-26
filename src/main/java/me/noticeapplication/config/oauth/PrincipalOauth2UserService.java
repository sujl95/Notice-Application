package me.noticeapplication.config.oauth;

import static me.noticeapplication.user.model.User.Role.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noticeapplication.config.auth.PrincipalDetails;
import me.noticeapplication.config.oauth.provider.FaceBookInfo;
import me.noticeapplication.config.oauth.provider.GoogleUserInfo;
import me.noticeapplication.config.oauth.provider.KakaoInfo;
import me.noticeapplication.config.oauth.provider.NaverInfo;
import me.noticeapplication.config.oauth.provider.OAuth2UserInfo;
import me.noticeapplication.persistence.user.entity.UserEntity;
import me.noticeapplication.persistence.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = null;
		if ("google".equals(userRequest.getClientRegistration().getRegistrationId())) {
			log.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if ("facebook".equals(userRequest.getClientRegistration().getRegistrationId())) {
			log.info("페이스북 로그인 요청");
			oAuth2UserInfo = new FaceBookInfo(oAuth2User.getAttributes());
		} else if ("naver".equals(userRequest.getClientRegistration().getRegistrationId())) {
			log.info("네이버 로그인 요청");
			oAuth2UserInfo = new NaverInfo((Map) oAuth2User.getAttributes().get("response"));
		} else if ("kakao".equals(userRequest.getClientRegistration().getRegistrationId())) {
			log.info("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoInfo(oAuth2User.getAttributes());
		} else {
			log.info("구글, 페이스북, 네이버, 카카오 로그인을 하지 않았을 때");
		}

		String provider = oAuth2UserInfo.getProvider();
		String providerId =  oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode(username); // 기본 비밀번호 설정
		String email =  oAuth2UserInfo.getEmail();

		Optional<UserEntity> userEntity = userRepository.findByUsername(username);

		if (ObjectUtils.isEmpty(userEntity)) {
			userEntity = Optional.ofNullable(UserEntity.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(ROLE_USER)
					.provider(provider)
					.providerId(providerId)
					.build());
			userRepository.save(userEntity.get());
		}
		return new PrincipalDetails(userEntity.get(), oAuth2User.getAttributes()); // Authentication 객체에 들어간다
	}

}
