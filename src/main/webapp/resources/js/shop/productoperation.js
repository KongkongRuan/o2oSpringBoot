$(function() {
	// 通过Url是否含有productId来判断是添加商品还是编辑
	var productId = getQueryString('productId');
	// 根据productId获取商品信息Url
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
	// 获取当前店铺设定的商品类别列表Url
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	// 商品提交Url，通过标识符来判断是添加还是编辑操作
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	// 通过标识符，确定调用的方法
	var isEdit = false;
	if (productId) {
		// 为true，则根据productId调用获取product信息的方法
		getInfo(productId);
		isEdit = true;
	} else {
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}

	/**
	 * 初始化新增product页面
	 * 
	 * 根据页面原型和数据模型，需要加载该shop对应的productCategory信息(shop信息从服务端session中获取)
	 */
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				// 设置product_category
				var productCategoryList = data.data;
				var productCategoryTempHtml = '';
				productCategoryList.map(function(item, index) {
					productCategoryTempHtml += '<option data-value="'
							+ item.productCategoryId + '">'
							+ item.productCategoryName + '</option>';
				});
				$('#category').html(productCategoryTempHtml);
			}
		});
	};
	
	 /**
	 * 编辑页面,根据id获取商品信息
	 */
    function getInfo(id){
        $.getJSON(infoUrl,
	        function(data) {
	            if (data.success) {
	            	// 返回的json信息进行页面赋值
	                var product = data.product;
	                $('#product-name').val(product.productName);
	                $('#product-desc').val(product.productDesc);
	                $('#priority').val(product.priority);
	                $('#normal-price').val(product.normalPrice);
	                $('#promotion-price').val(product.promotionPrice);
	                // 设置商品类别列表及选中的商品类别
	                var optionHtml = '';
	                var optionArr = data.productCategoryList;
	                var optionSelected = product.productCategory.productCategoryId;
	                //生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
	                optionArr.map(function(item, index) {
                            var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
                            optionHtml += '<option data-value="'
                                    + item.productCategoryId
                                    + '"'
                                    + isSelect
                                    + '>'
                                    + item.productCategoryName
                                    + '</option>';
	                        });
	                $('#category').html(optionHtml);
	            }
	        });
    };

	/**
	 * 点击控件的最后一个且图片数量小于6个的时候，生成一个选择框
	 */
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});
	
	/**
	 * 提交按钮的响应时间,分别对商品添加和商品编辑做不同的相应
	 */
	$('#submit').click(
			function() {
				
				$.showPreloader('上传中,请稍后')
				
				// 创建商品Json对象，并从表单对象中获取对应的属性值
				var product = {};
				product.productName = $('#product-name').val();
				product.productDesc = $('#product-desc').val();
				product.priority = $('#priority').val();
				product.normalPrice = $('#normal-price').val();
				product.promotionPrice = $('#promotion-price').val();
				// 获取商品的特定目录值
				product.productCategory = {
					productCategoryId : $('#category').find('option').not(function() {
						return !this.selected;
					}).data('value')
				};
				product.productId = productId;
				var thumbnail = $('#small-img')[0].files[0];
				var formData = new FormData();
				formData.append('thumbnail', thumbnail);
				$('.detail-img').map(
					function(index, item) {
						// 判断该控件是否已经选择了文件
						if ($('.detail-img')[index].files.length > 0) {
							// 将第i个文件流赋值给key为productImgi的表单键值对里
							formData.append('productImg' + index, $('.detail-img')[index].files[0]);
						}
					});
				// 将product 转换为json ,添加到forData
				formData.append('productStr', JSON.stringify(product));

				// 获取表单中的验证码
				var verifyCodeActual = $('#j-kaptcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);

				// 使用ajax异步提交
				$.ajax({
					url : productPostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					timeout: 20000,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功！');
							$('#kaptcha-img').click();
						} else {
							$.toast('提交失败！'+data.errMsg);
							$('#kaptcha-img').click();
						}
					},
					complete :function(XMLHttpRequest, TextStatus){
						$.hidePreloader();
						
						if(TextStatus=='timeout'){
							$.toast('请求超时！');
							$('#kaptcha-img').click();
						}
						if(TextStatus=='error'){
							$.toast('服务器请求错误！');
							$('#kaptcha-img').click();
						}
						//如果关闭服务器则返回abort
						if(TextStatus=='abort'){
							$.toast('服务器请求终止！');
							$('#kaptcha-img').click();
						}
					}
				});

			});
});

