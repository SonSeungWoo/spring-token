package me.seungwoo.config;

import me.seungwoo.interceptor.ApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-21
 * Time: 16:29
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private ApiInterceptor apiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor)
                .addPathPatterns("/rest/*");
    }
}
