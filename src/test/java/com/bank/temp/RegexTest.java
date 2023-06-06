package com.bank.temp;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

// java.util.regex.Pattern
public class RegexTest {

	@Test
	public void 한글만된다_test() throws Exception {
		String value = "한글";
		
		// 1-1. [가-힣] : 한글 한자
		// 1-2. 공백 X, 특수문자 X, 숫자 X, 영어 X, 한글 허용 O 정규표현식 : ^[가-힣]+$
		boolean result = Pattern.matches("^[가-힣]+$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void 한글은안된다_test() throws Exception {
		
		String value = "1";
		
		// 1-3. 공백 O, 특수문자 O, 숫자 O, 영어 O, 한글 허용 X 정규표현식 : ^[^ㄱ-ㅎ가-힣]*$
		boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void 영어만된다_test() throws Exception {
		String value = "abc";
		
		// 1-4. 공백 X, 특수문자 X, 숫자 X, 한글 X, 영어 허용 O 정규표현식 : ^[a-zA-Z]+$
		boolean result = Pattern.matches("^[a-zA-Z]+$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void 영어는안된다_test() throws Exception {
		String value = "가22";
		
		// 1-5. 공백 O, 특수문자 O, 숫자 O, 한글 O, 영어만 허용 X 정규표현식 : ^[^a-zA-Z]*$
		boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void 영어와숫자만된다_test() throws Exception {
		String value = "ssar1234";
		
		// 1-6. 공백 X, 특수문자 X, 한글 X, 영어와 숫자만 허용 O 정규표현식 : ^[a-zA-Z0-9]+$
		boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void 영어만되고_길이는최소2최대4이다_test() throws Exception {
		String value = "";
		
		// 1-7. 공백 X, 특수문자 X, 숫자X, 한글 X, 영어만 되고 길이는 2~4까지 : ^[a-zA-Z]{2,4}$
		boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void user_username_test() throws Exception {
		String username = "ssar";
		
		// 1-8 : 영문과 숫자를 조합해서 2~20자 이내
		boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void user_fullname_test() throws Exception {
		String fullname = "ssar한";
		
		// 1-9. 영어, 한글, 길이는 최소 1~20
		boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
		
		System.out.println("테스트 : " + result);
	}
	
	@Test
	public void user_email_test() throws Exception {
		String username = "ssar2688@nate.com";
		
		// 1-10 : 
		boolean result = Pattern.matches("^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", username);
		
		System.out.println("테스트 : " + result);
	}
}
