/**
 * 
 */
package org.gaming.backstage.interceptor;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.backstage.advice.LocalDateTimeDeserializerConfig;
import org.gaming.backstage.advice.LocalDateTimeSerializerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author YY
 *
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

	protected static Set<BusinessInterceptor> interceptors = new HashSet<>();

	@Value("${spring.resources.static-locations:classpath:/templates/,classpath:/static/}")
	private String springResourcesStaticLocations;

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		List<BusinessInterceptor> list = new ArrayList<>(interceptors);
		list.sort(BusinessInterceptor.PRIORITY_SORTER);
		for (BusinessInterceptor interceptor : list) {
			registry.addInterceptor(interceptor).addPathPatterns(interceptor.pathPatterns());
		}
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 解决中文乱码
		converters.add(responseBodyConverter());
		// 解决 添加解决中文乱码后 上述配置之后，返回json数据直接报错 500：no convertter for return value of type
		converters.add(messageConverter());
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}

	@Bean
	public MappingJackson2HttpMessageConverter messageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getObjectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializerConfig());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializerConfig());
        objectMapper.registerModule(javaTimeModule);
		return objectMapper;
	}

	// classpath:/templates/,classpath:/static/,file:E:/WorkSpace/LayuiDemo/templates/

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] locations = springResourcesStaticLocations.split(",");
		registry.addResourceHandler("/**").addResourceLocations(locations);
	}

	@Override
	protected void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
	}
}
