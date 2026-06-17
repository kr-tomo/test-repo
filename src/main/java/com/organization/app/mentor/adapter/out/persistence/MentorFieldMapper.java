package com.organization.app.mentor.adapter.out.persistence;

import com.organization.app.mentor.domain.model.MentorField;
import org.springframework.stereotype.Component;

@Component
public class MentorFieldMapper {

    public MentorField toDomain(MentorFieldEntity entity) {
        return MentorField.builder()
                .id(entity.getId())
                .mentorProfileId(entity.getMentorProfileId())
                .fieldId(entity.getFieldId())
                .status(entity.getStatus())
                .visible(entity.getVisible())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public MentorFieldEntity toEntity(MentorField domain) {
        return MentorFieldEntity.builder()
                .id(domain.getId())
                .mentorProfileId(domain.getMentorProfileId())
                .fieldId(domain.getFieldId())
                .status(domain.getStatus())
                .visible(domain.getVisible())
                .build();
    }
}
