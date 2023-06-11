package com.bank.config.jwt.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.config.jwt.JwtProperties;
import com.bank.domain.user.User;
import com.bank.domain.user.UserRepository;
import com.bank.dto.ResponseDto;
import com.bank.handler.exception.CustomApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Data
@Slf4j
public class JwtService {
	
	private final UserRepository userRepository;

	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();
	
	// 1-1. refresh token setting
	public void setRefreshToken(String username, String refreshToken) {
		
		log.info("refresh token 로그인 유저에 셋팅");
		
		Optional<User> userOp = userRepository.findByUsername(username);
		
		if(userOp.isPresent()) {
			
			User findUser = userOp.get();
			findUser.setRefreshToken(refreshToken);
			
			userRepository.save(findUser);
			
		} else {
			throw new CustomApiException("Refresh Token update error");
		}
	}
	
	// 1-2. 토큰의 유효성 및 만료일자 확인
	public boolean validationToken(String jwtToken) {
        try {
        	Jwts.parser().setSigningKey(secretKeyBytes).parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
          
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");

        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
           
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
          
        }
        return false;
    }
	
	// 1-3.
	public void sendErrorResponse(HttpServletResponse response, String message) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		
		String result = objectMapper.writeValueAsString(new ResponseDto<>(1, message, null));
		
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.getWriter().write(result);
	}
	
	// 1-4.
	public boolean isNeedToUpdateAccessToken(String access_token) {
		try {
			Date expiresAt = Jwts.parser().setSigningKey(secretKeyBytes).parseClaimsJws(access_token).getBody().getExpiration();
			
			Date current_date = new Date(System.currentTimeMillis());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current_date);
			calendar.add(Calendar.DATE, 2);
			
			Date after1dayFromToday = calendar.getTime();
			
			if(expiresAt.before(after1dayFromToday)) {
				log.info("access_token 만료 예정 일 : " + after1dayFromToday);
				return true;
			}
		} catch (CustomApiException e) {
			return true;
		}
		
		return false;
	}
}
