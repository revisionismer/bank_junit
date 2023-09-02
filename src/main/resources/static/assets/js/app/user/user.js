/**
 * user.js
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
	
	if(ACCESS_TOKEN != null) {
		console.log(ACCESS_TOKEN);
		
		var header = ACCESS_TOKEN.split('.')[0];
		var payload = ACCESS_TOKEN.split('.')[1];
		var signature = ACCESS_TOKEN.split('.')[2];
		
		console.log(header);
		console.log(payload);
		console.log(signature);
		
		console.log("복호화 : " + Base64.decode(payload));
		
		// 2023-02-12 -> 토큰에 있는 payload에 실어논 정보를 Base64로 디코드해서 가져와 세팅(base64.min.js 필요)
		var data = JSON.parse(Base64.decode(payload));

		console.log(data.username);
		
	}
	
	/**
	 * 4-1. 회원 정보 조회 -> 2023-08-22
	 */
	var userInfo = $("#userInfo").val();
	
	if(userInfo != null) {
		$("#userInfo").ready(function(){
			
			if(ACCESS_TOKEN != null) {
				$.ajax({
					type : "GET",
					url : "/api/user/s/info",
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						console.log(res);	
						
						$("#userId").val(res.data.id);
						$("#username").val(res.data.username);
						$("#fullname").val(res.data.fullname);
						$("#email").val(res.data.email);
							
					},
					error : function(res) {
						console.log(res);
						alert(res.responseJSON.message);
						location.href = "/login";
						return;
					}
				});	
				
			} else {
				alert("로그인을 해주세요.");
				location.href = "/login";
				return;
			}
			
		});
	}
	
	/**
	 * 4-2. 회원 정보 수정 -> 2023-08-28
	 */
	$("#userInfoModifyBtn").on("click", function(){
		console.log("회원 정보 수정 api 진행");
		
		if(ACCESS_TOKEN != null) {
			
			var userObject = {
				username : $("#username").val(),
				password : $("#password").val(),
				fullname : $("#fullname").val(),
				email : $("#email").val()
			};
			
			console.log(JSON.stringify(userObject));
			
			$.ajax({
				type : "PUT",
				url : "/api/user/s/update",
				data : JSON.stringify(userObject),
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
					
					if(res.code == 1) {
						alert(res.message);
						location.href = "/home";
					}
	
				},
				error : function(res) {
					console.log(res);
				
					if(res.responseJSON.message && res.responseJSON.data == null) {
						alert(res.responseJSON.message);
						location.href = "/login";
						return;
					} else if(res.responseJSON.data.username) {
						alert(res.responseJSON.data.username);
						$("#username").focus();	
					} else if(res.responseJSON.data.password) {
						alert(res.responseJSON.data.password)
						$("#password").focus();
						$("#password").val("");
					} else if(res.responseJSON.data.fullname) {
						alert(res.responseJSON.data.fullname);
						$("#fullname").focus();
					} else if(res.responseJSON.data.email) {
						alert(res.responseJSON.data.email);
						$("#email").focus();
					} 
					
				}
			});
			
		}
		
	});
	
});
