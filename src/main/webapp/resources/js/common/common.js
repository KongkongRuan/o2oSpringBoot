/**
*
*/

$.config = {router: false}

function changeVerifyCode(img){
	img.src="../Kaptcha?" + Math.floor(Math.random()*100);
}
function getQueryString(name){
	var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null){
		return decodeURIComponent(r[2]);
	}
	return '';
}
/**
 * 获取项目的ContexPath以便修正图片路由让其正常显示
 * @returns
 */
function getContexPath() {
	return "/o2o";
}
$('#log-out').click(function() {
	$.ajax({
		url:"/o2o/localauth/logout",
		type:'POST',
		contentType:false,
		processData:false,
		cache:false,
		timeout: 20000,
		success : function(data) {
			if (data.success) {
				$.toast('退出登录成功！');
					window.location.href="/o2o/local/login";
			} else {
				$.toast('退出登录失败！'+data.errMsg);
				
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
