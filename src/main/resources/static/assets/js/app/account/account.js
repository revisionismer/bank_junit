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
	
	/**
	 *  1-3. 조회 
	 */
	var accountList = $("#accountList").val();

	if(accountList != null) {
		$("#accountList").ready(function(){
			console.log("accountList");
			
			if(ACCESS_TOKEN != null) {
			
				$.ajax({
					type : "GET",
					url : "/api/account/s/all",
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						console.log(res);
					
						console.log(res.data.accounts.length);
					
						var html = `
							<!-- /* 게시글 영역 */ -->
							<div class="table-responsive clearfix">
								<table class="table table-hover">
									<thead>
										<tr>
											<th colspan="2">no</td>
											<th colspan="6">계좌번호</th>
											<th colspan="5">잔액</th>
											<th colspan="4">소유자</th>
										</tr>
									</thead>
										
									<!-- /* 게시글 리스트 rending 영역 */ -->
									<tbody id="list">
									</tbody>
								</table>
							</div>
						`;
					
						document.getElementById('accountList').innerHTML = html;
					
						html = ``;
					
						if(!res.data.accounts.length) {
							html = '<td colspan="53">등록된 계좌가 없습니다.</td>';
						} else {
							for(var i = 0; i < res.data.accounts.length; i++) {
								html += `
									<tr>
										<td colspan="2">${res.data.accounts.length - i}</td>
										<td colspan="6">${res.data.accounts[i].number}</td>
										<td colspan="5">${res.data.accounts[i].balance}</td>
										<td colspan="4">${res.data.fullname}</td>
									</tr>
								`;
							}
						}
						document.getElementById('list').innerHTML = html;
					},
					error : function(res) {
						console.log(res);
					
						alert(res.responseJSON.message);

					}
				});
			} else {
				alert("로그인을 해주세요.");
				location.href = "/login";
				return;
			}
		});
	}
	
});