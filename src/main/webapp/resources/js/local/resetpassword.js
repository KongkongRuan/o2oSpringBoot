$(function(){
	var resetPasswordUrl='/o2o/localauth/resetpassword';
	$('#submit').click(function() {
		username=$('#username').val();
		email=$('#email').val();
		newPassword=$('#newPassword').val();
		repNewPassword=$('#repNewPassword').val();
		var formData=new FormData();
		formData.append('usernameStr',username);
		formData.append('emailStr',email);
		formData.append('newPasswordStr',newPassword);
		formData.append('repNewPasswordStr',repNewPassword);
		$.showPreloader('重置中,请稍后');
		$.ajax({
			url:resetPasswordUrl,
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			timeout: 20000,
			success : function(data) {
				if (data.success) {
						$.toast('重置成功！');
				} else {
					$.toast('重置失败！'+data.errMsg);
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