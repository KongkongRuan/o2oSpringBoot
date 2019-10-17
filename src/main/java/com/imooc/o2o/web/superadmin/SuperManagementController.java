package com.imooc.o2o.web.superadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.PersonInfoExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/superadmin")
public class SuperManagementController {
	
	@Autowired
	ShopService shopService;
	@Autowired
	PersonInfoService personInfoService;
	@RequestMapping(value = "/getpersonlist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> adminGetUserList(HttpServletRequest request){
    	Map<String,Object>modelMap = new HashMap<String,Object>();
    	
    	try {

        	//int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
    		//int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
    		PersonInfo personInfoCondition =new PersonInfo();
    		PersonInfoExecution pie = personInfoService.getPersonInfoList(personInfoCondition, 1, 100);
    		modelMap.put("personInfoList", pie.getPersonInfoList());
    		modelMap.put("success", true);
    	}catch(Exception e) {
    		modelMap.put("success",false);
    		modelMap.put("errMsg",e.getMessage());
    	}
    	return modelMap;
	}
	
	
    @RequestMapping(value = "/admingetshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> adminGetshoplist(HttpServletRequest request){
    	Map<String,Object>modelMap = new HashMap<String,Object>();
    	PersonInfo admin ;
    	
    	try {
    		Long userId =HttpServletRequestUtil.getLong(request, "userId");
    		//Long userId = (Long)request.getSession().getAttribute("userId");
    		admin=personInfoService.getPersonInfoById(userId);
    		Shop shopCondition=new Shop();
    		shopCondition.setOwner(admin);
    		ShopExecution se = shopService.getShopList(shopCondition,1, 100);
    		modelMap.put("shopList", se.getShopList());
    		modelMap.put("user", admin);
    		modelMap.put("success", true);
    	}catch(Exception e) {
    		modelMap.put("success",false);
    		modelMap.put("errMsg",e.getMessage());
    	}
    	
    	return modelMap;
    	
    }
    @RequestMapping(value = "/modifyshopenablestatus",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyShopEnableStatus(HttpServletRequest request){
    	Map<String,Object>modelMap = new HashMap<String,Object>();
    	ObjectMapper mapper = new ObjectMapper();
    	Shop shop =null;
    	try {
			String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
			//尝试获取前端传来的表单string并将其转化成Product实体类
			shop = mapper.readValue(shopStr, Shop.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//非空判断
		if(shop!=null&&shop.getShopId()!=null&&shop.getShopId()>0) {
			try {
				//开始进行商品变更操作
				ShopExecution se = shopService.modifyShopEnableStatus(shop);
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg",se.getStateInfo());
				}
			}catch(ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "失败：店铺信息为空");
		}
		return modelMap;
    }
}
