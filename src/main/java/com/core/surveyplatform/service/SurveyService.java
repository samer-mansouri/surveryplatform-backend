package com.core.surveyplatform.service;

import com.core.surveyplatform.entity.Survey;
import com.core.surveyplatform.entity.User;
import com.core.surveyplatform.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    public Optional<Survey> findById(UUID id) {
        return surveyRepository.findById(id);
    }

    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    public void deleteById(UUID id) {
        surveyRepository.deleteById(id);
    }
}
