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
@RequestMapping("/surveys")
@RequiredArgsConstructor
class SurveyController {

    private final SurveyService surveyService;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createSurvey(@RequestBody Survey survey, @RequestParam UUID creatorId) {
        Optional<User> user = userService.findById(creatorId);
        if (user.isEmpty()) return ResponseEntity.badRequest().body("Invalid user ID");

        survey.setCreator(user.get());
        return ResponseEntity.ok(surveyService.save(survey));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSurvey(@PathVariable UUID id) {
        return surveyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<?> getByCreator(@PathVariable UUID creatorId) {
        return userService.findById(creatorId)
                .map(surveyService::findByCreator)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
