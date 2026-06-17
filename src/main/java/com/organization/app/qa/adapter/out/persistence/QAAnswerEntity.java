package com.organization.app.qa.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "qa_answer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QAAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instant_qa_id", nullable = false)
    private Long instantQAId;

    @Column(name = "mentor_account_id", nullable = false)
    private Long mentorAccountId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "answered_at", nullable = false)
    private LocalDateTime answeredAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
