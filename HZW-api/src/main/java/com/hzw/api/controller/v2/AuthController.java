package com.hzw.api.controller.v2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.common.core.domain.AjaxResult;
import com.hzw.common.core.redis.RedisCache;
import com.hzw.common.exception.api.ApiException;
import com.hzw.common.utils.StringUtils;
import com.hzw.api.domain.AppEntity;
import com.hzw.api.mapper.AppMapper;
import com.hzw.api.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AppMapper appMapper;
    @Autowired
    RedisUtil redisUtil;
    //过期时间
    private Integer timeToken = 60 * 60 * 2;
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/getAccessToken")
    public AjaxResult getAccesstoken(@RequestBody AppEntity appEntity) {
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("appId", appEntity.getAppId());
        AppEntity appResult = appMapper.selectOne(queryWrapper);
        //判断有appid.appsercret.有删除
        if (appResult == null) {
            throw new ApiException("没有对应机构的认证信息", 401);
        }
        int isFlag = appResult.getIsFlag();
        if (isFlag == 1) {
            throw new ApiException("您现在没有权限生成对应的AccessToken", 401);
        }
        //删除之前的，没有生成放到数据库和redis
        String accesstoken = newAccessTokeen();
        String accesstokenrd = "accesstoken:" + accesstoken;

        redisCache.setCacheObject(accesstokenrd, appResult, timeToken, TimeUnit.SECONDS);
        Object captcha = redisCache.getCacheObject(accesstokenrd);
        return AjaxResult.success("accesstoken获取成功", accesstoken);
    }

    private String newAccessTokeen() {
//32位 大写
        return StringUtils.crypt("1235");

    }

}
