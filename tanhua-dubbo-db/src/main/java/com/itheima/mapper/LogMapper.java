package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.db.Log;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LogMapper extends BaseMapper<Log> {

    // 统计今日 注册、登录
    @Select("select count(user_id) from tb_log where type = #{type} and  log_time = #{logTime}")
    Integer findRegAngLogCount(@Param("type") String type, @Param("logTime") String logTime);

    // 统计今日活跃
    @Select("select count(distinct user_id) from  tb_log where log_time = #{logTime}")
    Integer findActiveCount(String logTime);

    // 统计次日留存
    @Select("select count(distinct user_id) from  tb_log where log_time = #{today}" +
            "and user_id in(select user_id from tb_log where type = '0102' and  log_time = #{yesterday})")
    Integer findRetentionCount(@Param("today") String today, @Param("yesterday") String yesterday);

}