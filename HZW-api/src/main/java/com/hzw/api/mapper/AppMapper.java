package com.hzw.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzw.api.domain.AppEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AppMapper extends BaseMapper<AppEntity> {

//	@Select("SELECT ID AS ID ,APP_NAME AS appName, app_id as appId, app_secret as appSecret ,is_flag as isFlag , access_token as accessToken from m_app "
//			+ "where app_id=#{appId} and app_secret=#{appSecret}  ")
//	AppEntity findApp(AppEntity appEntity);
//
//	@Select("SELECT ID AS ID ,APP_NAME AS appName, app_id as appId, app_secret as appSecret ,is_flag as isFlag  ,  access_token as accessToken from m_app "
//			+ "where app_id=#{appId}")
//	AppEntity findAppId(@Param("appId") String appId);
//
//	@Update(" update m_app set access_token =#{accessToken} where app_id=#{appId} ")
//	int updateAccessToken(@Param("accessToken") String accessToken, @Param("appId") String appId);
}
