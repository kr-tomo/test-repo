package com.organization.app.field.query.service;

import com.organization.app.field.adapter.out.persistence.FieldEntity;
import com.organization.app.field.adapter.out.persistence.JpaFieldRepository;
import com.organization.app.field.query.port.in.GetFieldsQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldQueryService implements GetFieldsQuery {

    private final JpaFieldRepository jpaFieldRepository;

    @Override
    public List<FieldResponse> getFields() {
        List<FieldEntity> allEntities = jpaFieldRepository.findAll();
        
        Map<Long, List<FieldResponse>> childrenMap = allEntities.stream()
                .filter(e -> e.getParentId() != null)
                .collect(Collectors.groupingBy(
                        FieldEntity::getParentId,
                        Collectors.mapping(this::toResponse, Collectors.toList())
                ));

        List<FieldResponse> rootFields = allEntities.stream()
                .filter(e -> e.getParentId() == null)
                .map(this::toResponse)
                .collect(Collectors.toList());

        fillChildren(rootFields, childrenMap);
        
        return rootFields;
    }

    private FieldResponse toResponse(FieldEntity entity) {
        return new FieldResponse(
                entity.getId(),
                entity.getName(),
                entity.getParentId(),
                entity.getStatus(),
                new ArrayList<>()
        );
    }

    private void fillChildren(List<FieldResponse> parents, Map<Long, List<FieldResponse>> childrenMap) {
        for (FieldResponse parent : parents) {
            List<FieldResponse> children = childrenMap.getOrDefault(parent.id(), new ArrayList<>());
            parent.children().addAll(children);
            if (!children.isEmpty()) {
                fillChildren(children, childrenMap);
            }
        }
    }
}
