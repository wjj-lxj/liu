package com.wjj;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
/**
 * 继承SpringBootServletInitializer，相当于使用web.xml的形式去部署
 * @author Administrator
 *
 */
public class WarStartApplication extends SpringBootServletInitializer{

	/**
	 * 重写配置文件
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 使用web.xml运行应用程序，指向Application，最后启动sprigBoot
		return builder.sources(Application.class);

}
}
