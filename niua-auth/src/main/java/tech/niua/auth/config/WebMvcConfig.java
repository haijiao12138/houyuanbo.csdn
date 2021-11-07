package tech.niua.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangzhen
 * @title: WebMvcConfig
 * @projectName niua_easy_parent
 * @description: TODO
 * @date 2020/6/3 11:05 下午
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${niua.profileMap}")
	private String staticAccessPath;
	@Value("${niua.profile}")
	private String uploadFolder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + uploadFolder);
	}

	/**
	 * 跨域
	 * 
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
}
