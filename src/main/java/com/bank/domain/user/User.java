package com.bank.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bank.constant.user.UserEnum;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)  // 1-12. JPA LocalDateTime 자동 생성 방법 1(1-13은 Application에 있다.)
@NoArgsConstructor // 1-3. JPA에서 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문에 추가(중요)
@Getter @Setter
@Table(name = "user_tb")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 1-1. 기본키는 DB에 맡게 설정.
	private Long id;  // 1-4. PK
	
	@Column(unique = true, nullable = false, length = 20)
	private String username;  // 1-5. 아이디
	
	@Column(nullable = false, length = 60)  // 주의 : 인코딩시에는 비밀번호가 짧더라도 길게 들어갈 수 있기 때문에 60으로 설정
	private String password;  // 1-6. 패스워드
	
	@Column(nullable = false, length = 20)
	private String email;  // 1-7. 이메일
	
	@Column(nullable = false, length = 20)
	private String fullname;  // 1-8. 유저명
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserEnum role; // ADMIN, CUSTOMER  // 1-9. 유저 권한
	
	// 1-14. JPA LocalDateTime 자동 생성 방법 3 : LocalDateTime에 @CreatedDate, @LastModifiedDate 어노테이션을 걸어준다.
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;  // 1-10. 생성일
	 
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;  // 1-11. 수정일
	
	private String refreshToken;
	
	// 1-15. User entity builder 만들기
	@Builder
	public User(Long id, String username, String password, String email, String fullname, UserEnum role, LocalDateTime createdAt, LocalDateTime updateAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.role = role;
		this.createdAt = createdAt;
		this.updatedAt = updateAt;
	}
	
}
