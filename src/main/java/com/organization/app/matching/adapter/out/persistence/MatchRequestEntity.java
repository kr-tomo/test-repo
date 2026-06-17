package com.organization.app.matching.adapter.out.persistence;

import com.organization.app.matching.domain.model.MatchRequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "match_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentee_account_id", nullable = false)
    private Long menteeAccountId;

    @Column(name = "mentor_account_id")
    private Long mentorAccountId;

    @Column(name = "field_id", nullable = false)
    private Long fieldId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MatchRequestStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;
}
