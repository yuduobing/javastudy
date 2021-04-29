package com.hzw.api.yreturn;

import com.hzw.common.filter.RepeatableFilter;
import com.hzw.common.filter.XssFilter;
import com.hzw.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

/**
 * Filter配置
 *
 * @author HZW
 */
@Configuration
public class FIlterConfig2 {

    @Bean
    public HeaderFilter accessfilter() {
        return new HeaderFilter();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean headerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(accessfilter());
        registration.addUrlPatterns("/*");
        registration.setName("headerfilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

}
