package com.organization.app.qa.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "instant_qa")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstantQAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentee_account_id", nullable = false)
    private Long menteeAccountId;

    @Column(name = "field_id", nullable = false)
    private Long fieldId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;
}
