package com.hzw.api.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */

@ApiModel("认证密钥")
@Accessors(chain = true)    // 开启链式编程
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppEntity {

    private long id;
    private String appId;
    private String appName;
    private String appSecret;
    private String accessToken;
    private int isFlag;

}
