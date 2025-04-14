package com.core.surveyplatform.repository;

import com.core.surveyplatform.entity.Question;
import com.core.surveyplatform.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findBySurvey(Survey survey);
}
