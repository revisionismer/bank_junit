package com.bank.web.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bank.config.dummy.DummyObject;
import com.bank.domain.user.UserRepository;
import com.bank.dto.user.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

// 주의 : dev 환경에서만 테스트가 잘 동작한다.
@AutoConfigureMockMvc  // 1-2. Mock(가짜) 환경에 MockMvc를 등록해놓는다.
// @SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. SpringBootTest를 MOCK환경으로 실행한다는 뜻.
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class) // 1-5. @Order 어노테이션 대로 실행해주게 하는 설정.
public class UserApiControllerTest extends DummyObject { 

	@Autowired
	private MockMvc mvc;  // 1-3. 1-2에서 등록해놓은 MockMvc를 주입받는다.
	
	@Autowired
	private ObjectMapper om; // 1-4. ObjectMapper 주입
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Order(1)
	public void join_success_test() throws Exception {
		// given
		JoinReqDto joinReqDto = new JoinReqDto();
		joinReqDto.setUsername("love");
		joinReqDto.setPassword("1234");
		joinReqDto.setEmail("love@nate.com");
		joinReqDto.setFullname("박종희");
		
		String requestBody = om.writeValueAsString(joinReqDto);
		System.out.println("테스트 : " + requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/user/join")
												.content(requestBody)
												.contentType(MediaType.APPLICATION_JSON));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
	
		System.out.println(httpStatusCode);
		
		resultActions.andExpect(status().isCreated());
	}
	
	@Test
	@Order(2)
	public void join_fail_test() throws Exception {
		// given
		data_setting();
		
		JoinReqDto joinReqDto = new JoinReqDto();
		joinReqDto.setUsername("ssar");
		joinReqDto.setPassword("1234");
		joinReqDto.setEmail("ssar@nate.com");
		joinReqDto.setFullname("쌀");
		
		String requestBody = om.writeValueAsString(joinReqDto);
		System.out.println("테스트 : " + requestBody);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/user/join")
												.content(requestBody)
												.contentType(MediaType.APPLICATION_JSON));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		System.out.println("테스트 : " + responseBody);
		
		// then
		int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
	
		System.out.println(httpStatusCode);
		
		resultActions.andExpect(status().isBadRequest());
	}
	
	private void data_setting() {
		userRepository.save(newUser("ssar", "1234", "ssar@nate.com", "쌀"));
	}
}
