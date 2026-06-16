# TEST_DOC: 매칭 요청 (MatchRequest)

연관 SPEC: SPEC-04-match-request.md (v0.1)
작성일: 2026-06-16
개발 루프 대상 레벨: Unit, Component

-----

## 케이스 요약

|TC-ID    |이름                                                 |중요도|계층             |루프 포함|연관 요건                             |
|---------|---------------------------------------------------|---|---------------|-----|----------------------------------|
|TC-MR-001|분야 기반 매칭 요청 성공 (멘토 미지정)                            |P0 |Unit, Component|Yes  |UC-MATCH-001, BR-001              |
|TC-MR-002|특정 멘토 지정 매칭 요청 성공                                  |P0 |Unit, Component|Yes  |UC-MATCH-002, BR-002              |
|TC-MR-003|매칭 요청 — fieldId 누락                                 |P2 |Unit, Component|Yes  |VR:FIELD_REQUIRED                 |
|TC-MR-004|매칭 요청 — INACTIVE 분야 지정                             |P2 |Unit, Component|Yes  |VR:FIELD_NOT_FOUND_OR_INACTIVE    |
|TC-MR-005|매칭 요청 — 존재하지 않는 fieldId                            |P2 |Component      |Yes  |VR:FIELD_NOT_FOUND_OR_INACTIVE    |
|TC-MR-006|매칭 요청 — 멘토가 아닌 계정 지정                               |P2 |Unit, Component|Yes  |VR:NOT_A_MENTOR                   |
|TC-MR-007|매칭 요청 — 매칭 여력 없는 멘토 지정                             |P0 |Unit, Component|Yes  |BR-002, VR:MENTOR_NO_CAPACITY     |
|TC-MR-008|매칭 요청 — 멘토 계정으로 시도                                 |P0 |Component      |Yes  |UC-MATCH-001                      |
|TC-MR-009|매칭 요청 — 운영자 계정으로 시도                                |P0 |Component      |Yes  |UC-MATCH-001                      |
|TC-MR-010|운영자 멘토 지정 성공 (REQUESTED → MENTOR_ASSIGNED)         |P0 |Unit, Component|Yes  |UC-MATCH-003, BR-003              |
|TC-MR-011|운영자 멘토 지정 — mentorAccountId 누락                     |P2 |Unit, Component|Yes  |VR:MENTOR_REQUIRED                |
|TC-MR-012|운영자 멘토 지정 — 멘토가 아닌 계정 지정                           |P2 |Unit, Component|Yes  |VR:NOT_A_MENTOR                   |
|TC-MR-013|운영자 멘토 지정 — 매칭 여력 없는 멘토                            |P0 |Unit, Component|Yes  |BR-003, VR:MENTOR_NO_CAPACITY     |
|TC-MR-014|운영자 멘토 지정 — APPROVED 상태 요청에 시도                     |P2 |Unit, Component|Yes  |VR:MATCH_REQUEST_NOT_ASSIGNABLE   |
|TC-MR-015|운영자 멘토 지정 — CANCELLED 상태 요청에 시도                    |P2 |Unit           |Yes  |VR:MATCH_REQUEST_NOT_ASSIGNABLE   |
|TC-MR-016|운영자 멘토 지정 — MATCHING_MANAGEMENT 권한 없음              |P0 |Unit, Component|Yes  |BR-003                            |
|TC-MR-017|운영자 멘토 지정 — 존재하지 않는 requestId                      |P2 |Component      |Yes  |VR:MATCH_REQUEST_NOT_FOUND        |
|TC-MR-018|운영자 멘토 지정 — MENTOR_ASSIGNED 상태 재지정 (덮어쓰기)          |P3 |Unit, Component|Yes  |BR-003                            |
|TC-MR-019|매칭 승인 성공 — 멘티가 지정한 멘토 (REQUESTED → APPROVED)       |P0 |Unit, Component|Yes  |UC-MATCH-004, BR-004              |
|TC-MR-020|매칭 승인 성공 — 운영자가 지정한 멘토 (MENTOR_ASSIGNED → APPROVED)|P0 |Unit, Component|Yes  |UC-MATCH-004, BR-004              |
|TC-MR-021|매칭 승인 — 멘토 미지정 상태에서 시도                             |P0 |Unit, Component|Yes  |BR-004, VR:MENTOR_NOT_ASSIGNED    |
|TC-MR-022|매칭 승인 — 이미 APPROVED 상태                             |P2 |Unit, Component|Yes  |VR:MATCH_REQUEST_ALREADY_PROCESSED|
|TC-MR-023|매칭 승인 — CANCELLED 상태                               |P2 |Unit           |Yes  |VR:MATCH_REQUEST_ALREADY_PROCESSED|
|TC-MR-024|매칭 승인 — MATCHING_MANAGEMENT 권한 없음                  |P0 |Unit, Component|Yes  |BR-004                            |
|TC-MR-025|매칭 승인 — 존재하지 않는 requestId                          |P2 |Component      |Yes  |VR:MATCH_REQUEST_NOT_FOUND        |
|TC-MR-026|내 매칭 요청 목록 조회 — 본인 요청만 반환                          |P1 |Component      |Yes  |BR-005                            |
|TC-MR-027|내 매칭 요청 목록 조회 — 타인 요청 미포함 확인                       |P1 |Component      |Yes  |BR-005                            |

-----

## 테스트 케이스

### [TC-MR-001] 분야 기반 매칭 요청 성공 (멘토 미지정)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchRequest.create()` 도메인 객체 생성, mentorAccountId=null, status=REQUESTED 검증
  - Component — `POST /match-requests` API (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchRequestService.create()`, BR-001
- **연관 요건**: UC-MATCH-001, BR-001

#### 조건

- role=MENTEE 계정으로 인증
- fieldId=10 ACTIVE Field 존재
- mentorAccountId 미지정

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 201 Created
- 응답:

```json
{
  "id": any,
  "fieldId": 10,
  "fieldName": "백엔드",
  "mentorAccountId": null,
  "status": "REQUESTED",
  "requestedAt": any
}
```

- 부수 효과: MatchRequest 1건 저장, status=REQUESTED, mentorAccountId=null

-----

### [TC-MR-002] 특정 멘토 지정 매칭 요청 성공

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchRequest.create()` mentorAccountId 설정 + 여력 검증 로직
  - Component — `POST /match-requests` with mentorAccountId (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchRequestService.create()`, BR-002
- **연관 요건**: UC-MATCH-002, BR-002
- **⚠️ 주의**: OQ-003(매칭 여력 정의) 확정 후 여력 검증 구현 상세 결정 필요

#### 조건

- role=MENTEE 계정으로 인증
- fieldId=10 ACTIVE Field 존재
- mentorAccountId=7 (role=MENTOR, 매칭 여력 있음)

#### 입력

```json
{ "fieldId": 10, "mentorAccountId": 7 }
```

#### 기대 결과

- HTTP 201 Created
- 응답:

```json
{
  "id": any,
  "fieldId": 10,
  "mentorAccountId": 7,
  "status": "REQUESTED",
  "requestedAt": any
}
```

- 부수 효과: MatchRequest 저장, mentorAccountId=7, status=REQUESTED

-----

### [TC-MR-003] 매칭 요청 — fieldId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_REQUIRED
- **연관 요건**: VR:FIELD_REQUIRED

#### 조건

- role=MENTEE 계정

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_REQUIRED", "status": 400 }`

-----

### [TC-MR-004] 매칭 요청 — INACTIVE 분야 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:FIELD_NOT_FOUND_OR_INACTIVE
- **연관 요건**: VR:FIELD_NOT_FOUND_OR_INACTIVE

#### 조건

- fieldId=10 Field status=INACTIVE

#### 입력

```json
{ "fieldId": 10 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "FIELD_NOT_FOUND_OR_INACTIVE", "status": 422 }`

-----

### [TC-MR-005] 매칭 요청 — 존재하지 않는 fieldId

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

### [TC-MR-006] 매칭 요청 — 멘토가 아닌 계정 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:NOT_A_MENTOR
- **연관 요건**: VR:NOT_A_MENTOR

#### 조건

- mentorAccountId=20 (role=MENTEE 계정)

#### 입력

```json
{ "fieldId": 10, "mentorAccountId": 20 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "NOT_A_MENTOR", "status": 422 }`

-----

### [TC-MR-007] 매칭 요청 — 매칭 여력 없는 멘토 지정

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 여력 검증 도메인/서비스 로직 단위 검증
  - Component — `@WebMvcTest` 422 반환 검증
- **루프 포함**: Yes
- **대상**: BR-002, VR:MENTOR_NO_CAPACITY
- **연관 요건**: BR-002
- **⚠️ 주의**: OQ-003 확정 후 여력 판단 조건 구체화 필요

#### 조건

- mentorAccountId=7 (role=MENTOR, 매칭 여력 없음 상태)

#### 입력

```json
{ "fieldId": 10, "mentorAccountId": 7 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MENTOR_NO_CAPACITY", "status": 422 }`
- 부수 효과: MatchRequest 저장 없음

-----

### [TC-MR-008] 매칭 요청 — 멘토 계정으로 시도

- **중요도**: P0
- **계층**: Component
  - Component — `@WebMvcTest` 인가 필터 검증
- **루프 포함**: Yes
- **대상**: 멘티 전용 인가
- **연관 요건**: UC-MATCH-001

#### 조건

- role=MENTOR 계정으로 인증

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: MatchRequest 저장 없음

-----

### [TC-MR-009] 매칭 요청 — 운영자 계정으로 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘티 전용 인가

#### 조건

- role=OPERATOR 계정으로 인증

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MR-010] 운영자 멘토 지정 성공 (REQUESTED → MENTOR_ASSIGNED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchRequest.assignMentor()` 상태 전이 검증, mentorAccountId 설정
  - Component — `PATCH /admin/match-requests/{requestId}/mentor` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchRequestService.assignMentor()`, BR-003
- **연관 요건**: UC-MATCH-003, BR-003

#### 조건

- MATCHING_MANAGEMENT 권한 운영자
- MatchRequest id=200, status=REQUESTED, mentorAccountId=null
- 지정할 mentorAccountId=7 (role=MENTOR, 매칭 여력 있음)

#### 입력

- Path: `/admin/match-requests/200/mentor`

```json
{ "mentorAccountId": 7 }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 200, "mentorAccountId": 7, "status": "MENTOR_ASSIGNED" }`
- 부수 효과: MatchRequest status=MENTOR_ASSIGNED, mentorAccountId=7

-----

### [TC-MR-011] 운영자 멘토 지정 — mentorAccountId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:MENTOR_REQUIRED

#### 조건

- MATCHING_MANAGEMENT 권한 운영자

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "MENTOR_REQUIRED", "status": 400 }`

-----

### [TC-MR-012] 운영자 멘토 지정 — 멘토가 아닌 계정 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:NOT_A_MENTOR

#### 조건

- 지정할 mentorAccountId=20 (role=MENTEE)

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "NOT_A_MENTOR", "status": 422 }`

-----

### [TC-MR-013] 운영자 멘토 지정 — 매칭 여력 없는 멘토

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 여력 검증 로직
  - Component — `@WebMvcTest` 422 반환
- **루프 포함**: Yes
- **대상**: BR-003, VR:MENTOR_NO_CAPACITY
- **⚠️ 주의**: OQ-003 확정 필요

#### 조건

- mentorAccountId=7 (role=MENTOR, 매칭 여력 없음)

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MENTOR_NO_CAPACITY", "status": 422 }`
- 부수 효과: MatchRequest 상태 변경 없음

-----

### [TC-MR-014] 운영자 멘토 지정 — APPROVED 상태 요청에 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_NOT_ASSIGNABLE
- **연관 요건**: VR:MATCH_REQUEST_NOT_ASSIGNABLE

#### 조건

- MatchRequest id=200, status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCH_REQUEST_NOT_ASSIGNABLE", "status": 422 }`

-----

### [TC-MR-015] 운영자 멘토 지정 — CANCELLED 상태 요청에 시도

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_NOT_ASSIGNABLE

#### 조건

- MatchRequest status=CANCELLED

#### 기대 결과

- 도메인 레벨 예외 (MATCH_REQUEST_NOT_ASSIGNABLE)

-----

### [TC-MR-016] 운영자 멘토 지정 — MATCHING_MANAGEMENT 권한 없음

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 권한 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-003
- **연관 요건**: BR-003

#### 조건

- ACCOUNT_MANAGEMENT 권한만 보유한 운영자 (MATCHING_MANAGEMENT 없음)

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: MatchRequest 상태 변경 없음

-----

### [TC-MR-017] 운영자 멘토 지정 — 존재하지 않는 requestId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_NOT_FOUND

#### 조건

- requestId=9999 없음

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "MATCH_REQUEST_NOT_FOUND", "status": 404 }`

-----

### [TC-MR-018] 운영자 멘토 지정 — MENTOR_ASSIGNED 상태 재지정 (덮어쓰기)

- **중요도**: P3
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-003 — 이미 지정된 상태에서 재지정 허용 여부

#### 조건

- MatchRequest id=200, status=MENTOR_ASSIGNED, mentorAccountId=7
- 새 멘토 mentorAccountId=8 (여력 있음)으로 재지정 시도

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 200, "mentorAccountId": 8, "status": "MENTOR_ASSIGNED" }`
- 부수 효과: mentorAccountId 갱신

-----

### [TC-MR-019] 매칭 승인 성공 — 멘티가 지정한 멘토 (REQUESTED → APPROVED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchRequest.approve()` 상태 전이, mentorAccountId 지정 여부 검증
  - Component — `POST /admin/match-requests/{requestId}/approve` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchRequestService.approve()`, BR-004
- **연관 요건**: UC-MATCH-004, BR-004

#### 조건

- MATCHING_MANAGEMENT 권한 운영자
- MatchRequest id=200, status=REQUESTED, mentorAccountId=7 (멘티가 직접 지정)

#### 입력

- Path: `POST /admin/match-requests/200/approve`

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 200, "status": "APPROVED" }`
- 부수 효과: status=APPROVED

-----

### [TC-MR-020] 매칭 승인 성공 — 운영자가 지정한 멘토 (MENTOR_ASSIGNED → APPROVED)

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-004
- **연관 요건**: UC-MATCH-004, BR-004

#### 조건

- MATCHING_MANAGEMENT 권한 운영자
- MatchRequest id=200, status=MENTOR_ASSIGNED, mentorAccountId=7

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 200, "status": "APPROVED" }`

-----

### [TC-MR-021] 매칭 승인 — 멘토 미지정 상태에서 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchRequest.approve()` mentorAccountId null 체크
  - Component — `@WebMvcTest` 422 반환
- **루프 포함**: Yes
- **대상**: BR-004, VR:MENTOR_NOT_ASSIGNED
- **연관 요건**: BR-004

#### 조건

- MatchRequest id=200, status=REQUESTED, mentorAccountId=null

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MENTOR_NOT_ASSIGNED", "status": 422 }`
- 부수 효과: status 변경 없음

-----

### [TC-MR-022] 매칭 승인 — 이미 APPROVED 상태

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_ALREADY_PROCESSED

#### 조건

- MatchRequest status=APPROVED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCH_REQUEST_ALREADY_PROCESSED", "status": 422 }`

-----

### [TC-MR-023] 매칭 승인 — CANCELLED 상태

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_ALREADY_PROCESSED

#### 조건

- MatchRequest status=CANCELLED

#### 기대 결과

- 도메인 레벨 예외 (MATCH_REQUEST_ALREADY_PROCESSED)

-----

### [TC-MR-024] 매칭 승인 — MATCHING_MANAGEMENT 권한 없음

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 권한 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-004

#### 조건

- ACCOUNT_MANAGEMENT 권한만 보유한 운영자

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: MatchRequest 상태 변경 없음

-----

### [TC-MR-025] 매칭 승인 — 존재하지 않는 requestId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_NOT_FOUND

#### 조건

- requestId=9999 없음

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "MATCH_REQUEST_NOT_FOUND", "status": 404 }`

-----

### [TC-MR-026] 내 매칭 요청 목록 조회 — 본인 요청만 반환

- **중요도**: P1
- **계층**: Component
  - Component — `GET /match-requests/me` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `MatchRequestService.findMyRequests()`, BR-005
- **연관 요건**: BR-005

#### 조건

- 멘티 A의 MatchRequest: id=200, id=201
- 멘티 B의 MatchRequest: id=202

#### 기대 결과

- HTTP 200 OK
- 반환 목록에 id=200, id=201 포함, id=202 미포함

-----

### [TC-MR-027] 내 매칭 요청 목록 조회 — 타인 요청 미포함 확인

- **중요도**: P1
- **계층**: Component
- **루프 포함**: Yes
- **대상**: BR-005 소유 필터

#### 조건

- 멘티 B로 인증, 멘티 A의 요청만 존재 (id=200)

#### 기대 결과

- HTTP 200 OK
- 반환 목록 비어 있음 (`content: []`)

-----

## 커버리지 매트릭스

|요건                                |커버 TC                          |비고                   |
|----------------------------------|-------------------------------|---------------------|
|UC-MATCH-001                      |TC-MR-001, TC-MR-003~009       |—                    |
|UC-MATCH-002                      |TC-MR-002, TC-MR-006~007       |OQ-003 의존            |
|UC-MATCH-003                      |TC-MR-010~018                  |—                    |
|UC-MATCH-004                      |TC-MR-019~025                  |—                    |
|BR-001                            |TC-MR-001, TC-MR-008~009       |P0 — Unit+Component ✅|
|BR-002                            |TC-MR-002, TC-MR-007           |P0 — Unit+Component ✅|
|BR-003                            |TC-MR-010, TC-MR-013, TC-MR-016|P0 — Unit+Component ✅|
|BR-004                            |TC-MR-019~021, TC-MR-024       |P0 — Unit+Component ✅|
|BR-005                            |TC-MR-026~027                  |—                    |
|VR:FIELD_REQUIRED                 |TC-MR-003                      |—                    |
|VR:FIELD_NOT_FOUND_OR_INACTIVE    |TC-MR-004, TC-MR-005           |—                    |
|VR:NOT_A_MENTOR                   |TC-MR-006, TC-MR-012           |—                    |
|VR:MENTOR_NO_CAPACITY             |TC-MR-007, TC-MR-013           |P0 — Unit+Component ✅|
|VR:MENTOR_REQUIRED                |TC-MR-011                      |—                    |
|VR:MATCH_REQUEST_NOT_ASSIGNABLE   |TC-MR-014, TC-MR-015           |—                    |
|VR:MENTOR_NOT_ASSIGNED            |TC-MR-021                      |P0 — Unit+Component ✅|
|VR:MATCH_REQUEST_ALREADY_PROCESSED|TC-MR-022, TC-MR-023           |—                    |
|VR:MATCH_REQUEST_NOT_FOUND        |TC-MR-017, TC-MR-025           |—                    |

-----

## 루프 외 케이스 목록

해당 없음. 전 케이스 Unit 또는 Component 내 검증 가능.

## ⚠️ OQ 의존 케이스

|TC-ID    |의존 OQ |현재 가정                        |확정 후 조치                           |
|---------|------|-----------------------------|----------------------------------|
|TC-MR-002|OQ-003|매칭 여력 = 조건 충족 상태로 stub 처리    |OQ-003 결정 후 여력 판단 로직 및 mock 조건 구체화|
|TC-MR-007|OQ-003|매칭 여력 없음 = 조건 미충족 상태로 stub 처리|동일                                |
|TC-MR-013|OQ-003|동일                           |동일                                |