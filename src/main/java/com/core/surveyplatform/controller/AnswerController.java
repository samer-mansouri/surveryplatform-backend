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
@RequestMapping("/answers")
@RequiredArgsConstructor
class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswer(@RequestBody Answer answer, @RequestParam UUID questionId, @RequestParam(required = false) UUID userId) {
        Optional<Question> question = questionService.findById(questionId);
        if (question.isEmpty()) return ResponseEntity.badRequest().body("Invalid question ID");

        answer.setQuestion(question.get());
        if (userId != null) {
            userService.findById(userId).ifPresent(answer::setUser);
        }

        return ResponseEntity.ok(answerService.save(answer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getAnswersByQuestion(@PathVariable UUID questionId) {
        return questionService.findById(questionId)
                .map(answerService::findByQuestion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAnswersByUser(@PathVariable UUID userId) {
        return userService.findById(userId)
                .map(answerService::findByUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
