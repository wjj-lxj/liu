package com.wjj.controller.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wjj.utils.IMoocJSONResult;
import com.wjj.utils.JsonUtils;
import com.wjj.utils.RedisOperator;

public class MiniInterceptor implements HandlerInterceptor {
	@Autowired
	public RedisOperator redis;
	public static final String USER_REDIS_SESSION="user-redis-session";
	/**
	 * 拦截请求，在controller调用之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String userId=request.getHeader("userId");
		String userToken=request.getHeader("userToken");
		//System.out.println(userId);
		//System.out.println(userToken);
		if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)) {
		String uniqueToken=redis.get(USER_REDIS_SESSION +":"+userId);
		if(StringUtils.isEmpty(uniqueToken)&&StringUtils.isBlank(uniqueToken)) {
			System.out.println("请登陆....");
			returnErrorResponse(response, new IMoocJSONResult().errorTokenMsg("请登录..."));
			return false;
		}else {
			if(!uniqueToken.equals(userToken)) {
				System.out.println("账号被挤掉...");
				returnErrorResponse(response, new IMoocJSONResult().errorTokenMsg("账号被挤掉..."));
				return false;
			}
		}
		}else {
			System.out.println("请登陆....");
			returnErrorResponse(response, new IMoocJSONResult().errorTokenMsg("请登录..."));
			return false;
		}
		
		/**
		 * 返回false：请求被拦截，返回
		 * 返回true：请求ok，可以继续执行，放行
		 */
		return true;
	}
	
	public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result) 
			throws IOException, UnsupportedEncodingException {
		OutputStream out=null;
		try{
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("text/json");
		    out = response.getOutputStream();
		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
		    out.flush();
		} finally{
		    if(out!=null){
		        out.close();
		    }
		}
	}
	
	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	

	}
	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		

	}

}
