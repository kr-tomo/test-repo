# TEST_DOC: 인스턴스 Q&A (InstantQA)

연관 SPEC: SPEC-03-instant-qa.md (v0.1)
작성일: 2026-06-16
개발 루프 대상 레벨: Unit, Component

-----

## 케이스 요약

|TC-ID    |이름                                        |중요도|계층             |루프 포함|연관 요건                                 |
|---------|------------------------------------------|---|---------------|-----|--------------------------------------|
|TC-QA-001|Q&A 등록 성공                                 |P0 |Unit, Component|Yes  |UC-QA-001, BR-001                     |
|TC-QA-002|Q&A 등록 — content 누락                       |P2 |Unit, Component|Yes  |VR:QA_CONTENT_REQUIRED                |
|TC-QA-003|Q&A 등록 — content 공백                       |P2 |Unit           |Yes  |VR:QA_CONTENT_REQUIRED                |
|TC-QA-004|Q&A 등록 — content 1000자 초과                 |P2 |Unit, Component|Yes  |VR:QA_CONTENT_TOO_LONG                |
|TC-QA-005|Q&A 등록 — content 정확히 1000자 (경계값)          |P3 |Unit           |Yes  |VR:QA_CONTENT_TOO_LONG                |
|TC-QA-006|Q&A 등록 — fieldId 누락                       |P2 |Unit, Component|Yes  |VR:FIELD_REQUIRED                     |
|TC-QA-007|Q&A 등록 — INACTIVE 분야 지정                   |P2 |Unit, Component|Yes  |BR-001, VR:FIELD_NOT_FOUND_OR_INACTIVE|
|TC-QA-008|Q&A 등록 — 존재하지 않는 fieldId                  |P2 |Component      |Yes  |VR:FIELD_NOT_FOUND_OR_INACTIVE        |
|TC-QA-009|Q&A 등록 — 멘토 계정으로 시도                       |P0 |Component      |Yes  |UC-QA-001                             |
|TC-QA-010|멘토 Q&A 목록 조회 — APPROVED·visible=true 분야 필터|P0 |Unit, Component|Yes  |UC-QA-002, BR-002                     |
|TC-QA-011|멘토 Q&A 목록 조회 — PENDING 분야는 노출 안됨          |P0 |Unit, Component|Yes  |BR-002                                |
|TC-QA-012|멘토 Q&A 목록 조회 — visible=false 분야는 노출 안됨    |P0 |Unit, Component|Yes  |BR-002                                |
|TC-QA-013|멘토 Q&A 목록 조회 — 최신 게재 순 정렬                 |P1 |Component      |Yes  |BR-002                                |
|TC-QA-014|멘토 Q&A 목록 조회 — 조회 가능 분야 없을 때 빈 목록         |P3 |Component      |Yes  |BR-002                                |
|TC-QA-015|멘토 Q&A 목록 조회 — 멘티 계정으로 시도                 |P0 |Component      |Yes  |UC-QA-002                             |
|TC-QA-016|Q&A 답변 작성 성공                              |P0 |Unit, Component|Yes  |UC-QA-003, BR-003                     |
|TC-QA-017|Q&A 답변 작성 — content 누락                    |P2 |Unit, Component|Yes  |VR:ANSWER_CONTENT_REQUIRED            |
|TC-QA-018|Q&A 답변 작성 — content 2000자 초과              |P2 |Unit, Component|Yes  |VR:ANSWER_CONTENT_TOO_LONG            |
|TC-QA-019|Q&A 답변 작성 — content 정확히 2000자 (경계값)       |P3 |Unit           |Yes  |VR:ANSWER_CONTENT_TOO_LONG            |
|TC-QA-020|Q&A 답변 작성 — 동일 Q&A 재답변 시도                 |P0 |Unit, Component|Yes  |BR-003, VR:ANSWER_ALREADY_EXISTS      |
|TC-QA-021|Q&A 답변 작성 — 멘티 계정으로 시도                    |P0 |Component      |Yes  |UC-QA-003                             |
|TC-QA-022|Q&A 답변 작성 — 다른 멘토는 동일 Q&A에 각각 답변 가능       |P1 |Unit           |Yes  |BR-003                                |
|TC-QA-023|Q&A 답변 수정 성공 (본인 답변)                      |P1 |Unit, Component|Yes  |UC-QA-004, BR-004                     |
|TC-QA-024|Q&A 답변 수정 — content 누락                    |P2 |Unit           |Yes  |VR:ANSWER_CONTENT_REQUIRED            |
|TC-QA-025|Q&A 답변 수정 — 타인 답변 수정 시도                   |P0 |Unit, Component|Yes  |BR-004                                |
|TC-QA-026|Q&A 답변 수정 — 존재하지 않는 answerId              |P2 |Component      |Yes  |—                                     |
|TC-QA-027|멘티 Q&A 답변 확인 — 본인 Q&A 답변 목록 조회            |P0 |Component      |Yes  |UC-QA-005, BR-005                     |
|TC-QA-028|멘티 Q&A 답변 확인 — 타인 Q&A 답변 조회 시도            |P0 |Component      |Yes  |BR-005                                |
|TC-QA-029|멘티 Q&A 목록 — 본인 게재 Q&A 목록 조회               |P1 |Component      |Yes  |UC-QA-005                             |
|TC-QA-030|Repository — 분야 기반 Q&A 조회 쿼리 (복수 분야)      |P0 |Component      |Yes  |BR-002                                |

-----

## 테스트 케이스

### [TC-QA-001] Q&A 등록 성공

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `InstantQA.create()` 도메인 객체 생성 검증
  - Component — `POST /instant-qas` API (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `InstantQAService.post()`, BR-001
- **연관 요건**: UC-QA-001, BR-001

#### 조건

- role=MENTEE 계정으로 인증
- fieldId=10 ACTIVE Field 존재

#### 입력

```json
{
  "fieldId": 10,
  "content": "Spring Boot에서 멀티 모듈 구성 시 의존성 관리는 어떻게 하시나요?"
}
```

#### 기대 결과

- HTTP 201 Created
- 응답: `{ "id": any, "fieldId": 10, "fieldName": "백엔드", "content": "Spring Boot...", "postedAt": any }`
- 부수 효과: InstantQA 1건 저장

-----

### [TC-QA-002] Q&A 등록 — content 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:QA_CONTENT_REQUIRED

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "QA_CONTENT_REQUIRED", "status": 400 }`

-----

### [TC-QA-003] Q&A 등록 — content 공백 문자열

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:QA_CONTENT_REQUIRED (Not Blank)

#### 입력

```json
{ "fieldId": 10, "content": "   " }
```

#### 기대 결과

- 도메인 레벨 예외 (QA_CONTENT_REQUIRED)

-----

### [TC-QA-004] Q&A 등록 — content 1000자 초과

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:QA_CONTENT_TOO_LONG

#### 입력

- content: 1001자 문자열

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "QA_CONTENT_TOO_LONG", "status": 400 }`

-----

### [TC-QA-005] Q&A 등록 — content 정확히 1000자 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:QA_CONTENT_TOO_LONG 경계

#### 입력

- content: 정확히 1000자

#### 기대 결과

- 도메인 객체 생성 성공

-----

### [TC-QA-006] Q&A 등록 — fieldId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_REQUIRED

#### 입력

```json
{ "content": "질문 내용" }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_REQUIRED", "status": 400 }`

-----

### [TC-QA-007] Q&A 등록 — INACTIVE 분야 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-001, VR:FIELD_NOT_FOUND_OR_INACTIVE

#### 조건

- fieldId=10인 Field status=INACTIVE

#### 입력

```json
{ "fieldId": 10, "content": "질문 내용" }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "FIELD_NOT_FOUND_OR_INACTIVE", "status": 422 }`

-----

### [TC-QA-008] Q&A 등록 — 존재하지 않는 fieldId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_NOT_FOUND_OR_INACTIVE

#### 입력

```json
{ "fieldId": 9999, "content": "질문 내용" }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "FIELD_NOT_FOUND_OR_INACTIVE", "status": 422 }`

-----

### [TC-QA-009] Q&A 등록 — 멘토 계정으로 시도

- **중요도**: P0
- **계층**: Component
  - Component — `@WebMvcTest` 인가 필터 검증
- **루프 포함**: Yes
- **대상**: 멘티 전용 인가

#### 조건

- role=MENTOR 계정으로 인증

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: InstantQA 저장 없음

-----

### [TC-QA-010] 멘토 Q&A 목록 조회 — APPROVED·visible=true 분야 필터

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `InstantQAService.findForMentor()` 분야 필터 로직
  - Component — `GET /instant-qas` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: BR-002
- **연관 요건**: UC-QA-002, BR-002

#### 조건

- 멘토의 FieldRegistration: fieldId=10 (APPROVED, visible=true), fieldId=11 (APPROVED, visible=true)
- InstantQA: id=100 (fieldId=10), id=101 (fieldId=11), id=102 (fieldId=20, 다른 분야)

#### 기대 결과

- HTTP 200 OK
- 반환 목록에 id=100, id=101 포함, id=102 미포함

-----

### [TC-QA-011] 멘토 Q&A 목록 조회 — PENDING 분야는 노출 안됨

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-002 — PENDING 분야 제외 조건

#### 조건

- 멘토의 FieldRegistration: fieldId=10 (PENDING)
- InstantQA: id=100 (fieldId=10)

#### 기대 결과

- 반환 목록에 id=100 미포함 (빈 목록)

-----

### [TC-QA-012] 멘토 Q&A 목록 조회 — visible=false 분야는 노출 안됨

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-002 — visible=false 분야 제외 조건

#### 조건

- 멘토의 FieldRegistration: fieldId=10 (APPROVED, visible=false)
- InstantQA: id=100 (fieldId=10)

#### 기대 결과

- 반환 목록에 id=100 미포함

-----

### [TC-QA-013] 멘토 Q&A 목록 조회 — 최신 게재 순 정렬

- **중요도**: P1
- **계층**: Component
  - Component — `@SpringBootTest` postedAt 정렬 검증
- **루프 포함**: Yes
- **대상**: BR-002 정렬 조건

#### 조건

- InstantQA: id=100 (postedAt=10:00), id=101 (postedAt=11:00), id=102 (postedAt=09:00) — 모두 조회 가능 분야

#### 기대 결과

- 반환 순서: id=101 → id=100 → id=102 (최신 게재 순)

-----

### [TC-QA-014] 멘토 Q&A 목록 조회 — 조회 가능 분야 없을 때 빈 목록

- **중요도**: P3
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-002 빈 상태

#### 조건

- 멘토에 APPROVED·visible=true FieldRegistration 없음

#### 기대 결과

- HTTP 200 OK
- `{ "content": [], "totalElements": 0 }`

-----

### [TC-QA-015] 멘토 Q&A 목록 조회 — 멘티 계정으로 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘토 전용 인가

#### 조건

- role=MENTEE 계정

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-QA-016] Q&A 답변 작성 성공

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `InstantQA.addAnswer()` 도메인 메서드, QAAnswer 생성
  - Component — `POST /instant-qas/{qaId}/answers` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `QAAnswerService.answer()`, BR-003
- **연관 요건**: UC-QA-003, BR-003

#### 조건

- role=MENTOR 계정 (멘토 A)
- InstantQA id=100 존재
- 멘토 A의 동일 Q&A 답변 없음

#### 입력

```json
{ "content": "gradle multi-project 기준으로 설명드리면..." }
```

#### 기대 결과

- HTTP 201 Created
- 응답: `{ "id": any, "instantQAId": 100, "content": "gradle...", "answeredAt": any }`
- 부수 효과: QAAnswer 1건 저장

-----

### [TC-QA-017] Q&A 답변 작성 — content 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:ANSWER_CONTENT_REQUIRED

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "ANSWER_CONTENT_REQUIRED", "status": 400 }`

-----

### [TC-QA-018] Q&A 답변 작성 — content 2000자 초과

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:ANSWER_CONTENT_TOO_LONG

#### 입력

- content: 2001자 문자열

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "ANSWER_CONTENT_TOO_LONG", "status": 400 }`

-----

### [TC-QA-019] Q&A 답변 작성 — content 정확히 2000자 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:ANSWER_CONTENT_TOO_LONG 경계

#### 입력

- content: 정확히 2000자

#### 기대 결과

- 도메인 객체 생성 성공

-----

### [TC-QA-020] Q&A 답변 작성 — 동일 Q&A 재답변 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `InstantQA.addAnswer()` 중복 체크 로직
  - Component — `@SpringBootTest` UK 제약 검증
- **루프 포함**: Yes
- **대상**: BR-003, VR:ANSWER_ALREADY_EXISTS
- **연관 요건**: BR-003

#### 조건

- 멘토 A가 InstantQA id=100에 이미 답변 존재

#### 입력

```json
{ "content": "두 번째 답변 시도" }
```

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "ANSWER_ALREADY_EXISTS", "status": 409 }`
- 부수 효과: 기존 답변 유지, 신규 저장 없음

-----

### [TC-QA-021] Q&A 답변 작성 — 멘티 계정으로 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘토 전용 인가

#### 조건

- role=MENTEE 계정

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-QA-022] Q&A 답변 작성 — 다른 멘토는 동일 Q&A에 각각 답변 가능

- **중요도**: P1
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-003 — “멘토당” 1회 제한 범위 확인

#### 조건

- 멘토 A가 InstantQA id=100에 이미 답변
- 멘토 B가 같은 id=100에 답변 시도

#### 기대 결과

- 멘토 B의 답변 생성 성공 (중복 아님)

-----

### [TC-QA-023] Q&A 답변 수정 성공 (본인 답변)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `QAAnswer.updateContent()` 본인 소유 검증 + 내용 변경
  - Component — `PATCH /instant-qas/{qaId}/answers/{answerId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `QAAnswerService.update()`, BR-004
- **연관 요건**: UC-QA-004, BR-004

#### 조건

- 멘토 A가 InstantQA id=100에 작성한 QAAnswer id=50 존재

#### 입력

```json
{ "content": "수정된 답변 내용" }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 50, "content": "수정된 답변 내용", "updatedAt": any }`
- 부수 효과: QAAnswer content 변경, updatedAt 갱신

-----

### [TC-QA-024] Q&A 답변 수정 — content 누락

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:ANSWER_CONTENT_REQUIRED

#### 입력

```json
{}
```

#### 기대 결과

- 도메인 레벨 예외 (ANSWER_CONTENT_REQUIRED)

-----

### [TC-QA-025] Q&A 답변 수정 — 타인 답변 수정 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 소유권 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-004

#### 조건

- QAAnswer id=50은 멘토 A 작성
- 멘토 B로 인증하여 수정 시도

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: content 변경 없음

-----

### [TC-QA-026] Q&A 답변 수정 — 존재하지 않는 answerId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 404 처리

#### 조건

- answerId=9999 없음

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "QA_ANSWER_NOT_FOUND", "status": 404 }`

-----

### [TC-QA-027] 멘티 Q&A 답변 확인 — 본인 Q&A 답변 목록 조회

- **중요도**: P0
- **계층**: Component
  - Component — `GET /instant-qas/{qaId}/answers` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `InstantQAService.getAnswers()`, BR-005
- **연관 요건**: UC-QA-005, BR-005

#### 조건

- 멘티 A의 InstantQA id=100
- 멘토 B 답변 id=50, 멘토 C 답변 id=51

#### 기대 결과

- HTTP 200 OK
- 응답 배열에 id=50, id=51 포함

-----

### [TC-QA-028] 멘티 Q&A 답변 확인 — 타인 Q&A 답변 조회 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-005

#### 조건

- InstantQA id=100은 멘티 A 작성
- 멘티 B로 인증하여 `/instant-qas/100/answers` 조회 시도

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-QA-029] 멘티 Q&A 목록 — 본인 게재 Q&A 목록 조회

- **중요도**: P1
- **계층**: Component
  - Component — `GET /instant-qas/me` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `InstantQAService.findMyQAs()`
- **연관 요건**: UC-QA-005

#### 조건

- 멘티 A의 InstantQA id=100, id=101
- 멘티 B의 InstantQA id=102

#### 기대 결과

- HTTP 200 OK
- 반환 목록에 id=100, id=101 포함, id=102 미포함

-----

### [TC-QA-030] Repository — 분야 기반 Q&A 조회 쿼리 (복수 분야)

- **중요도**: P0
- **계층**: Component
  - Component — `@DataJpaTest` 쿼리 정확성 검증
- **루프 포함**: Yes
- **대상**: `InstantQARepositoryPort.findByFieldIds()` 쿼리
- **연관 요건**: BR-002

#### 조건

- InstantQA: fieldId=10 (3건), fieldId=11 (2건), fieldId=20 (1건)
- 조회 fieldId 목록: [10, 11]

#### 기대 결과

- 반환 5건 (fieldId=10 3건 + fieldId=11 2건)
- postedAt DESC 정렬 확인

-----

## 커버리지 매트릭스

|요건                            |커버 TC                          |비고                   |
|------------------------------|-------------------------------|---------------------|
|UC-QA-001                     |TC-QA-001~009                  |—                    |
|UC-QA-002                     |TC-QA-010~015                  |—                    |
|UC-QA-003                     |TC-QA-016~022                  |—                    |
|UC-QA-004                     |TC-QA-023~026                  |—                    |
|UC-QA-005                     |TC-QA-027~029                  |—                    |
|BR-001                        |TC-QA-001, TC-QA-007, TC-QA-009|P0 — Unit+Component ✅|
|BR-002                        |TC-QA-010~015, TC-QA-030       |P0 — Unit+Component ✅|
|BR-003                        |TC-QA-016, TC-QA-020, TC-QA-022|P0 — Unit+Component ✅|
|BR-004                        |TC-QA-023, TC-QA-025           |P0 — Unit+Component ✅|
|BR-005                        |TC-QA-027, TC-QA-028           |P0 — Component ✅     |
|VR:QA_CONTENT_REQUIRED        |TC-QA-002, TC-QA-003           |—                    |
|VR:QA_CONTENT_TOO_LONG        |TC-QA-004, TC-QA-005           |—                    |
|VR:FIELD_REQUIRED             |TC-QA-006                      |—                    |
|VR:FIELD_NOT_FOUND_OR_INACTIVE|TC-QA-007, TC-QA-008           |—                    |
|VR:ANSWER_CONTENT_REQUIRED    |TC-QA-017, TC-QA-024           |—                    |
|VR:ANSWER_CONTENT_TOO_LONG    |TC-QA-018, TC-QA-019           |—                    |
|VR:ANSWER_ALREADY_EXISTS      |TC-QA-020                      |—                    |

-----

## 루프 외 케이스 목록

해당 없음.