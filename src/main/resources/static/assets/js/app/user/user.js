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
						
						var html = `
							<!-- /* 게시글 영역 */ -->
							<div class="table-responsive clearfix">
								<table class="table table-dark table-bordered table-striped table-hover">
									<thead>
										<tr>
											<th colspan="2">userId</td>
											<th colspan="6">username</th>
											<th colspan="5">fullname</th>
											<th colspan="4">role</th>
										</tr>
									</thead>
										
									<!-- /* 게시글 리스트 rending 영역 */ -->
									<tbody id="list">
									</tbody>
								</table>
							</div>
						`;
					
						document.getElementById('content').innerHTML = html;
						
						html = ``;
						
						if(!res.data.id) {
							html = '<td colspan="53">사용자 정보를 찾을 수 없습니다.</td>';
						} else {
							html += `
								<tr>
									<td colspan="2">${res.data.id}</td>
									<td colspan="6">${res.data.username}</td>
									<td colspan="5">${res.data.fullname}</td>
									<td colspan="4">${res.data.role}</td>
								</tr>
							`;
						}
						
						document.getElementById('list').innerHTML = html;
							
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
	
	
});
