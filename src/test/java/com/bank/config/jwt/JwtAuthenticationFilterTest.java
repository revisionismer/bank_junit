package com.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.dummy.DummyObject;
import com.bank.config.jwt.dto.SignInDto;
import com.bank.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional  // 1-6. 테스트 클래스에 트랜잭션을 걸어주면 테스트 하나 실행 후 롤백을 해버린다. beforeEach가 두번 실행되도 기존 데이터는 날라가서 정상으로 동작한다는 말.
@ActiveProfiles("dev")  // 1-5. dev로 실행
@AutoConfigureMockMvc // 1-4. @AutoConfigurationMockMvc를 붙여줘야  한다. 
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. dev로 셋팅후 시작
public class JwtAuthenticationFilterTest extends DummyObject {

	@Autowired
	private ObjectMapper om;  // 1-2. Mock 환경에서 주입 받는다.
	
	@Autowired
	private MockMvc mvc;  // 1-3. MockMvc를 주입 받기 위해선
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void setUp() throws Exception {
		userRepository.save(newUser("ssar", "1234", "ssar@nate.com", "쌀"));
	}
	
	@Test
	public void successfulAuthentication_test() throws Exception {
		// given
		SignInDto signInDto = new SignInDto();
		signInDto.setUsername("ssar");
		signInDto.setPassword("1234");
		
		String requestBody = om.writeValueAsString(signInDto);
		
		System.out.println("테스트 : " + requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/users/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		String access_token = resultActions.andReturn().getResponse().getHeader(JwtProperties.HEADER_STRING);
		
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + access_token);
		
		// then
		resultActions.andExpect(status().isOk());
		assertNotNull(access_token);
		assertTrue(access_token.startsWith(JwtProperties.TOKEN_PREFIX));
		resultActions.andExpect(jsonPath("$.username").value("ssar"));
		
	}
	
	@Test
	public void unsuccessfulAuthentication_test() throws Exception {
		// given
		SignInDto signInDto = new SignInDto();
		signInDto.setUsername("ssar");
		signInDto.setPassword("12345");
		
		String requestBody = om.writeValueAsString(signInDto);
		
		System.out.println("테스트 : " + requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/users/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		String access_token = resultActions.andReturn().getResponse().getHeader(JwtProperties.HEADER_STRING);
		
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + access_token);
		
		// then
		resultActions.andExpect(status().isUnauthorized());
	}
}
