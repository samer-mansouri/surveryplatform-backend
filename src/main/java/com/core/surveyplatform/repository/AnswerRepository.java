package com.core.surveyplatform.repository;

import com.core.surveyplatform.entity.Answer;
import com.core.surveyplatform.entity.Question;
import com.core.surveyplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    List<Answer> findByQuestion(Question question);
    List<Answer> findByUser(User user);
}
