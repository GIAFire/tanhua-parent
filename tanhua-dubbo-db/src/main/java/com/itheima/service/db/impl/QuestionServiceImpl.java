package com.itheima.service.db.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.domain.db.Question;
import com.itheima.mapper.QuestionMapper;
import com.itheima.service.db.QuestionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question findByUserId(Long userId) {
        return questionMapper.selectOne(new LambdaQueryWrapper<Question>()
                .eq(Question::getUserId,userId));
    }
}
