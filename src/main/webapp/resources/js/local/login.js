$(function(){
	var loginLocalAuthUrl='/o2o/localauth/login';
	$('#submit').click(function() {
		//获取radio选中的值
		var radios =document.getElementsByName("userType");
	    var radioValue = 0;
        for(var i=0;i<radios.length;i++){
            if(radios[i].checked == true){
            	radioValue = radios[i].value;
            }
        }
		username=$('#username').val();
		password=$('#password').val();
		var formData=new FormData();
		formData.append('usernameStr',username);
		formData.append('passwordStr',password);
		formData.append('userTypeStr',radioValue);
		$.showPreloader('登录中,请稍后')
		$.ajax({
			url:loginLocalAuthUrl,
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			timeout: 20000,
			success : function(data) {
				//还需要完善，让店家，管理员，也可以登录普通用户
				if (data.success) {
					if(radioValue==1){
						$.toast('登录成功！');
						window.location.href="/o2o/frontend/index";
					}else if(radioValue==2&&(data.userType==2||data.userType==3)){
						$.toast('登录成功！');
						window.location.href="/o2o/shopadmin/shoplist";
					}else if(radioValue==3&&data.userType==3){
						$.toast('登录成功！');
						window.location.href="/o2o/superadmin/getuserlist";
					}else {
						$.toast('登录失败！');
					}
				} else {
					$.toast('登录失败！'+data.errMsg);
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