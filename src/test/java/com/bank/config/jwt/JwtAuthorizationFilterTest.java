package com.bank.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.auth.PrincipalDetails;
import com.bank.constant.user.UserEnum;
import com.bank.domain.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// 주의 : 현재 실제 DB에서 테스트만 정상적으로 동작하기 때문에 실제 웹에서 한번 로그인을 해서 refresh_token값을 갱신해주고 시작해야 정상적으로 동작한다.
@Transactional // 1-6. 테스트환경에서는 트랜잭션이 끝나면 롤백
@ActiveProfiles("prod")  // 1-5. 여기선 실제 DB 환경에서만 테스트가 정상적으로 동작하기 때문에 prod
@AutoConfigureMockMvc // 1-4. @AutoConfigurationMockMvc를 붙여줘야  한다. 
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. 
public class JwtAuthorizationFilterTest {
	
	@Autowired
	private MockMvc mvc;  // 1-3. MockMvc를 주입 받기 위해선

	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();	
	
//	@Test
	public void authorization_success_test() throws Exception {
		// given
		User user = User.builder().id(1L)
								.username("ssar")
								.password("1234")
								.role(UserEnum.CUSTOMER).build();
		
		PrincipalDetails loginUser = new PrincipalDetails(user);
		
		// ====================== 토큰 만들기 ====================================
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
	
		Map<String, Object> claims = new HashMap<>();;
		claims.put("username", loginUser.getUser().getUsername());
		
		Long expiredTime = 1000 * 60L * 60L * 1;
//		Long expiredTime = 8 * 60L * 60L * 1;
		
		Date date = new Date();
		date.setTime(date.getTime() + expiredTime);
		
		System.out.println("access_token 만료일자 : " + date);
	
		Key key = Keys.hmacShaKeyFor(secretKeyBytes);
	
		String access_token = Jwts.builder()
				.setHeader(headers) 
				.setClaims(claims)
				.setSubject("access_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		// ====================== 토큰 만들기 ====================================
		
		System.out.println("내꺼야 : " + access_token);
		
		// when  
		ResultActions resultActions = mvc.perform(get("/api/users/s/1").header(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token));
		
		// then => 참고 : Mock 환경이니 해당 url이 가상으로 존재한다고 생각하고 보내기 때문에 해당 api가 존재하지 않아도 header가 정상적이면 200이뜨는게 맞다.
//		resultActions.andExpect(status().isOk()); // dev
		resultActions.andExpect(status().isNotFound());  // prod
	}
	
//	@Test
	public void authorization_admin_test() throws Exception {
		// given
		User user = User.builder().id(1L)
								.username("ssar")
								.password("1234")
								.role(UserEnum.CUSTOMER).build();
		
		PrincipalDetails loginUser = new PrincipalDetails(user);
		
		// ====================== 토큰 만들기 ====================================
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
	
		Map<String, Object> claims = new HashMap<>();;
		claims.put("username", loginUser.getUser().getUsername());
		
		Long expiredTime = 1000 * 60L * 60L * 1;
//		Long expiredTime = 8 * 60L * 60L * 1;
		
		Date date = new Date();
		date.setTime(date.getTime() + expiredTime);
		
		System.out.println("access_token 만료일자 : " + date);
	
		Key key = Keys.hmacShaKeyFor(secretKeyBytes);
	
		String access_token = Jwts.builder()
				.setHeader(headers) 
				.setClaims(claims)
				.setSubject("access_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		// ====================== 토큰 만들기 ====================================
		
		System.out.println("내꺼야 : " + access_token);
		
		// when  
		ResultActions resultActions = mvc.perform(get("/api/admin/1").header(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token));
		
		// then => 참고 : Mock 환경이니 해당 url이 가상으로 존재한다고 생각하고 보내기 때문에 해당 api가 존재하지 않아도 header가 정상적이면 200이뜨는게 맞다.
//		resultActions.andExpect(status().isOk()); // dev
		resultActions.andExpect(status().isForbidden());  // prod
	}
	
//	@Test
	public void authorization_fail_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/hello/test"));

        // then
        resultActions.andExpect(status().isUnauthorized()); // 401
	}
}
