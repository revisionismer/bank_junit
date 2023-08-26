/**
 * login.js
 */

$(document).ready(function(){
	
	var ACCESS_TOKEN = "";

	$("#loginBtn").on("click", function(){
		/**
		 *  2-1. 로그인
		 */
		
		var loginObject = {
			username : $("#username").val(),
			password : $("#password").val()
					
		};
		
		console.log(JSON.stringify(loginObject));
		
		$.ajax({
			type : "POST",
			url : "/user/login",
			data : JSON.stringify(loginObject),
			contentType : "application/json; charset=UTF-8",
			success : function(data, textStatus, request) {
				
				console.log(data);
				
				if(data.code != -1) {
					var responseHeader = request.getResponseHeader('Authorization');
					
					ACCESS_TOKEN = responseHeader.substr(7);
					
					console.log("엑세스 토큰 : " + ACCESS_TOKEN);
					
					location.href = "/home";
				} else {
					alert(data.message);
					
				}

			},
			error : function(res) {
				console.log(res);
				alert(res.responseJSON.message);	
			}
		});	
		
	});
	
	/**
	 *  2-2. 뒤로가기
	 */
	$("#cancelHomeBtn").on("click", function(){
		location.href = "/";
	});

});
