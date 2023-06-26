/**
 * account.js
 */

$(document).ready(function(){

	var ACCESS_TOKEN = getCookie('access_token');
	
	function getCookie(key) {
		let result = null;
		let cookie = document.cookie.split(';');
		cookie.some(function(item) {
			item = item.replace(' ', '');
			
			let dic = item.split('=');
			if(key === dic[0]) {
				result = dic[1];
				return true;
			}
		});
		return result;
	}
	
	console.log(ACCESS_TOKEN);
	
	/* 1-1. 계좌 목록 조회
	$.ajax({
		type : "GET",
		url : "/api/account/s/list",
		contentType : "application/json; charset=UTF-8",
		headers: {
			"Authorization" : "Bearer " + ACCESS_TOKEN
		},
		success : function(res) {
			console.log(res);
			
			
			
		},
		error : function(res) {
			console.log(res);
							
		}
	});
	
	 */
	/**
	 *  1-2. 등록 
	 */
	$("#accountBtn").on("click", function(){
		console.log("accountBtnBtn");
		
		if(ACCESS_TOKEN != null) {
			
			console.log("계좌 등록 api 진행");
		
			var number = $("#number").val();
			var password = $("#password").val();
			
			var accountObject = {
				number : $("#number").val(),
				password : $("#password").val()
			};
			
			if(number.trim()) {
				
				if(password.trim()) {
				
					$.ajax({
						type : "POST",
						url : "/api/account/s/new",
						contentType : "application/json; charset=UTF-8",
						data: JSON.stringify(accountObject),
						headers: {
							"Authorization" : "Bearer " + ACCESS_TOKEN
						},
						success : function(res) {
							console.log(res);
							
							if(res.code == 1) {
								alert(res.message);
								$("#number").val("");
								$("#password").val("");
							}
							
						},
						error : function(res) {
							console.log(res);
							// 2023-06-21
							// tip : if(변수) -> null, undefined, 0, NaN 체킹
							if(res.responseJSON.message && res.responseJSON.data == null) {
								alert(res.responseJSON.message);
							} else if(res.responseJSON.data.number) {
								alert(res.responseJSON.data.number);
							} else if(res.responseJSON.data.password) {
								alert(res.responseJSON.data.password);
							}

							$("#number").val("");
							$("#password").val("");
											
						}
					});
				} else {
					alert("비밀번호를 입력해주세요.");
					$("#password").focus();
				}
				
				
			} else {
				alert("계좌번호를 입력해주세요.");
				$("#number").focus();
			}
			
			
		} else {
			alert("로그인을 해주세요");
			location.href = "/login";
			return;
		}
	});
	
});