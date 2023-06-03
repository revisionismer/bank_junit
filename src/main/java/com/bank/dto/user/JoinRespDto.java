package com.bank.dto.user;

import com.bank.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class JoinRespDto {

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
