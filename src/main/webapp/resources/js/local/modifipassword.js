$(function(){
	
	var modifiPasswordUrl='/o2o/localauth/modifipassword';
	var getUserNameUrl="/o2o/localauth/getusername";
	getusername();
	function getusername() {
		// 从后台获取用户名
		$.getJSON(getUserNameUrl, function(data) {
			if (data.success) {
				var tempHtml = '';
				tempHtml+='<input type="text" value="'+data.username+'" readonly>';
				// 将拼接好的信息赋值进html控件中
				$('.username-wrap').html(tempHtml);
				};
		});
	}
	
	
	$('#submit').click(function() {
		username=$('#username').val();
		oldPassword=$('#oldPassword').val();
		newPassword=$('#newPassword').val();
		repNewPassword=$('#repNewPassword').val();
		var formData=new FormData();
		formData.append('usernameStr',username);
		formData.append('oldPasswordStr',oldPassword);
		formData.append('newPasswordStr',newPassword);
		formData.append('repNewPasswordStr',repNewPassword);
		$.showPreloader('修改中,请稍后');
		$.ajax({
			url:modifiPasswordUrl,
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			timeout: 20000,
			success : function(data) {
				if (data.success) {
						$.toast('修改成功！');
				} else {
					$.toast('修改失败！'+data.errMsg);
				}
			},
			complete :function(XMLHttpRequest, TextStatus){
				$.hidePreloader();
				
				if(TextStatus=='timeout'){
					$.alert('请求超时！');
					
				}
				if(TextStatus=='error'){
					$.alert('服务器请求错误！');
					
				}
				//如果关闭服务器则返回abort
				if(TextStatus=='abort'){
					$.alert('服务器请求终止！');
					
				}
			}
		});
	})
})