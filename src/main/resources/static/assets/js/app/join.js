/**
 * join.js
 */

$(document).ready(function() {
	
	/**
	 * 1-1. 회원 가입
	 */
	
	$("#joinBtn").on("click", function(){

		var joinObject = {
			username : $("#username").val(),
			password : $("#password").val(),
			email : $("#email").val(),
			fullname : $("#fullname").val()
		};
		
		console.log(JSON.stringify(joinObject));
		
		$.ajax({
			type : "POST",
			url : "/api/user/join",
			data : JSON.stringify(joinObject),
			contentType : "application/json; charset=UTF-8",
			success : function(data, textStatus, request) {
				
				console.log(data);
				
				if(data.code == 1) {
					alert(data.message);
					location.href = "/login";
				} 

			},
			error : function(res) {
				console.log(res);
				
				if(res.responseJSON.data.username) {
					alert(res.responseJSON.data.username);
					$("#username").focus();
				} else if(res.responseJSON.data.password) {
					alert(res.responseJSON.data.password)
					$("#password").focus();
				} else if(res.responseJSON.data.email) {
					alert(res.responseJSON.data.email);
					$("#email").focus();
				} else if(res.responseJSON.data.fullname) {
					alert(res.responseJSON.data.fullname);
					$("#fullname").focus()
				} else {
					alert(res.responseJSON.message);
				}
			
			}
		});	
	});
});