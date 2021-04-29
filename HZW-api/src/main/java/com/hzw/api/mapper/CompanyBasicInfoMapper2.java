package com.hzw.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzw.api.domain.CompanyBasicInfo2;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
public interface CompanyBasicInfoMapper2 extends BaseMapper<CompanyBasicInfo2> {

    int insertsn(@Param(value = "record") Map<String, Object> map);
}
