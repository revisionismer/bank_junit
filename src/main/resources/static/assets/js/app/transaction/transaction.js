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
	 * 5-1. 거래 내역 정보 조회 -> 2023-09-01
	 */
	var transactionInfo = $("#transactionInfo").val();
	
	if(transactionInfo != null) {
		$("#transactionInfo").ready(function(){
			console.log("transactionInfo");
			
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
						
						$("#transaction_numberList").append(`<option selected disabled>선택해주세요.</option>`);
						
						res.data.accounts.forEach( (account) => {
							let transaction_numberList = getTransacitonNumberList(account);
							
							$("#transaction_numberList").append(transaction_numberList);
							
						});
							
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
	
	function getTransacitonNumberList(account) {
		let item = `<option value="${account.id}">${account.number}</option>`;
		
		return item;
	}
	
	$("#transaction_numberList").change(function() {
		
		var number = $(this).children("option:selected").text();
		
		if(ACCESS_TOKEN != null) {
			
			$.ajax({
				type : "GET",
				url : "/api/transaction/s/account/" + number + "/transactionInfo",
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
										<th colspan="2">no</td>
										<th colspan="3">구분</th>
										<th colspan="5">이체 금액</th>
										<th colspan="4">보낸 사람</th>
										<th colspan="4">받는 사람</th>
										<th colspan="8">전화번호</th>
										<th colspan="5">잔액</th>
									</tr>
								</thead>
									
								<!-- /* 게시글 리스트 rending 영역 */ -->
								<tbody id="list">
								</tbody>
							</table>
						</div>
					`;
				
					document.getElementById('transactionList').innerHTML = html;
				
					html = ``;
				
					if(!res.data.transactions.length) {
						html = '<td colspan="53">등록된 계좌가 없습니다.</td>';
					} else {
						for(var i = 0; i < res.data.transactions.length; i++) {
							html += `
								<tr>
									<td colspan="2">${i + 1}</td>
									<td colspan="3">${res.data.transactions[i].gubun}</td>
									<td colspan="5">${res.data.transactions[i].amount}</td>
									<td colspan="4">${res.data.transactions[i].sender}</td>
									<td colspan="4">${res.data.transactions[i].receiver}</td>
									<td colspan="8">${res.data.transactions[i].tel}</td>
									<td colspan="4">${res.data.transactions[i].balance}</td>
								</tr>
							`;
						}
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
	
});
