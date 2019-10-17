package com.imooc.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("localauth")
public class LocalAuthManagementController {
	@Autowired
	LocalAuthService localAuthService;
	/**
	 * 注册
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> register(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 接收前端参数的变量的初始化，包括本地账号，用户实体类
		ObjectMapper mapper = new ObjectMapper();
		String localAuthStr = HttpServletRequestUtil.getString(request, "localAuthStr");
		String personInfoStr = HttpServletRequestUtil.getString(request, "personInfoStr");
		LocalAuth localAuth;
		PersonInfo personInfo;
		try {
			localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
			personInfo = mapper.readValue(personInfoStr, PersonInfo.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//如果localAuth和personInfo都不为空，开始注册账号
		//首先判断两输入密码是否一致然后判断账号是否存在
		//如果都通过开始创建用户
		if(localAuth!=null && personInfo!=null) {
			if(localAuth.getUsername()!=null&&localAuth.getpassword()!=null) {
				String passwordrep=HttpServletRequestUtil.getString(request, "passwordrepStr");
				if(!passwordrep.equals(localAuth.getpassword())) {
					modelMap.put("success", false);
					modelMap.put("errMsg","两次密码输入不一致");
					return modelMap;
				}
				//判断账号是否已注册
				//查找数据库中是否存在该账号
				//如果存在直接返回错误
				try {
					int repsign = localAuthService.countLocalInfoByUserName(localAuth.getUsername());
					if(repsign==1) {
						modelMap.put("success", false);
						modelMap.put("errMsg","账号已存在");
						return modelMap;
					}
				}catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
					return modelMap;
				}
			}
			//创建用户
			localAuth.setPersonInfo(personInfo);
			try {
				LocalAuthExecution lae = localAuthService.register(localAuth);
				if(lae.getState()==LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", lae.getStateInfo());
				}
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入信息违法");
		}
		return modelMap;
	}
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> login(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		LocalAuth localAuth;
		String username = HttpServletRequestUtil.getString(request, "usernameStr");
		String password = HttpServletRequestUtil.getString(request, "passwordStr");
		
		if(username!=null&&password!=null) {
			try {
				localAuth= localAuthService.getLocalInfoByUserName(username);
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			if(localAuth!=null) {
				String pwd =localAuth.getpassword();
				if(pwd.equals(password)) {
					modelMap.put("success", true);
					modelMap.put("userType", localAuth.getPersonInfo().getUserType());
					request.getSession().setAttribute("user", localAuth.getPersonInfo());
					request.getSession().setAttribute("username", username);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", "密码错误，请确认密码");
				}
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "该用户不存在请注册");
			}
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名密码不能为空");
		}
		return modelMap;
	}
	@RequestMapping(value = "/getusername", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getUsername(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			
		String username=(String)request.getSession().getAttribute("username");
		
			modelMap.put("username", username);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			//清除session
			request.getSession().invalidate();
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifipassword", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifiPassword(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute("username");
		String oldPassword = HttpServletRequestUtil.getString(request, "oldPasswordStr");
		String newPassword = HttpServletRequestUtil.getString(request, "newPasswordStr");
		String repNewPassword = HttpServletRequestUtil.getString(request, "repNewPasswordStr");
				
		if(username!=null&&oldPassword!=null&&newPassword!=null&&repNewPassword!=null) {
			if(!newPassword.equals(repNewPassword)) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "两次输入的新密码不一致！");
				return modelMap;
			}
			try {
				LocalAuthExecution le =  localAuthService.modifiPassword(username, oldPassword, newPassword);
				if(le.getState()==LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					return modelMap;
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "修改密码失败！");
					return modelMap;
				}
				
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入信息不能为空");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> resetPassword(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String username = HttpServletRequestUtil.getString(request, "usernameStr");
		String email = HttpServletRequestUtil.getString(request, "emailStr");
		String newPassword = HttpServletRequestUtil.getString(request, "newPasswordStr");
		String repNewPassword = HttpServletRequestUtil.getString(request, "repNewPasswordStr");
		if(username!=null&&email!=null&&newPassword!=null) {
			if(!newPassword.equals(repNewPassword)) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "两次输入的新密码不一致！");
				return modelMap;
			}
			try {
				LocalAuthExecution le = localAuthService.resetPassword(username, email, newPassword);
				if(le.getState()==LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					return modelMap;
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "重置密码失败！");
					return modelMap;
				}
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入信息不能为空");
		}
		return modelMap;
	}
	
	
}
