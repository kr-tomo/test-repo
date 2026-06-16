package com.organization.app.field.adapter.in.web;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.in.CreateFieldUseCase;
import com.organization.app.field.domain.port.in.DeactivateFieldUseCase;
import com.organization.app.field.domain.port.in.UpdateFieldUseCase;
import com.organization.app.field.query.port.in.GetFieldsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Field", description = "분야 관리 API")
@RestController
@RequestMapping("/fields")
@RequiredArgsConstructor
public class FieldController {

    private final CreateFieldUseCase createFieldUseCase;
    private final UpdateFieldUseCase updateFieldUseCase;
    private final DeactivateFieldUseCase deactivateFieldUseCase;
    private final GetFieldsQuery getFieldsQuery;

    @Operation(summary = "분야 생성", description = "새로운 분야를 생성합니다. 운영자 권한이 필요합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FieldResponse createField(@RequestBody @Valid CreateFieldRequest request) {
        Field field = createFieldUseCase.createField(
                CreateFieldUseCase.Command.builder()
                        .name(request.name())
                        .parentId(request.parentId())
                        .build()
        );
        return FieldResponse.from(field);
    }

    @Operation(summary = "분야 수정", description = "분야 이름을 수정합니다. 운영자 권한이 필요합니다.")
    @PatchMapping("/{fieldId}")
    public FieldResponse updateField(
            @PathVariable Long fieldId,
            @RequestBody @Valid UpdateFieldRequest request) {
        Field field = updateFieldUseCase.updateField(
                UpdateFieldUseCase.Command.builder()
                        .id(fieldId)
                        .name(request.name())
                        .build()
        );
        return FieldResponse.from(field);
    }

    @Operation(summary = "분야 비활성화", description = "분야를 비활성화합니다. 하위 분야도 모두 비활성화됩니다.")
    @DeleteMapping("/{fieldId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateField(@PathVariable Long fieldId) {
        deactivateFieldUseCase.deactivateField(fieldId);
    }

    @Operation(summary = "분야 목록 조회", description = "계층 트리 구조로 분야 목록을 조회합니다.")
    @GetMapping
    public List<GetFieldsQuery.FieldResponse> getFields() {
        return getFieldsQuery.getFields();
    }

    public record FieldResponse(
            Long id,
            String name,
            Long parentId,
            FieldStatus status
    ) {
        public static FieldResponse from(Field field) {
            return new FieldResponse(field.getId(), field.getName(), field.getParentId(), field.getStatus());
        }
    }
}
