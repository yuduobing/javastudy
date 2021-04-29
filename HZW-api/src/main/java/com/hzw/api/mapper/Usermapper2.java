package com.hzw.api.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.hzw.api.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 15105
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
public interface Usermapper2 extends BaseMapper<User> {

    //    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);
    public List<User> selectUser();

    List<JSONObject> queryMessage();

    int alterMessage(@Param(value = "record") Map<String, Object> map);
}
