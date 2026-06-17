package com.organization.app.mentor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MentorProfile {
    private Long id;
    private Long accountId;
    @Builder.Default
    private List<CareerEntry> careers = new ArrayList<>();
    @Builder.Default
    private List<MentorField> mentorFields = new ArrayList<>();

    public void addCareer(String content) {
        CareerEntry entry = CareerEntry.create(this.id, content);
        this.careers.add(entry);
    }

    public void addMentorField(Long fieldId) {
        MentorField registration = MentorField.create(this.id, fieldId);
        this.mentorFields.add(registration);
    }
}
