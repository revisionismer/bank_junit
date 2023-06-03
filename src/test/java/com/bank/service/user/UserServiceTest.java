package com.bank.service.user;

import static org.assertj.core.api.Assertions.assertThat; // 1-7.
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;  // 1-4. stub when

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.constant.user.UserEnum;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.user.JoinReqDto;
import com.bank.dto.user.JoinRespDto;

@ExtendWith(MockitoExtension.class)  // 1-1. Mockito 환경에서 테스트 하겠다는 의미.
public class UserServiceTest {

	@InjectMocks  // 1-4. 1-1에서 만들어진 가짜환경 안에 1-2의 UserRepository를 가져와서 service에 주입해준다.
	private UserService userService;
	
	@Mock // 1-2. 가짜 UserRepository를 1-1에서 만든 모키토 환경에 만들어준다.(실제로 userRepository를 여기서 쓰진 않지만 service에선 사용하기 때문)
	private UserRepository userRepository;
	
	@Spy // 1-3. 진짜 BCryptPasswordEncoder를 mockito 환경에 넣는다.
	private BCryptPasswordEncoder PasswordEncoder;

	@Test
	public void 회원가입_테스트() throws Exception {
		// given
		JoinReqDto joinReqDto = new JoinReqDto();
		
		joinReqDto.setUsername("ssar");
		joinReqDto.setPassword("1234");
		joinReqDto.setEmail("ssar@nate.com");
		joinReqDto.setFullname("쌀");
		
		// stub 1 : empty return
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());  // 1-6. 혹시 findByUsername이 실행되면 회원가입에 있는 중복처리가 패싱되서 다음으로 넘어간다.
//		when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
		
		// stub 2 : user return
		User user = User.builder()
				.id(1L)
				.username("ssar")
				.password("1234")
				.email("ssar@nate.com")
				.fullname("쌀")
				.role(UserEnum.CUSTOMER)
				.createdAt(LocalDateTime.now())
				.updateAt(LocalDateTime.now())
				.build();
		
		when(userRepository.save(any())).thenReturn(user);
		
		// when
		JoinRespDto joinRespDto = userService.join(joinReqDto);
		System.out.println("테스트 : " + joinRespDto);
		
		// then
		assertThat(joinRespDto.getId()).isEqualTo(1L);
		assertThat(joinRespDto.getUsername()).isEqualTo("ssar");
	}
	
}
