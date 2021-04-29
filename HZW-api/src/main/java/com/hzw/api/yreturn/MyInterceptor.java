package com.hzw.api.yreturn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
@Configuration
public class MyInterceptor extends WebMvcConfigurerAdapter {
    @Bean
    public AccessTokenInterceptor accessTokenInterceptor() {
        return new AccessTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/platform/**");
        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/v2/**");
        registry.addInterceptor(new ResponseResultInterception()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }

}
