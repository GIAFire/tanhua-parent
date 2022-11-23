package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.db.AnalysisByDay;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AnalysisByDayMapper extends BaseMapper<AnalysisByDay> {

    // 累计用户
    @Select("select sum(num_registered) from tb_analysis_by_day;")
    Long findCumulativeUsers();

    // 7、30天活跃用户
    @Select("select sum(num_active) from tb_analysis_by_day where record_date between #{startTime} and #{endTime}")
    Long findActivePass(@Param("startTime") String startTime, @Param("endTime") String endTime);

    //  今日、昨天基础数据
    @Select("select * from tb_analysis_by_day where record_date =#{recordDate}")
    AnalysisByDay findBaseByRecordDate(String recordDate);
}