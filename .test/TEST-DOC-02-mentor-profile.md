# TEST_DOC: 멘토 프로필 — 경력·분야 등록 관리

연관 SPEC: SPEC-02-mentor-profile.md (v0.1)
작성일: 2026-06-16
개발 루프 대상 레벨: Unit, Component

-----

## 케이스 요약

|TC-ID    |이름                                      |중요도|계층             |루프 포함|연관 요건                                   |
|---------|----------------------------------------|---|---------------|-----|----------------------------------------|
|TC-MP-001|경력 등록 요청 성공 → PENDING 생성                |P0 |Unit, Component|Yes  |UC-CAREER-001, BR-001                   |
|TC-MP-002|경력 등록 — content 누락                      |P2 |Unit, Component|Yes  |VR:CAREER_CONTENT_REQUIRED              |
|TC-MP-003|경력 등록 — content 공백                      |P2 |Unit           |Yes  |VR:CAREER_CONTENT_REQUIRED              |
|TC-MP-004|경력 등록 — content 2000자 초과                |P2 |Unit, Component|Yes  |VR:CAREER_CONTENT_TOO_LONG              |
|TC-MP-005|경력 등록 — content 정확히 2000자 (경계값)         |P3 |Unit           |Yes  |VR:CAREER_CONTENT_TOO_LONG              |
|TC-MP-006|경력 등록 — 멘티 계정으로 시도                      |P0 |Component      |Yes  |UC-CAREER-001                           |
|TC-MP-007|경력 수정 성공 (PENDING)                      |P1 |Unit, Component|Yes  |UC-CAREER-002, BR-002                   |
|TC-MP-008|경력 수정 — APPROVED 상태에서 시도                |P2 |Unit, Component|Yes  |BR-002, VR:MODIFICATION_NOT_ALLOWED     |
|TC-MP-009|경력 수정 — REJECTED 상태에서 시도                |P2 |Unit           |Yes  |BR-002                                  |
|TC-MP-010|경력 수정 — WITHDRAWN 상태에서 시도               |P2 |Unit           |Yes  |BR-002                                  |
|TC-MP-011|경력 수정 — 타인 항목 수정 시도                     |P0 |Unit, Component|Yes  |BR-012                                  |
|TC-MP-012|경력 철회 성공 (PENDING → WITHDRAWN)          |P1 |Unit, Component|Yes  |UC-CAREER-003, BR-003                   |
|TC-MP-013|경력 철회 — APPROVED 상태에서 시도                |P2 |Unit, Component|Yes  |BR-003, VR:WITHDRAWAL_NOT_ALLOWED       |
|TC-MP-014|경력 철회 — 타인 항목 시도                        |P0 |Component      |Yes  |BR-012                                  |
|TC-MP-015|경력 노출 설정 — APPROVED → visible=false     |P1 |Unit, Component|Yes  |UC-CAREER-004, BR-004                   |
|TC-MP-016|경력 노출 설정 — APPROVED → visible=true (재노출)|P1 |Unit           |Yes  |UC-CAREER-004, BR-004                   |
|TC-MP-017|경력 노출 설정 — PENDING 상태에서 시도              |P2 |Unit, Component|Yes  |BR-004, VR:VISIBILITY_CHANGE_NOT_ALLOWED|
|TC-MP-018|경력 노출 설정 — 타인 항목 시도                     |P0 |Component      |Yes  |BR-012                                  |
|TC-MP-019|경력 검토 — 운영자 승인 (PENDING → APPROVED)     |P0 |Unit, Component|Yes  |UC-CAREER-005, BR-005                   |
|TC-MP-020|경력 검토 — 운영자 반려 (PENDING → REJECTED)     |P0 |Unit, Component|Yes  |UC-CAREER-005, BR-005                   |
|TC-MP-021|경력 검토 — 이미 APPROVED 상태 항목 검토 시도         |P2 |Unit, Component|Yes  |VR:REVIEW_TARGET_NOT_PENDING            |
|TC-MP-022|경력 검토 — ACCOUNT_MANAGEMENT 권한 없는 운영자    |P0 |Unit, Component|Yes  |BR-005                                  |
|TC-MP-023|분야 등록 요청 성공 → PENDING 생성                |P0 |Unit, Component|Yes  |UC-MF-001, BR-006, BR-007               |
|TC-MP-024|분야 등록 — fieldId 누락                      |P2 |Unit, Component|Yes  |VR:FIELD_REQUIRED                       |
|TC-MP-025|분야 등록 — INACTIVE 분야 지정                  |P2 |Unit, Component|Yes  |BR-006, VR:FIELD_NOT_FOUND_OR_INACTIVE  |
|TC-MP-026|분야 등록 — 존재하지 않는 fieldId                 |P2 |Component      |Yes  |VR:FIELD_NOT_FOUND_OR_INACTIVE          |
|TC-MP-027|분야 등록 — PENDING 상태 동일 분야 중복 요청          |P0 |Unit, Component|Yes  |BR-007, VR:FIELD_REGISTRATION_DUPLICATE |
|TC-MP-028|분야 등록 — APPROVED 상태 동일 분야 중복 요청         |P0 |Unit, Component|Yes  |BR-007, VR:FIELD_REGISTRATION_DUPLICATE |
|TC-MP-029|분야 등록 — REJECTED 분야 재등록 허용 여부           |P2 |Unit           |Yes  |BR-007 (OQ-002 의존)                      |
|TC-MP-030|분야 수정 성공 (PENDING)                      |P1 |Unit, Component|Yes  |UC-MF-002, BR-008                       |
|TC-MP-031|분야 수정 — APPROVED 상태에서 시도                |P2 |Unit, Component|Yes  |BR-008, VR:MODIFICATION_NOT_ALLOWED     |
|TC-MP-032|분야 수정 — 타인 항목 시도                        |P0 |Component      |Yes  |BR-012                                  |
|TC-MP-033|분야 철회 성공 (PENDING → WITHDRAWN)          |P1 |Unit, Component|Yes  |UC-MF-003, BR-009                       |
|TC-MP-034|분야 철회 — APPROVED 상태에서 시도                |P2 |Unit           |Yes  |BR-009                                  |
|TC-MP-035|분야 노출 설정 — APPROVED → visible=false     |P1 |Unit, Component|Yes  |UC-MF-004, BR-010                       |
|TC-MP-036|분야 노출 설정 — PENDING 상태에서 시도              |P2 |Unit, Component|Yes  |BR-010, VR:VISIBILITY_CHANGE_NOT_ALLOWED|
|TC-MP-037|분야 검토 — 운영자 승인 (PENDING → APPROVED)     |P0 |Unit, Component|Yes  |UC-MF-005, BR-011                       |
|TC-MP-038|분야 검토 — 운영자 반려 (PENDING → REJECTED)     |P0 |Unit, Component|Yes  |UC-MF-005, BR-011                       |
|TC-MP-039|분야 검토 — ACCOUNT_MANAGEMENT 권한 없는 운영자    |P0 |Unit, Component|Yes  |BR-011                                  |
|TC-MP-040|분야 검토 — PENDING이 아닌 항목 검토 시도            |P2 |Unit, Component|Yes  |VR:REVIEW_TARGET_NOT_PENDING            |
|TC-MP-041|승인 후 visible 기본값 확인 (true)              |P1 |Unit           |Yes  |BR-011                                  |

-----

## 테스트 케이스

### [TC-MP-001] 경력 등록 요청 성공 → PENDING 생성

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MentorProfile.addCareer()` 도메인 메서드, CareerEntry 초기 상태 검증
  - Component — `POST /mentor-profiles/me/careers` API (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MentorCareerService.register()`, `CareerEntry` 생성
- **연관 요건**: UC-CAREER-001, BR-001

#### 조건

- role=MENTOR 계정으로 인증
- 해당 멘토의 MentorProfile 존재

#### 입력

```json
{ "content": "카카오 백엔드 개발자 3년. Java/Spring 기반 서비스 개발 및 운영 경험." }
```

#### 기대 결과

- HTTP 201 Created
- 응답: `{ "id": any, "content": "카카오 백엔드...", "status": "PENDING", "visible": null }`
- 부수 효과: CareerEntry 1건 저장, status=PENDING

-----

### [TC-MP-002] 경력 등록 — content 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:CAREER_CONTENT_REQUIRED
- **연관 요건**: VR:CAREER_CONTENT_REQUIRED

#### 조건

- role=MENTOR 계정

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "CAREER_CONTENT_REQUIRED", "status": 400 }`

-----

### [TC-MP-003] 경력 등록 — content 공백 문자열

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:CAREER_CONTENT_REQUIRED (Not Blank)
- **연관 요건**: VR:CAREER_CONTENT_REQUIRED

#### 입력

```json
{ "content": "   " }
```

#### 기대 결과

- 도메인 레벨 예외 발생 (CAREER_CONTENT_REQUIRED)

-----

### [TC-MP-004] 경력 등록 — content 2000자 초과

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:CAREER_CONTENT_TOO_LONG

#### 입력

- content: 2001자 문자열

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "CAREER_CONTENT_TOO_LONG", "status": 400 }`

-----

### [TC-MP-005] 경력 등록 — content 정확히 2000자 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:CAREER_CONTENT_TOO_LONG 경계

#### 입력

- content: 정확히 2000자

#### 기대 결과

- 도메인 객체 생성 성공 (예외 없음)

-----

### [TC-MP-006] 경력 등록 — 멘티 계정으로 시도

- **중요도**: P0
- **계층**: Component
  - Component — `@WebMvcTest` 인가 검증
- **루프 포함**: Yes
- **대상**: 멘토 전용 인가 필터
- **연관 요건**: UC-CAREER-001

#### 조건

- role=MENTEE 계정으로 인증

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: CareerEntry 저장 없음

-----

### [TC-MP-007] 경력 수정 성공 (PENDING)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `CareerEntry.updateContent()` 상태 조건 검증
  - Component — `PATCH /mentor-profiles/me/careers/{careerId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MentorCareerService.modify()`, BR-002
- **연관 요건**: UC-CAREER-002, BR-002

#### 조건

- 본인 멘토의 CareerEntry id=1, status=PENDING

#### 입력

- Path: `/mentor-profiles/me/careers/1`

```json
{ "content": "수정된 경력 내용" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 1, "content": "수정된 경력 내용", "status": "PENDING" }`

-----

### [TC-MP-008] 경력 수정 — APPROVED 상태에서 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-002, VR:MODIFICATION_NOT_ALLOWED
- **연관 요건**: BR-002

#### 조건

- CareerEntry id=1, status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MODIFICATION_NOT_ALLOWED", "status": 422 }`
- 부수 효과: content 변경 없음

-----

### [TC-MP-009] 경력 수정 — REJECTED 상태에서 시도

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-002
- **연관 요건**: BR-002

#### 조건

- CareerEntry status=REJECTED

#### 기대 결과

- 도메인 레벨 예외 발생 (MODIFICATION_NOT_ALLOWED)

-----

### [TC-MP-010] 경력 수정 — WITHDRAWN 상태에서 시도

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-002
- **연관 요건**: BR-002

#### 조건

- CareerEntry status=WITHDRAWN

#### 기대 결과

- 도메인 레벨 예외 발생 (MODIFICATION_NOT_ALLOWED)

-----

### [TC-MP-011] 경력 수정 — 타인 항목 수정 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 소유권 검증 로직
  - Component — `@WebMvcTest` 403 반환 검증
- **루프 포함**: Yes
- **대상**: BR-012
- **연관 요건**: BR-012

#### 조건

- 멘토 A의 CareerEntry id=1 (PENDING)
- 멘토 B로 인증하여 수정 시도

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: content 변경 없음

-----

### [TC-MP-012] 경력 철회 성공 (PENDING → WITHDRAWN)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `CareerEntry.withdraw()` 상태 전이 검증
  - Component — `DELETE /mentor-profiles/me/careers/{careerId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MentorCareerService.withdraw()`, BR-003
- **연관 요건**: UC-CAREER-003, BR-003

#### 조건

- 본인 CareerEntry id=1, status=PENDING

#### 기대 결과

- HTTP 204 No Content
- 부수 효과: CareerEntry status=WITHDRAWN

-----

### [TC-MP-013] 경력 철회 — APPROVED 상태에서 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-003, VR:WITHDRAWAL_NOT_ALLOWED

#### 조건

- CareerEntry id=1, status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "WITHDRAWAL_NOT_ALLOWED", "status": 422 }`

-----

### [TC-MP-014] 경력 철회 — 타인 항목 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-012

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MP-015] 경력 노출 설정 — APPROVED → visible=false

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `CareerEntry.updateVisibility()` 상태 조건 + 값 변경
  - Component — `PATCH /mentor-profiles/me/careers/{careerId}/visibility`
- **루프 포함**: Yes
- **대상**: BR-004
- **연관 요건**: UC-CAREER-004, BR-004

#### 조건

- CareerEntry id=1, status=APPROVED, visible=true

#### 입력

```json
{ "visible": false }
```

#### 기대 결과

- HTTP 200 OK
- 부수 효과: visible=false로 변경

-----

### [TC-MP-016] 경력 노출 설정 — APPROVED → visible=true (재노출)

- **중요도**: P1
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-004 양방향 토글

#### 조건

- CareerEntry status=APPROVED, visible=false

#### 입력

```json
{ "visible": true }
```

#### 기대 결과

- visible=true로 변경 (예외 없음)

-----

### [TC-MP-017] 경력 노출 설정 — PENDING 상태에서 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-004, VR:VISIBILITY_CHANGE_NOT_ALLOWED

#### 조건

- CareerEntry status=PENDING

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "VISIBILITY_CHANGE_NOT_ALLOWED", "status": 422 }`

-----

### [TC-MP-018] 경력 노출 설정 — 타인 항목 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-012

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MP-019] 경력 검토 — 운영자 승인 (PENDING → APPROVED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `CareerEntry.approve()` 상태 전이 검증
  - Component — `POST /admin/careers/{careerId}/review` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MentorCareerService.review()`, BR-005
- **연관 요건**: UC-CAREER-005, BR-005

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- CareerEntry id=1, status=PENDING

#### 입력

```json
{ "action": "APPROVE" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 1, "status": "APPROVED" }`
- 부수 효과: status=APPROVED, visible=true (기본값)

-----

### [TC-MP-020] 경력 검토 — 운영자 반려 (PENDING → REJECTED)

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-005
- **연관 요건**: UC-CAREER-005, BR-005

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- CareerEntry id=1, status=PENDING

#### 입력

```json
{ "action": "REJECT" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 1, "status": "REJECTED" }`

-----

### [TC-MP-021] 경력 검토 — 이미 APPROVED 상태 항목 검토 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:REVIEW_TARGET_NOT_PENDING

#### 조건

- CareerEntry status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "REVIEW_TARGET_NOT_PENDING", "status": 422 }`

-----

### [TC-MP-022] 경력 검토 — ACCOUNT_MANAGEMENT 권한 없는 운영자

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 권한 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-005

#### 조건

- MATCHING_MANAGEMENT 권한만 보유한 운영자

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: CareerEntry 상태 변경 없음

-----

### [TC-MP-023] 분야 등록 요청 성공 → PENDING 생성

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MentorProfile.addFieldRegistration()` 도메인 검증 및 PENDING 상태 초기화
  - Component — `POST /mentor-profiles/me/field-registrations` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MentorFieldRegistrationService.register()`, BR-006, BR-007
- **연관 요건**: UC-MF-001, BR-006, BR-007

#### 조건

- role=MENTOR 계정
- fieldId=10인 ACTIVE Field 존재
- 동일 멘토의 fieldId=10 PENDING/APPROVED 항목 없음

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 201 Created
- 응답: `{ "id": any, "fieldId": 10, "fieldName": "백엔드", "status": "PENDING", "visible": null }`
- 부수 효과: FieldRegistration 저장, status=PENDING

-----

### [TC-MP-024] 분야 등록 — fieldId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_REQUIRED

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_REQUIRED", "status": 400 }`

-----

### [TC-MP-025] 분야 등록 — INACTIVE 분야 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-006, VR:FIELD_NOT_FOUND_OR_INACTIVE

#### 조건

- fieldId=10인 Field status=INACTIVE

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "FIELD_NOT_FOUND_OR_INACTIVE", "status": 422 }`

-----

### [TC-MP-026] 분야 등록 — 존재하지 않는 fieldId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_NOT_FOUND_OR_INACTIVE

#### 입력

```json
{ "fieldId": 9999 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "FIELD_NOT_FOUND_OR_INACTIVE", "status": 422 }`

-----

### [TC-MP-027] 분야 등록 — PENDING 상태 동일 분야 중복 요청

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 중복 체크 도메인 로직
  - Component — `@SpringBootTest` 실제 DB 중복 확인
- **루프 포함**: Yes
- **대상**: BR-007, VR:FIELD_REGISTRATION_DUPLICATE
- **연관 요건**: BR-007

#### 조건

- 동일 멘토에 fieldId=10, status=PENDING인 FieldRegistration 이미 존재

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "FIELD_REGISTRATION_DUPLICATE", "status": 409 }`
- 부수 효과: FieldRegistration 신규 저장 없음

-----

### [TC-MP-028] 분야 등록 — APPROVED 상태 동일 분야 중복 요청

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-007, VR:FIELD_REGISTRATION_DUPLICATE

#### 조건

- 동일 멘토에 fieldId=10, status=APPROVED인 FieldRegistration 이미 존재

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "FIELD_REGISTRATION_DUPLICATE", "status": 409 }`

-----

### [TC-MP-029] 분야 등록 — REJECTED 분야 재등록 허용 여부

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-007 (OQ-002 의존)
- **연관 요건**: BR-007
- **⚠️ 주의**: OQ-002 미확정. 현재 선택지 A(재등록 허용) 기준으로 작성. OQ-002 결정 후 기대 결과 수정 필요.

#### 조건

- 동일 멘토에 fieldId=10, status=REJECTED인 FieldRegistration 존재

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과 (OQ-002 선택지 A 기준)

- HTTP 201 Created (재등록 허용)

#### 기대 결과 (OQ-002 선택지 B 기준)

- HTTP 409 Conflict, `FIELD_REGISTRATION_DUPLICATE`

-----

### [TC-MP-030] 분야 수정 성공 (PENDING)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `FieldRegistration.updateField()` 상태 조건 + 필드 변경
  - Component — `PATCH /mentor-profiles/me/field-registrations/{registrationId}`
- **루프 포함**: Yes
- **대상**: `MentorFieldRegistrationService.modify()`, BR-008

#### 조건

- FieldRegistration id=5, status=PENDING, fieldId=10
- 변경할 fieldId=11 ACTIVE 존재
- 멘토에게 fieldId=11 PENDING/APPROVED 항목 없음

#### 입력

```json
{ "fieldId": 11 }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 5, "fieldId": 11, "status": "PENDING" }`

-----

### [TC-MP-031] 분야 수정 — APPROVED 상태에서 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-008, VR:MODIFICATION_NOT_ALLOWED

#### 조건

- FieldRegistration status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MODIFICATION_NOT_ALLOWED", "status": 422 }`

-----

### [TC-MP-032] 분야 수정 — 타인 항목 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-012

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MP-033] 분야 철회 성공 (PENDING → WITHDRAWN)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `FieldRegistration.withdraw()` 상태 전이
  - Component — `DELETE /mentor-profiles/me/field-registrations/{registrationId}`
- **루프 포함**: Yes
- **대상**: BR-009

#### 조건

- FieldRegistration id=5, status=PENDING

#### 기대 결과

- HTTP 204 No Content
- 부수 효과: status=WITHDRAWN

-----

### [TC-MP-034] 분야 철회 — APPROVED 상태에서 시도

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-009

#### 기대 결과

- 도메인 레벨 예외 (WITHDRAWAL_NOT_ALLOWED)

-----

### [TC-MP-035] 분야 노출 설정 — APPROVED → visible=false

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `FieldRegistration.updateVisibility()` 상태 조건 검증
  - Component — `PATCH /mentor-profiles/me/field-registrations/{registrationId}/visibility`
- **루프 포함**: Yes
- **대상**: BR-010

#### 조건

- FieldRegistration status=APPROVED, visible=true

#### 입력

```json
{ "visible": false }
```

#### 기대 결과

- HTTP 200 OK
- 부수 효과: visible=false

-----

### [TC-MP-036] 분야 노출 설정 — PENDING 상태에서 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-010, VR:VISIBILITY_CHANGE_NOT_ALLOWED

#### 조건

- FieldRegistration status=PENDING

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "VISIBILITY_CHANGE_NOT_ALLOWED", "status": 422 }`

-----

### [TC-MP-037] 분야 검토 — 운영자 승인 (PENDING → APPROVED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `FieldRegistration.approve()` 상태 전이 + visible 기본값
  - Component — `POST /admin/field-registrations/{registrationId}/review`
- **루프 포함**: Yes
- **대상**: BR-011
- **연관 요건**: UC-MF-005, BR-011

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- FieldRegistration id=5, status=PENDING

#### 입력

```json
{ "action": "APPROVE" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 5, "status": "APPROVED" }`
- 부수 효과: status=APPROVED, visible=true (기본값)

-----

### [TC-MP-038] 분야 검토 — 운영자 반려 (PENDING → REJECTED)

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-011

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- FieldRegistration id=5, status=PENDING

#### 입력

```json
{ "action": "REJECT" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 5, "status": "REJECTED" }`

-----

### [TC-MP-039] 분야 검토 — ACCOUNT_MANAGEMENT 권한 없는 운영자

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-011

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: FieldRegistration 상태 변경 없음

-----

### [TC-MP-040] 분야 검토 — PENDING이 아닌 항목 검토 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:REVIEW_TARGET_NOT_PENDING

#### 조건

- FieldRegistration status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "REVIEW_TARGET_NOT_PENDING", "status": 422 }`

-----

### [TC-MP-041] 승인 후 visible 기본값 확인 (true)

- **중요도**: P1
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-011 — 승인 시 visible 초기화 정책
- **연관 요건**: BR-011

#### 조건

- CareerEntry 또는 FieldRegistration PENDING 상태에서 APPROVE 전이

#### 기대 결과

- 전이 직후 visible=true
- Unit 수준에서 `approve()` 메서드 반환 객체의 visible 필드 검증

-----

## 커버리지 매트릭스

|요건                              |커버 TC                                     |비고                    |
|--------------------------------|------------------------------------------|----------------------|
|UC-CAREER-001                   |TC-MP-001~006                             |—                     |
|UC-CAREER-002                   |TC-MP-007~011                             |—                     |
|UC-CAREER-003                   |TC-MP-012~014                             |—                     |
|UC-CAREER-004                   |TC-MP-015~018                             |—                     |
|UC-CAREER-005                   |TC-MP-019~022                             |—                     |
|UC-MF-001                       |TC-MP-023~029                             |OQ-002 의존 TC-MP-029 주의|
|UC-MF-002                       |TC-MP-030~032                             |—                     |
|UC-MF-003                       |TC-MP-033~034                             |—                     |
|UC-MF-004                       |TC-MP-035~036                             |—                     |
|UC-MF-005                       |TC-MP-037~040                             |—                     |
|BR-001                          |TC-MP-001, TC-MP-006                      |P0 — Unit+Component ✅ |
|BR-002                          |TC-MP-007~010                             |PENDING만 수정 허용 전 상태 검증|
|BR-003                          |TC-MP-012~013                             |—                     |
|BR-004                          |TC-MP-015~017                             |—                     |
|BR-005                          |TC-MP-019~022                             |P0 — Unit+Component ✅ |
|BR-006                          |TC-MP-023, TC-MP-025                      |—                     |
|BR-007                          |TC-MP-027~029                             |P0 — Unit+Component ✅ |
|BR-008                          |TC-MP-030~031                             |—                     |
|BR-009                          |TC-MP-033~034                             |—                     |
|BR-010                          |TC-MP-035~036                             |—                     |
|BR-011                          |TC-MP-037~041                             |P0 — Unit+Component ✅ |
|BR-012                          |TC-MP-011, TC-MP-014, TC-MP-018, TC-MP-032|P0 — Unit+Component ✅ |
|VR:CAREER_CONTENT_REQUIRED      |TC-MP-002, TC-MP-003                      |—                     |
|VR:CAREER_CONTENT_TOO_LONG      |TC-MP-004, TC-MP-005                      |—                     |
|VR:FIELD_REQUIRED               |TC-MP-024                                 |—                     |
|VR:FIELD_NOT_FOUND_OR_INACTIVE  |TC-MP-025, TC-MP-026                      |—                     |
|VR:FIELD_REGISTRATION_DUPLICATE |TC-MP-027, TC-MP-028                      |—                     |
|VR:MODIFICATION_NOT_ALLOWED     |TC-MP-008, TC-MP-031                      |—                     |
|VR:WITHDRAWAL_NOT_ALLOWED       |TC-MP-013                                 |—                     |
|VR:VISIBILITY_CHANGE_NOT_ALLOWED|TC-MP-017, TC-MP-036                      |—                     |
|VR:REVIEW_TARGET_NOT_PENDING    |TC-MP-021, TC-MP-040                      |—                     |
|VR:REVIEW_ACTION_REQUIRED       |—                                         |TC-MP-019/037에서 암묵 커버 |

-----

## 루프 외 케이스 목록

해당 없음. 전 케이스 Unit 또는 Component 내 검증 가능.

## ⚠️ OQ 의존 케이스

|TC-ID    |의존 OQ |현재 가정                  |확정 후 조치                     |
|---------|------|-----------------------|----------------------------|
|TC-MP-029|OQ-002|선택지 A (REJECTED 재등록 허용)|OQ-002 결정 후 기대 결과 및 구현 로직 확정|