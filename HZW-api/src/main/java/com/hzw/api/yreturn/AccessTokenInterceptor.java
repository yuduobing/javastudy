package com.hzw.api.yreturn;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.CompanyBasicInfo2;
import com.hzw.api.mapper.CompanyBasicInfoMapper2;
import com.hzw.api.mapper.LogMapper;
import com.hzw.api.util.RedisUtil;
import com.hzw.common.exception.api.ApiException;
import com.hzw.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

public class AccessTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CompanyBasicInfoMapper2 companyBasicInfoMapper2;
    @Autowired
    private LogMapper logMapper;
    //过期时间
    private Integer timeToken = 60 * 60 * 2;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {

        String accessToken = httpServletRequest.getHeader("accessToken");

        String appId = httpServletRequest.getHeader("appId");
        if (StringUtils.isEmpty(accessToken)) {
            // 参数Token accessToken
            throw new ApiException("无权访问", 401);
        }
        // 判断accessToken是否空
        if (StringUtils.isEmpty(appId)) {
            // 参数Token accessToken
            throw new ApiException("无权访问", 401);
        }
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", appId);
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        if (companyBasicInfo2 == null) {
            throw new ApiException("无权访问", 401);
        }

        String appsercret = companyBasicInfo2.getSecretid();
        String mysign = "";
        final String signa = "";
        String accesstokenid = "accesstoken:" + appId;
        String redisname = redisUtil.get(accesstokenid);
        if (redisname != null) {
            if (!redisname.equalsIgnoreCase(accessToken)) {
                throw new ApiException("无权访问", 401);
            }
        } else {
            String appId1 = httpServletRequest.getHeader("appId1");
            String apsid = appId1 + appsercret;
            String accesstokenme = StringUtils.crypt(apsid);
            if (!accesstokenme.equalsIgnoreCase(accessToken)) {
                throw new ApiException("无权访问", 401);
            }
            redisUtil.set(accesstokenid, accesstokenme, timeToken);

        }
        BufferedReader reader = httpServletRequest.getReader();
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();

        }
        mysign = builder.toString();
//为了hashmap遍历有序
        LinkedHashMap hashMap = JSON.parseObject(mysign, LinkedHashMap.class);
        builder.delete(0, builder.length());
        hashMap.forEach((k, v) -> {
            if (k.toString().equalsIgnoreCase("sign")) {
                return;
            }
            builder.append(v);

        });
        builder.append(appsercret);
        reader.close();
        mysign = builder.toString();
        if (hashMap.get("sign") == null) {
            throw new ApiException("参数错误", 10000);

        }
        //md5加密
        mysign = StringUtils.crypt(mysign);

        if (!hashMap.get("sign").toString().equalsIgnoreCase(mysign)) {
            throw new ApiException("验签失败", 10010);
        }
        return true;
        // 判断accessToken是否空

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

        String accessToken = httpServletRequest.getHeader("accessToken");
        String appId = httpServletRequest.getHeader("appId");
        String requestURI = httpServletRequest.getRequestURI();
        String requestContent = "";
        //获取返回结果
        Object result = httpServletRequest.getAttribute("response");
        try{
            BufferedReader reader = httpServletRequest.getReader();
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            requestContent = builder.toString();
        }catch (Exception e1){
            throw new ApiException("服务异常", 500);
        }
        JSONObject insertLog = new JSONObject();
        insertLog.put("accessToken", accessToken);
        insertLog.put("appId", appId);
        insertLog.put("requestUrl", requestURI);
        insertLog.put("requestContent", requestContent);

        logMapper.insertRequestLog(insertLog);
    }

    // 返回错误提示
    public void resultError(String errorMsg, HttpServletResponse httpServletResponse) throws IOException {
        throw new ApiException("服务异常", 500);
    }

}