package com.bank.data;

import org.junit.jupiter.api.Test;

public class LongTest {

	@Test
	public void long_test1() throws Exception {
		// given
		Long number1 = 1111L;
		Long number2 = 2222L;
		
		Long amount1 = 100L;
		Long amount2 = 1000L;
		
		// when
		if(number1.longValue() == number2.longValue()) {
			System.out.println("테스트 : 동일합니다.");
		} else {
			System.out.println("테스트 : 동일하지 않습니다.");
		}
		
		if(amount1 != amount2) {
			System.out.println("테스트 : 다릅니다.");
		} else {
			System.out.println("테스트 : 같습니다");
		}
		
		// then
	}

	@Test
	public void long_test2() throws Exception {
		// given
		Long v1 = 100L;
		Long v2 = 200L;
		
		Long v3 = 100L;
		Long v4 = 100L;
		
		Long v5 = 1000L;
		Long v6 = 1000L;
		
		// when
		if(v1 < v2) {
			System.out.println("테스트 : v1이 v2보다 작습니다.");
		} else {
			System.out.println("테스트 : v1이 v2보다 큽니다.");
		}
		
		// 1-1. Long value가 값이 작다면 ==으로 비교가 가능.
		if(v3 == v4) {
			System.out.println("테스트 : v3랑 v4가 같습니다.");
		} else {
			System.out.println("테스트 : v3랑 v4가 다릅니다.");
		}
		
		// 1-2. Long value 값이 1000L을 넘어가면 ==으로 비교 불가능 -> 8비트 까지 비교 가능 
		if(v5 == v6) {
			System.out.println("테스트 : v5랑 v6가 같습니다.");
		} else {
			System.out.println("테스트 : v5랑 v6가 다릅니다.");
		}
		
		// 1-3. 1000L 이상은 longValue를 붙여서 비교해야한다
		if(v5.longValue() == v6.longValue()) {
			System.out.println("테스트 : v5랑 v6가 같습니다.");
		} else {
			System.out.println("테스트 : v5랑 v6가 다릅니다.");
		}
		
		// then
	}
}
