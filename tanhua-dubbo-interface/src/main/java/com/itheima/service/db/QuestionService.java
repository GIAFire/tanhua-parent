package com.itheima.service.db;

import com.itheima.domain.db.Question;

public interface QuestionService {

    Question findByUserId(Long userId);
}
