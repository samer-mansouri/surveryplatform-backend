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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable UUID id, @RequestBody Question updatedQuestion) {
        return questionService.findById(id).map(existingQuestion -> {
            existingQuestion.setText(updatedQuestion.getText());
            return ResponseEntity.ok(questionService.save(existingQuestion));
        }).orElse(ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable UUID id) {
        if (questionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        questionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
