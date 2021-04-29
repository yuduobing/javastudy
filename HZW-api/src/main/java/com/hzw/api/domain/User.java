package com.hzw.api.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
@ApiModel("用户实体类")
@Accessors(chain = true)    // 开启链式编程
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    @NotNull(message = "用户id不能为空")
    private String name;
    private Integer age;
    private String email;
}