package com.core.surveyplatform.controller;

import com.core.surveyplatform.entity.Answer;
import com.core.surveyplatform.entity.Question;
import com.core.surveyplatform.entity.Survey;
import com.core.surveyplatform.service.AnswerService;
import com.core.surveyplatform.service.QuestionService;
import com.core.surveyplatform.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping("/general")
    public Map<String, Object> getGeneralStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Survey> allSurveys = surveyService.findAll();
        List<Question> allQuestions = allSurveys.stream()
                .flatMap(survey -> survey.getQuestions().stream())
                .collect(Collectors.toList());
        List<Answer> allAnswers = allQuestions.stream()
                .flatMap(question -> answerService.findByQuestion(question).stream())
                .collect(Collectors.toList());

        stats.put("totalSurveys", allSurveys.size());
        stats.put("totalQuestions", allQuestions.size());
        stats.put("totalAnswers", allAnswers.size());
        stats.put("activeSurveys", allSurveys.stream()
                .filter(this::isSurveyActive)
                .count());
        stats.put("averageQuestionsPerSurvey", allSurveys.isEmpty() ? 0 :
                allQuestions.size() * 1.0 / allSurveys.size());
        stats.put("averageAnswersPerSurvey", allSurveys.isEmpty() ? 0 :
                allAnswers.size() * 1.0 / allSurveys.size());

        // ➔ New: Answers today
        LocalDateTime today = LocalDateTime.now();
        long answersToday = allAnswers.stream()
                .filter(answer -> answer.getAnsweredAt().toLocalDate().isEqual(today.toLocalDate()))
                .count();
        stats.put("answersToday", answersToday);

        return stats;
    }

    @GetMapping("/survey/{surveyId}")
    public Map<String, Object> getSurveyStatistics(@PathVariable UUID surveyId) {
        Map<String, Object> stats = new HashMap<>();

        Survey survey = surveyService.findById(surveyId)
                .orElseThrow(() -> new NoSuchElementException("Survey not found"));

        List<Question> questions = questionService.findBySurvey(survey);
        List<Answer> allAnswers = questions.stream()
                .flatMap(q -> answerService.findByQuestion(q).stream())
                .collect(Collectors.toList());

        stats.put("surveyTitle", survey.getTitle());
        stats.put("surveyDescription", survey.getDescription());
        stats.put("startDate", survey.getStartDate());
        stats.put("endDate", survey.getEndDate());
        stats.put("isActive", isSurveyActive(survey));

        stats.put("numberOfQuestions", questions.size());
        stats.put("numberOfAnswers", allAnswers.size());

        // ➔ Answers per question
        Map<UUID, Integer> answersPerQuestion = new HashMap<>();
        for (Question question : questions) {
            List<Answer> answers = answerService.findByQuestion(question);
            answersPerQuestion.put(question.getId(), answers.size());
        }
        stats.put("answersPerQuestion", answersPerQuestion);

        // ➔ New: Answers today for this survey
        LocalDateTime today = LocalDateTime.now();
        long answersToday = allAnswers.stream()
                .filter(answer -> answer.getAnsweredAt().toLocalDate().isEqual(today.toLocalDate()))
                .count();
        stats.put("answersTodayForThisSurvey", answersToday);

        return stats;
    }

    private boolean isSurveyActive(Survey survey) {
        LocalDateTime now = LocalDateTime.now();
        return survey.getStartDate() != null && survey.getEndDate() != null &&
                (now.isAfter(survey.getStartDate()) && now.isBefore(survey.getEndDate()));
    }
}
