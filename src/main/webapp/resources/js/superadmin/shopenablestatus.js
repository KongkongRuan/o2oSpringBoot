$(function() {
	// 获取此店铺列表的URL
	var listUrl = '/o2o/superadmin/admingetshoplist?userId='+sessionStorage.getItem('userId');
	// 店铺状态更改URL
	var statusUrl = '/o2o/superadmin/modifyshopenablestatus';
	
	getList();
	/**
	 * 获取商店列表
	 * 
	 * @returns
	 */
	function getList() {
		// 从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var shopList = data.shopList;
				var tempHtml = '';
				// 遍历每个店铺信息，拼接成一行显示，列信息包括：
				// 店铺名称，状态，禁止\通过(含shopId)，删除按钮(含shopId  待实现，危险程度较高)
				shopList.map(function(item, index) {
					var textOp = "违规整改";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 若状态值为0，表明是已下架的店铺，操作变为上架
						textOp = "审核通过";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每个店铺的行信息
					tempHtml += '' + '<div class="row row-product">'
					+ '<div class="col-33">'
					+ item.shopName
					+ '</div>'
					
					
					+ '<div class="col-20">'
					+ item.priority
					+ '</div>'
					
					+ '<div class="col-40">'
					+ '<a href="#" class="status" data-id="'
					+ item.shopId
					+ '" data-status="'
					+ contraryStatus
					+ '">'
					+ textOp
					+ '</a>'
					+ '</div>'
					
					+ '</div>';
				});
				// 将拼接好的信息赋值进html控件中
				$('.product-wrap').html(tempHtml);
			}
		});
	}
	// 将class为product-wrap里面的a标签绑定上点击的事件
	$('.product-wrap').on('click','a',
			function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('status')) {
							// 如果有class status则调用后台功能审核相关店铺，并带有shopId参数
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						}
					});
	
	function changeItemStatus(id, enableStatus) {
		// 定义shop json对象并添加shsopId以及状态(审核通过/审核中)
		var shop = {};
		shop.shopId = id;
		shop.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			// 审核相关店铺
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					shopStr : JSON.stringify(shop)
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