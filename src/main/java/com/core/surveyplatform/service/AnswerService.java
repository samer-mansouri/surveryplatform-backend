package com.core.surveyplatform.service;

import com.core.surveyplatform.entity.Answer;
import com.core.surveyplatform.entity.Question;
import com.core.surveyplatform.entity.User;
import com.core.surveyplatform.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> findByQuestion(Question question) {
        return answerRepository.findByQuestion(question);
    }

    public List<Answer> findByUser(User user) {
        return answerRepository.findByUser(user);
    }

    public Optional<Answer> findById(UUID id) {
        return answerRepository.findById(id);
    }
}
