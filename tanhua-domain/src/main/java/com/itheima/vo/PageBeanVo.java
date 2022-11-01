package com.itheima.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

//分页
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBeanVo implements Serializable {

    private Long counts = 0L;//总记录数
    private Integer pagesize;//页大小
    private Long pages = 0L;//总页数
    private Integer page;//当前页码
    private List<?> items = Collections.emptyList(); //列表

    public PageBeanVo(Integer pageNum, Integer pageSize, Long counts, List list) {
        this.page = pageNum;
        this.pagesize = pageSize;
        this.items = list;
        this.counts = counts;
        this.pages = counts % pagesize == 0 ? (counts / pagesize) : (counts / pagesize) + 1;
    }
}
