package com.core.surveyplatform.controller;

import com.core.surveyplatform.entity.*;
import com.core.surveyplatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
class QuestionController {

    private final QuestionService questionService;
    private final SurveyService surveyService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody Question question, @RequestParam UUID surveyId) {
        Optional<Survey> survey = surveyService.findById(surveyId);
        if (survey.isEmpty()) return ResponseEntity.badRequest().body("Invalid survey ID");

        question.setSurvey(survey.get());
        return ResponseEntity.ok(questionService.save(question));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<?> getQuestions(@PathVariable UUID surveyId) {
        return surveyService.findById(surveyId)
                .map(questionService::findBySurvey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

