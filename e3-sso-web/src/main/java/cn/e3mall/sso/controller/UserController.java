package cn.e3mall.sso.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

/**
 * 用户管理controller
 * @author Administrator
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	/**
	 *  数据有效性校验
	 * @param data
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{data}/{type}")
	@ResponseBody 
	public E3Result checkData(@PathVariable String data,@PathVariable int type){
		E3Result e3Result = userService.checkData(data, type);
		return e3Result;
	}
	/**
	 * 用户注册
	 * 
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result e3Result = userService.register(user);
		return e3Result;
	}
	
	@RequestMapping("/page/login")
	public String showLogin(){
		return "login";
	}
	/**
	 * 登录处理
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result doLogin(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result e3Result = userService.login(username, password);
		if(e3Result.getStatus()==200){
			CookieUtils.setCookie(request, response, "token", e3Result.getData().toString());
		}
		return e3Result;
	}
	
	/**
	 * 根据token来取用户信息
	 * 
	 */
	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		E3Result e3Result = userService.getUserByToken(token);
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			return callback + "("+JsonUtils.objectToJson(e3Result) +");";
		}
		return JsonUtils.objectToJson(e3Result);
	}
}
