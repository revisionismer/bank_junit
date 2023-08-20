/**
 * join.js
 */

$(document).ready(function() {
	
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
				
				if(data.code != -1) {
					alert(data.message);
					location.href = "/login";
				} 

			},
			error : function(res) {
				console.log(res);
				$("#username").val("");
				$("#password").val("");
				$("#email").val("");
				$("#fullname").val("");
				
				alert(res.responseJSON.message);	
			}
		});	
	});
});