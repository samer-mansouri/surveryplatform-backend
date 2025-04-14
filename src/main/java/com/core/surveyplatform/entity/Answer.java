package com.core.surveyplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Question question;

    @ManyToOne
    private User user; // Peut être null si réponse anonyme

    private String value;

    private LocalDateTime answeredAt = LocalDateTime.now();
}
