/**
*
*/
$(function(){
	var registerLocalAuthUrl='/o2o/localauth/register';
	$('#submit').click(function() {
		//获取radio选中的值
		var radios =document.getElementsByName("userType");
	    var radioValue = 0;
        for(var i=0;i<radios.length;i++){
            if(radios[i].checked == true){
            	radioValue = radios[i].value;
            }
        }
		var localAuth={};
		var PersonInfo={};
		localAuth.username=$('#username').val();
		localAuth.password=$('#password').val();
		PersonInfo.userType=radioValue;
		PersonInfo.name=$('#name').val();
		PersonInfo.gender=$('#selectGender').val();
		PersonInfo.profileImg=$('#headimage').val();
		PersonInfo.email=$('#email').val();
		passwordrep=$('#passwordrep').val();
		var formData=new FormData();
		formData.append('localAuthStr',JSON.stringify(localAuth));
		formData.append('personInfoStr',JSON.stringify(PersonInfo));
		formData.append('passwordrepStr',passwordrep);
		var verifyCodeActual=$('#j-kaptcha').val();
		if(!verifyCodeActual){
			$.toast("请输入验证码！");
			return;
		}
		
		formData.append('verifyCodeActual',verifyCodeActual);
		$.showPreloader('上传中,请稍后')
		$.ajax({
			url:registerLocalAuthUrl,
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			timeout: 20000,
			success : function(data) {
				
				if (data.success) {
					$.toast('注册成功！');
					$('#kaptcha-img').click();
					setTimeout(function(){window.location.href="/o2o/local/login";},1000);
				} else {
					$.toast('注册失败！'+data.errMsg);
					$('#kaptcha-img').click();
				}
			},
			complete :function(XMLHttpRequest, TextStatus){
				$.hidePreloader();
				
				if(TextStatus=='timeout'){
					$.alert('请求超时！');
					$('#kaptcha-img').click();
				}
				if(TextStatus=='error'){
					$.alert('服务器请求错误！');
					$('#kaptcha-img').click();
				}
				//如果关闭服务器则返回abort
				if(TextStatus=='abort'){
					$.alert('服务器请求终止！');
					$('#kaptcha-img').click();
				}
			}
		});
	})
})