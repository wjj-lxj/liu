package com.wjj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.wjj.controller.interceptor.MiniInterceptor;
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
		.addResourceLocations("file:F:/wjj-video/");
	}
	@Bean
	public MiniInterceptor miniInterceptor() {
		return new MiniInterceptor();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
						.addPathPatterns("/video/upload","/video/uploadCover",
								"/video/userLike", "/video/userUnLike")
								.addPathPatterns("/bgm/**").
								excludePathPatterns("/user/queryPublisher")
								.excludePathPatterns("/user/beyourfans",
										"/user/dontbeyourfans","/user/queryIfFollow"
										,"/user/reportUser");
		super.addInterceptors(registry);
	}
}
