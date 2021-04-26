package me.noticeapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;
import me.noticeapplication.config.oauth.PrincipalOauth2UserService;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final PrincipalOauth2UserService principalOauth2UserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.ignoringAntMatchers("/h2-console/**")
				.ignoringAntMatchers("/notices/{id}")
				.ignoringAntMatchers("/users")
				.disable();
		http.httpBasic().disable()
				.authorizeRequests()
				.antMatchers("/notices/write", "/notices/{id}/edit").access("hasRole('ROLE_ADMIN')")
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().permitAll() // 다른 요청들 모든 사용자가 접근 가능
				.and()
			.formLogin()
				.loginPage("/login-form")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/notices")
				.permitAll()
				.and()
			.oauth2Login()
				.loginPage("/login-form")
				.userInfoEndpoint()
				.userService(principalOauth2UserService)
				.and()
				.defaultSuccessUrl("/notices");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}

}
