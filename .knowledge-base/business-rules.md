# Business Rules (BR)

참조: [INDEX.md](./INDEX.md) | 도메인 모델: [domain-model.md](./domain-model.md)

BR은 “어떤 조건에서 무엇이 허용/금지되는가”를 정의한다.
데이터 형식·범위 규칙은 [validation-rules.md](./validation-rules.md)를 참조한다.

-----

## 운영자 권한 {#운영자-권한}

### BR-OP-001

- **조건**: 운영자가 멘토 경력·분야 등록 요청을 검토·승인·반려하려는 경우
- **결과**: `ACCOUNT_MANAGEMENT` 권한이 있어야 한다. 없으면 거부된다.

### BR-OP-002

- **조건**: 운영자가 매칭 수립·해지·아카이빙 또는 멘토 지정을 처리하려는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. 없으면 거부된다.

-----

## 분야 (Field) {#분야-field}

### BR-FIELD-001

- **조건**: 분야를 생성·수정·비활성화하려는 경우
- **결과**: `ACCOUNT_MANAGEMENT` 권한을 가진 운영자만 처리할 수 있다.

### BR-FIELD-002

- **조건**: 분야를 생성하거나 이름을 수정하는 경우
- **결과**: 상위 카테고리와 무관하게 전체 분야에서 이름이 고유해야 한다.

### BR-FIELD-003

- **조건**: 하위 분야를 생성하거나 상위 분야를 지정하는 경우
- **결과**: 지정한 상위 분야(parentId)는 존재하고 ACTIVE 상태여야 한다.

### BR-FIELD-004

- **조건**: 멘토가 분야를 등록 요청하는 경우
- **결과**: 서비스에서 관리하는 분야(status=ACTIVE)에 해당하는 분야만 선택할 수 있다.

### BR-FIELD-005

- **조건**: 상위 분야가 INACTIVE로 전환되는 경우
- **결과**: 하위 분야도 모두 INACTIVE로 전환된다.
- **OQ 참조**: [OQ-001](./open-questions.md#OQ-001)

-----

## 멘토 경력 등록 (Career) {#멘토-경력-등록-career}

### BR-CAREER-001

- **조건**: 멘토가 경력을 등록 요청하는 경우
- **결과**: 요청은 PENDING 상태로 생성된다. `ACCOUNT_MANAGEMENT` 권한을 가진 운영자 중 한 명이 검토 후 승인(APPROVED) 또는 반려(REJECTED)한다.

### BR-CAREER-002

- **조건**: 멘토가 자신의 경력 항목을 수정하려는 경우
- **결과**: 해당 경력 항목의 상태가 PENDING인 경우에만 수정할 수 있다. APPROVED·REJECTED·WITHDRAWN 상태에서는 수정할 수 없다.

### BR-CAREER-003

- **조건**: 멘토가 자신의 경력 항목을 철회하려는 경우
- **결과**: 해당 항목의 상태가 PENDING인 경우에만 WITHDRAWN으로 전환할 수 있다.

### BR-CAREER-004

- **조건**: 멘토가 자신의 경력 항목의 노출 설정을 변경하려는 경우
- **결과**: 해당 항목의 상태가 APPROVED인 경우에만 노출(visible) 설정을 변경할 수 있다.

-----

## 멘토 분야 등록 (MentorField) {#멘토-분야-등록-mentor-field}

### BR-MENTOR-FIELD-001

- **조건**: 멘토가 분야를 등록 요청하는 경우
- **결과**: 요청은 PENDING 상태로 생성된다. `ACCOUNT_MANAGEMENT` 권한을 가진 운영자가 검토 후 승인 또는 반려한다.
- **추가 제약**: 선택한 분야는 서비스에서 관리하는 ACTIVE 상태의 분야여야 한다 (BR-FIELD-002).

### BR-MENTOR-FIELD-002

- **조건**: 멘토가 자신의 분야 등록 항목을 수정하려는 경우
- **결과**: 해당 항목의 상태가 PENDING인 경우에만 수정할 수 있다.

### BR-MENTOR-FIELD-003

- **조건**: 멘토가 자신의 분야 등록 항목을 철회하려는 경우
- **결과**: 해당 항목의 상태가 PENDING인 경우에만 WITHDRAWN으로 전환할 수 있다.

### BR-MENTOR-FIELD-004

- **조건**: 멘토가 자신의 분야 등록 항목의 노출 설정을 변경하려는 경우
- **결과**: 해당 항목의 상태가 APPROVED인 경우에만 노출(visible) 설정을 변경할 수 있다.

### BR-MENTOR-FIELD-005

- **조건**: 멘토가 이미 PENDING 또는 APPROVED 상태인 분야를 다시 등록 요청하는 경우
- **결과**: 중복 등록 요청은 거부된다.
- **OQ 참조**: [OQ-002](./open-questions.md#OQ-002)

-----

## 매칭 (Matching) {#매칭-matching}

### BR-MATCH-001

- **조건**: 멘티가 매칭을 요청하는 경우
- **결과**: 관심 분야를 지정하여 MatchRequest를 생성한다. 상태는 REQUESTED.

### BR-MATCH-002

- **조건**: 멘티가 특정 멘토를 지정하여 매칭을 요청하는 경우 (Q&A 답변 멘토 포함)
- **결과**: 해당 멘토의 **매칭 여력**이 가능한 상태여야 한다. 매칭 여력이 없으면 요청이 거부된다.
- **OQ 참조**: [OQ-003](./open-questions.md#OQ-003) — 매칭 여력의 정의

### BR-MATCH-003

- **조건**: 운영자가 MatchRequest에 멘토를 지정하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. 지정 후 상태는 MENTOR_ASSIGNED.

### BR-MATCH-004

- **조건**: 운영자가 MatchRequest를 승인하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. 멘토가 지정된 상태(REQUESTED with 멘토 or MENTOR_ASSIGNED)에서만 승인 가능. 상태는 APPROVED.

### BR-MATCH-005

- **조건**: 매칭의 validUntil이 경과한 경우
- **결과**: Matching 상태가 EXPIRED로 자동 전환된다.

### BR-MATCH-006

- **조건**: 멘토가 현재 ACTIVE 상태 매칭의 연장 가능 여부를 설정하는 경우
- **결과**: 본인이 멘토로 속한 ACTIVE 매칭에 대해서만 extendable 값을 true/false로 설정할 수 있다.

### BR-MATCH-007

- **조건**: 운영자가 매칭을 해지하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. ACTIVE 상태 Matching을 EXPIRED로 전환한다.
- **OQ 참조**: [OQ-004](./open-questions.md#OQ-004) — 해지 시 환불 처리

### BR-MATCH-008

- **조건**: 운영자가 매칭을 아카이빙하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. EXPIRED 상태 Matching을 ARCHIVED로 전환한다.

-----

## 인스턴스 Q&A (InstantQA) {#인스턴스-qa-instant-qa}

### BR-QA-001

- **조건**: 멘티가 인스턴스 Q&A를 등록하는 경우
- **결과**: 분야(fieldId)를 반드시 지정해야 한다. 지정된 분야는 ACTIVE 상태여야 한다.

### BR-QA-002

- **조건**: 멘토가 인스턴스 Q&A 목록을 조회하는 경우
- **결과**: 자신의 APPROVED 분야(visible=true)에 해당하는 Q&A만 조회할 수 있다.
  목록은 최신 게재 순(postedAt 내림차순)으로 반환된다.

### BR-QA-003

- **조건**: 멘토가 인스턴스 Q&A에 답변하는 경우
- **결과**: 동일 InstantQA에 대해 멘토 1인당 1개의 답변만 허용된다. 이미 답변한 경우 재답변은 거부된다.

### BR-QA-004

- **조건**: 멘토가 자신의 답변을 수정하는 경우
- **결과**: 본인이 작성한 답변에 한해 content를 수정할 수 있다.

### BR-QA-005

- **조건**: 멘티가 자신의 Q&A 답변을 확인하는 경우
- **결과**: 본인이 게재한 InstantQA에 달린 답변만 조회할 수 있다.

### BR-QA-006

- **조건**: 멘티가 Q&A 답변 멘토에게 매칭을 요청하는 경우
- **결과**: BR-MATCH-002 적용 — 해당 멘토의 매칭 여력이 있어야 한다.

-----

## 결제 (Payment) {#결제-payment}

### BR-PAYMENT-001

- **조건**: 멘티가 멘토링 상품을 결제하는 경우
- **결과**: MatchRequest가 APPROVED 상태인 경우에만 결제할 수 있다. 결제 완료 시 Matching이 생성(ACTIVE)되고, 상품의 기간 동안 유효한 매칭 채널이 제공된다.

### BR-PAYMENT-002

- **조건**: 멘티가 연장 상품을 결제하는 경우
- **결과**: 해당 Matching의 extendable=true인 경우에만 결제할 수 있다. 결제 완료 시 별도 승인 없이 validUntil이 연장된다.

### BR-PAYMENT-003

- **조건**: 매칭 채널이 제공되는 경우
- **결과**: 채널은 Matching의 validUntil까지만 유효하다.