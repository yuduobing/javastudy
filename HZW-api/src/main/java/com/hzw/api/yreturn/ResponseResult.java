package com.hzw.api.yreturn;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Documented
public @interface ResponseResult {

}
