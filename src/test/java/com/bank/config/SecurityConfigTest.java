package com.bank.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  // 1-4. 
import static org.assertj.core.api.Assertions.assertThat;  // 1-5.

// Tip 1 : 서버는 일관성있게 에러정보를 리턴해주어야 한다.
// Tip 2 : 내가 모르는 에러 정보가 프론트단으로 넘어가지 않게 내가 직접 다 제어하자.
@AutoConfigureMockMvc  // 1-2. Mock(가짜) 환경에 MockMvc를 등록해놓는다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 1-1. SpringBootTest를 MOCK환경으로 실행한다는 뜻.
public class SecurityConfigTest {
	
	@Autowired
	private MockMvc mvc;  // 1-3. 1-2에서 등록해놓은 MockMvc를 주입받는다.

	@Test
	void authentication_test() throws Exception {
		// given
		
		// when
		// 1-6. 
		ResultActions resultActions = mvc.perform(get("/api/s/hello"));
		
		// 1-7.
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
		
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + httpStatusCode);
		
		// then
		assertThat(httpStatusCode).isEqualTo(401);
	}
	
	@Test
	void authorization_test() throws Exception {
		// given
		
		// when
		ResultActions resultActions = mvc.perform(get("/api/admin/hello"));
		
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
		
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + httpStatusCode);
		
		// then
		assertThat(httpStatusCode).isEqualTo(403);
	}
}
