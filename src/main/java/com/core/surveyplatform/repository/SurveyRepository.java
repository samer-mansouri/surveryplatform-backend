package com.core.surveyplatform.repository;

import com.core.surveyplatform.entity.Survey;
import com.core.surveyplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
