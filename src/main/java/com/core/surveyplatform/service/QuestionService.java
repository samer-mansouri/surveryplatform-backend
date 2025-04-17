package com.core.surveyplatform.service;

import com.core.surveyplatform.entity.Question;
import com.core.surveyplatform.entity.Survey;
import com.core.surveyplatform.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public Optional<Question> findById(UUID id) {
        return questionRepository.findById(id);
    }

    public List<Question> findBySurvey(Survey survey) {
        return questionRepository.findBySurvey(survey);
    }

    public void deleteById(UUID id) {
        questionRepository.deleteById(id);
    }
}
