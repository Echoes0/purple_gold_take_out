package com.itl.purple_gold.config;

import com.itl.purple_gold.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将前端请求的地址 /backend/** 映射到 classpath目录下的backend目录里的相应文件
        //classes文件夹下就是我们原项目的java文件和resources文件夹里面的内容
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");

        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展MVC的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展MVC框架的消息转换器，将java对象转成json");
        //消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter =new MappingJackson2HttpMessageConverter();

        //设置对象转换器，底层使用Jackson将java对象转成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将上面的消息转换器加到MVC框架的转换器集合中,加在最前，优先使用
        converters.add(0,messageConverter);
    }
}
