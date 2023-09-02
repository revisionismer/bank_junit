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
	
	/** 
	 * 3-1. 계좌 목록 조회  
	 
	if(ACCESS_TOKEN != null) {
		$.ajax({
			type : "GET",
			url : "/api/account/s/list",
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
				console.log(res);
				
				res.data.accounts.forEach((account) => {
					let accountList = getAccount(account);
					
					$("#my_accounts").append(accountList);
				});				
				
			},
			error : function(res) {
				console.log(res);
								
			}
		});
	}	
	
	function getAccount(account) {
		let item = `<option value="${account.id}">${account.number}</option>`;
		
		return item;
	}
	 */
	/**
	 *  3-2. 등록 
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
								
								location.href = "/home";
							}
							
						},
						error : function(res) {
							console.log(res);
							$("#number").val("");
							$("#password").val("");
							
							// 2023-06-21
							// tip : if(변수) -> null, undefined, 0, NaN 체킹
							if(res.responseJSON.message && res.responseJSON.data == null) {
								alert(res.responseJSON.message);
								location.href = "/login";
								return;
							} else if(res.responseJSON.data.number) {
								alert(res.responseJSON.data.number);
							} else if(res.responseJSON.data.password) {
								alert(res.responseJSON.data.password);
							}

							
											
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
			alert("로그인을 해주세요.");
			location.href = "/login";
			return;
		}
	});
	
	/**
	 *  3-3. 조회 
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
								<table class="table table-dark table-bordered table-striped table-hover">
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
										<td colspan="2">${i + 1}</td>
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
	
	// 2023-08-09
	/**
	 * 3-4. 입금 하기 
	 */
	$("#depositBtn").on("click", function(){
		console.log("입금하기 버튼");
		
		var depositObject = {
			number : $("#accountNumber").val(),
			sender : $("#sender").val(),
			amount : $("#amount").val(),
			gubun : "DEPOSIT",
			tel : $("#tel").val()
		};
		
		console.log(JSON.stringify(depositObject));
		
		$.ajax({
			type : "POST",
			url : "/api/account/deposit",
			data : JSON.stringify(depositObject),
			contentType : "application/json; charset=UTF-8",
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
				} else if(res.responseJSON.data.number) {
					alert(res.responseJSON.data.number);
					$("#accountNumber").focus();
				} else if(res.responseJSON.data.sender) {
					alert(res.responseJSON.data.sender)
					$("#sender").focus();
				} else if(res.responseJSON.data.amount) {
					alert(res.responseJSON.data.amount);
					$("#amount").focus();
				} 
				
			}
		});	
	});
	
	/**
	 *  3-5. 뒤로가기
	 */
	$("#cancelBtn").on("click", function(){
		history.back();
	});
	
	
	/**
	 *  3-6. 출금 하기
	 */
	var accountInfo = $("#accountInfo").val();
	
	if(accountInfo != null) {
		$("#accountInfo").ready(function(){
			console.log("accountInfo");
			
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
						
						$("#numberList").append(`<option selected disabled>선택해주세요.</option>`);
						
						res.data.accounts.forEach( (account) => {
							let numberList = getNumberList(account);
							
							$("#numberList").append(numberList);
							
						});
						
						$("#fullname").val(res.data.fullname);
						
						
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
	
	function getNumberList(account) {
		let item = `<option value="${account.id}">${account.number}</option>`;
		
		return item;
	}
	
	$("#numberList").change(function() {
		
		var number = $(this).children("option:selected").text();
		
		if(ACCESS_TOKEN != null) {
			
			$.ajax({
				type : "GET",
				url : "/api/account/s/" + number + "/info",
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);
					
					$("#balance").val(res.data.balance);
					// 2023-08-24 : 여기까지
					
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
	
	$("#withdrawBtn").on("click", function(){
		
		var withdrawObject = {
			number : $("#numberList option:selected").text(),
			password : $("#password").val(),
			amount : $("#amount").val(),
			gubun : "WITHDRAW"
		};
		
		console.log(JSON.stringify(withdrawObject));
		
		$.ajax({
			type : "POST",
			url : "/api/account/s/withdraw",
			data : JSON.stringify(withdrawObject),
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
				} else if(res.responseJSON.data.number) {
					alert("계좌 번호를 선택해주세요.");
					$("#numberList").focus();
				} else if(res.responseJSON.data.password) {
					alert(res.responseJSON.data.password)
					$("#password").focus();
					$("#password").val("");
				} else if(res.responseJSON.data.amount) {
					alert(res.responseJSON.data.amount);
					$("#amount").focus();
				} 
				
			}
		});	
		
		
	});
	

});