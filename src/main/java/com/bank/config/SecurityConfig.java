package com.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bank.constant.user.UserEnum;

import lombok.RequiredArgsConstructor;

@Configuration  // 주의 : SecurityConfig를 테스트할땐 @Configuration 어노테이션 주석
@EnableWebSecurity  // 1-1. 시큐리티 활성화 -> 기본 스프링 필터체인에 등록, 주의 : SecurityConfig를 테스트할땐 @EnableWebSecurity 주석
@RequiredArgsConstructor
public class SecurityConfig {
	
	// 1-2. @Slf4j 어노테이션으로 사용해도 된다.
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	// 1-3. 비밀번호 해시
	@Bean 
	public BCryptPasswordEncoder encode() throws Exception {
		log.info("BCryptPasswordEncoder 빈 등록 완료.");
		return new BCryptPasswordEncoder();
	}
	
	// 1-4. 기존 SecurityConfig에서 configure 메소드 기능을 한다.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("filterChain 빈 등록 완료");
		return http
				.headers().frameOptions().disable()  // 1-5. http iframe 허용 X
				.and()
				.csrf().disable()  // 1-6. csrf 비활성화
				.cors().configurationSource(configurationSource())  // 1-7. custom cors 설정 등록
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 1-8. jSessionId를 서버에서 관리하지 않는다는 뜻으로, 세션 응답이 종료되면 사라진다는 뜻.(무상태성 설정) : jwt인증을 구현할거기 때문에 추가
				.and()
				.formLogin().disable()  // 1-9. 폼 로그인 방식을 사용하지 않는다고 선언
				.httpBasic().disable()  // 1-10. httpSecurity가 제공하는 기본인증 기능 disable
				.authorizeRequests()  // 1-11. 인증 Request를 정의
				.antMatchers("/api/**").authenticated()  // 1-12. /api/** 형태로 들어오는 url은 인증이 필요하다.
				.antMatchers("/api/admin/**").hasRole(""  + UserEnum.ADMIN)  // 1-13. /api/admin/**을 호출하기 위해선 설정된 Role이 필요하다.
				.anyRequest()  // 1-14. 1-12, 1-13가 아닌 요청은
				.permitAll()  // 1-15. 모두 허용
				.and()
				.build();
	}	
		
	public CorsConfigurationSource configurationSource() {  // 2-1. CorsConfigurationSource로 cors 설정 -> 기존엔 filter로 만들어서 등록했었는데 이번엔 CorsConfigurationSource 객체를 이용해 등록.
		log.info("CorsConfigurationSource cors 설정 생성 후 SecurityFilterChain에 등록 완료");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");  // 2-2. 모든 header 응답 허용
		configuration.addAllowedMethod("*");  // 2-3. GET, POST, PUT, DELETE 허용
		configuration.addAllowedOriginPattern("*");  // 2-4. 모든 IP 주소 허용
		configuration.setAllowCredentials(true);  // 2-5. 클라이언트쪽에서 쿠키 요청하는걸 허용
			
		// 2-6. UrlBasedCorsConfigurationSource 객체 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);  // 2-7. 모든 주소 요청시 CorsConfiguration 설정을 적용.
			
		return source;
	}
	
	// 2023-05-30 -> SecurityConfig 틀잡기 성공.

/*
	--- 이거 넣으면 시큐리티 테스트때 에러터짐 --- => 나중에 넣자.
	private AuthenticationConfiguration configuration; 
			
	// WebSecurityConfigurerAdapter를 상속해서 AuthenticationManager를 bean으로 등록했던걸 직접 등록(이걸 등록하면 비밀번호 입력하라는게 콘솔에 안뜸) -> JWT 구현시 주석 푼다.
//	@Bean  // 주의 : Security Test할땐 주석하고 진행해야한다. 이게 Bean으로 등록되어 있으면 Test가 제대로 동작 안한다.
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		log.info("AuthenticationManager 빈 등록 완료.");
		this.configuration = authenticationConfiguration; 
		return configuration.getAuthenticationManager();
	}
*/
}

