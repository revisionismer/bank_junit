# bank_junit 프로젝트

### 기능 정리
 1. 프로젝트 생성 및 github 연동
 2. 기본 화면(home) 설계 및 assets 파일 추가와 view 폴더 구조 잡기(fragments)
 3. 테이블 설계 및 User 엔티티 생성  
 4. Account 엔티티 생성
 5. Transaction 엔티티 생성
 6. SecurityConfig 기본 설정
 7. SecurityConfig 테스트 및 junit 테스트
 8. 공통 응답 DTO 만들기(ResponseDto)
 9. 회원 가입 서비스 만들기 및 CustomExceptionHandler 만들기
 10. 회원 가입 서비스 테스트
 11. 회원가입 서비스 코드 리팩토링
 12. 회원가입 컨트롤러 만들기 및 유효성 검사(AOP 사용 X)
 13. 회원가입 컨트롤러 유효성 검사(AOP 사용 O)
 14. 회원가입 컨트롤러 정규표현식 테스트
 15. 회원가입 컨트롤러 정규표현식 DTO 적용
 16. 회원가입 컨트롤러 테스트
 17. Jwt 토큰 생성을 위한 세팅
 18. Jwt 인증 필터 구현 및 SecurityConfig에 등록
 19. Jwt 인증 필터 등록 및 refreshToken 구현
 20. Jwt 인증 필터 successfulAuthentication에 응답 객체 만들기
 21. Jwt 토큰 로그인 실패 로직 처리(unsuccessfulAuthentication 재정의)
 22. Jwt 인가필터 구현 및 등록완료
 23. Jwt 인가필터 수정 및 화면 만들고 로그인 로그아웃 테스트
 24. Jwt AuthenticationFilter, AuthorizationFilter 테스트
 25. 계좌등록 서비스 만들기
 26. 계좌등록 컨트롤러 만들기 
 27. 계좌등록 서비스 테스트
 28. 계좌등록 컨트롤러 테스트
 29. 계좌 생성 화면에서 ajax로 비동기 통신하기
 30. 본인계좌목록보기 서비스 만들기 
 31. 본인계좌목록보기 컨트롤러 만들기
 32. 계좌삭제 서비스 만들기
 33. 계좌삭제 컨트롤러 만들기
 34. 계좌삭제 서비스 테스트
 35. 계좌삭제 컨트롤러 테스트
 36. 계좌입금 서비스 만들기
 37. 계좌입금 컨트롤러 만들기   
 38. 계좌입금 서비스 테스트
 39. 계좌입금 컨트롤러 테스트
 40. 계좌출금 서비스 만들기
 41. 계좌출금 서비스 테스트
 42. 계좌출금 컨트롤러 만들기
 43. 계좌출금 컨트롤러 테스트
 44. 계좌이체 서비스 만들기
 45. 계좌이체 서비스 테스트
 46. 계좌이체 컨트롤러 만들기
 47. 계좌이체 컨트롤러 테스트
 48. Long 타입 테스트
 49. cors 테스트
 50. 입출금내역 동적 쿼리 만들기
 51. @DataJpaTest 더미데이터 만들기
 52. @DataJpaTest autoincrement 초기화
 53. @DataJpaTest 더티체킹
 54. fetch join 테스트 및 gradlew clean & gradlew build 작업
 55. 입출금내역조회 서비스 만들기
 56. 입출금내역조회 컨트롤러 만들기
 57. 입출금내역조회 컨트롤러 테스트
 58. 계좌상세보기 서비스 만들기
 59. 계좌상세보기 컨트롤러 만들기
 60. 계좌상세보기 컨트롤러 테스트
 61. 전체 테스트 및 완강
 62. home.html 시큐리티 처리하기(쿠키가 없을때 로그인 버튼만 보이게 하고 쿠키가 있다면 로그인 버튼은 숨기고 로그아웃 버튼만 보이게 처리)
 63. depositForm.html에서 입금하는 로직을 account.js에서 ajax로 연결 및 Postman 테스트
 64. AccountListRespDto를 만들고 사용자 보유 계좌 정보 리스트 조회 서비스 만들기
 65. 사용자 보유 계좌 정보 리스트 조회 컨트롤러 만들기
 66. login 화면 부트스트랩으로 꾸미기
 67. accountForm, depositForm 부트스트랩으로 꾸미기
 68. 계좌정보 리스트 보기 API 출력 순서 변경
 69. form 태그 안에 있던 버튼을 밖으로 분리 작업 및 회원 가입 화면 꾸미고 js와 연결
 70. 회원 기능에서 회원 리스트를 보여주는 버튼을 회원 정보 보기로 변경
 71. select box에 값 뿌려주는 기능 구현 및 계좌 출금하기 기능 view와 연결
 72. 회원 정보보기 API 생성 및 화면구성, 거래 내역 API를 거래 내역 화면 transactionList에 연결 
 73. loginForm, joinForm 디자인 변경(html, css)