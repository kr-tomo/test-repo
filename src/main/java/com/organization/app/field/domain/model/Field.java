package com.organization.app.field.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Field {
    private Long id;
    private String name;
    private Long parentId;
    private FieldStatus status;

    public static Field create(String name, Long parentId) {
        validateName(name);
        return Field.builder()
                .name(name)
                .parentId(parentId)
                .status(FieldStatus.ACTIVE)
                .build();
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public void deactivate() {
        this.status = FieldStatus.INACTIVE;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("FIELD_NAME_REQUIRED");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("FIELD_NAME_TOO_LONG");
        }
    }
}
