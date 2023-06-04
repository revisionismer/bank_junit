package com.bank.dto.user;

import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Static 변수가 붙은 변수나 클래스는 클래스가 메모리에 올라갈 때 자동으로 생성이 됩니다.
// 상위 클래스에 포함된 하위 클래스는 미리 생성이 되어야 사용이 가능합니다
public class UserRespDto {

	@Getter @Setter
	@ToString
	public static class JoinRespDto {

		private Long id;
		private String username;
		private String fullname;
		
		public JoinRespDto(Long id, String username, String fullname) {
			this.id = id;
			this.username = username;
			this.fullname = fullname;
		}
		
		public JoinRespDto(User userEntity) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.fullname = userEntity.getFullname();
		}
	}
}
