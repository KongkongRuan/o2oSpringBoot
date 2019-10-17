$(function() {
	// 获取用户列表的URL
	var listUrl = '/o2o/superadmin/getpersonlist';
	// 用户状态更改URL
	var statusUrl = '';
	getList();
	/**
	 * 获取商店列表
	 * 
	 * @returns
	 */
	function getList() {
		// 从后台获取用户列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var personInfoList = data.personInfoList;
				var tempHtml = '';
				// 遍历每个用户信息，拼接成一行显示，列信息包括：
				// 用户名称，身份，封停\解封(含userId)，删除按钮(含userId  待实现，危险程度较高)
				personInfoList.map(function(item, index) {
					var textStatus = "违规封停(未实现)";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 若状态值为0，表明是已封停的用户，操作变为解封
						textStatus = "解封";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					var textType ="顾客";
					if(item.userType==2){
						textType ="店家";
					}
					if(item.userType==3){
						textType ="超级管理员";
					}
					// 拼接每个用户的行信息
					tempHtml += '' 
					+ '<div class="row row-person">'
					+ '<div  class="col-33" data-id="'+item.userId+'">'
					+ '<a href="#" class="userName" >'
					+ item.name
					+'</a>'
					+ '</div>'
					
					
					
					+ '<div class="col-20">'
					+ textType
					+ '</div>'
					
					+ '<div class="col-40">'
					+ '<a href="#" class="status" data-id="'
					+ item.userId
					+ '" data-status="'
					+ contraryStatus
					+ '">'
					+ textStatus
					+ '</a>'
					+ '</div>'
					
					+ '</div>';
				});
				// 将拼接好的信息赋值进html控件中
				$('.person-wrap').html(tempHtml);
			}
		});
	}
	
	// 将class为person-wrap里面class为col-33标签绑定上点击的事件
	$('.person-wrap').on('click','.col-33',
			function(e) {
						var userId =e.currentTarget.dataset.id;
						
						sessionStorage.setItem('userId',userId);
						window.location.href="/o2o/superadmin/getshoplist";
						
					});
	
	 //更改用户状态
	$('.person-wrap').on('click','a',
			function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('status')) {
							// 如果有class status则调用后台功能审核相关用户，并带有userId参数
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						}
					});
	function changeItemStatus(id, enableStatus) {
		// 定义PersonInfo json对象并添加userId以及状态(审核通过/审核中)
		var PersonInfo = {};
		PersonInfo.userId = id;
		PersonInfo.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			// 审核相关店铺
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					personStr : JSON.stringify(PersonInfo)
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
});