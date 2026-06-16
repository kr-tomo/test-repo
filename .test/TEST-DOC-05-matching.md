# TEST_DOC: 매칭 성립 및 운영 (Matching)

연관 SPEC: SPEC-05-matching.md (v0.1)
작성일: 2026-06-16
개발 루프 대상 레벨: Unit, Component

-----

## 케이스 요약

|TC-ID    |이름                                                |중요도|계층             |루프 포함|연관 요건                                |
|---------|--------------------------------------------------|---|---------------|-----|-------------------------------------|
|TC-MT-001|멘토링 상품 결제 → 매칭 성립 성공                              |P0 |Unit, Component|Yes  |UC-MATCH-005, BR-001                 |
|TC-MT-002|매칭 성립 — matchRequestId 누락                         |P2 |Unit, Component|Yes  |VR:MATCH_REQUEST_ID_REQUIRED         |
|TC-MT-003|매칭 성립 — 존재하지 않는 matchRequestId                    |P2 |Component      |Yes  |VR:MATCH_REQUEST_NOT_FOUND           |
|TC-MT-004|매칭 성립 — APPROVED 아닌 MatchRequest (REQUESTED)      |P0 |Unit, Component|Yes  |BR-001, VR:MATCH_REQUEST_NOT_APPROVED|
|TC-MT-005|매칭 성립 — APPROVED 아닌 MatchRequest (MENTOR_ASSIGNED)|P0 |Unit           |Yes  |BR-001, VR:MATCH_REQUEST_NOT_APPROVED|
|TC-MT-006|매칭 성립 — 본인 MatchRequest 아님                        |P0 |Unit, Component|Yes  |VR:NOT_YOUR_MATCH_REQUEST            |
|TC-MT-007|매칭 성립 — productId 누락                              |P2 |Unit, Component|Yes  |VR:PRODUCT_ID_REQUIRED               |
|TC-MT-008|매칭 성립 — channels 빈 배열                             |P2 |Unit, Component|Yes  |VR:CHANNEL_REQUIRED                  |
|TC-MT-009|매칭 성립 — 채널 URL 형식 오류                              |P2 |Unit, Component|Yes  |VR:CHANNEL_URL_INVALID               |
|TC-MT-010|매칭 성립 — 유효하지 않은 채널 유형                             |P2 |Unit, Component|Yes  |VR:CHANNEL_TYPE_INVALID              |
|TC-MT-011|매칭 성립 — 멘토 계정으로 시도                                |P0 |Component      |Yes  |UC-MATCH-005                         |
|TC-MT-012|매칭 채널 validUntil 이후 접근 불가                         |P0 |Unit, Component|Yes  |BR-002                               |
|TC-MT-013|매칭 채널 validUntil 당일 접근 가능 (경계값)                   |P3 |Unit           |Yes  |BR-002                               |
|TC-MT-014|연장 가능 여부 설정 — extendable=true 성공                  |P1 |Unit, Component|Yes  |UC-MATCH-006, BR-003                 |
|TC-MT-015|연장 가능 여부 설정 — extendable=false 성공                 |P1 |Unit           |Yes  |UC-MATCH-006, BR-003                 |
|TC-MT-016|연장 가능 여부 설정 — EXPIRED 매칭에 시도                      |P2 |Unit, Component|Yes  |BR-003, VR:MATCHING_NOT_ACTIVE       |
|TC-MT-017|연장 가능 여부 설정 — 타인 매칭에 시도                           |P0 |Unit, Component|Yes  |BR-003, VR:NOT_YOUR_MATCHING         |
|TC-MT-018|연장 가능 여부 설정 — extendable 값 누락                     |P2 |Unit, Component|Yes  |VR:EXTENDABLE_REQUIRED               |
|TC-MT-019|연장 가능 여부 설정 — 멘티 계정으로 시도                          |P0 |Component      |Yes  |BR-003                               |
|TC-MT-020|연장 상품 결제 성공 — validUntil 연장 확인                    |P0 |Unit, Component|Yes  |UC-MATCH-007, BR-004                 |
|TC-MT-021|연장 결제 — extendable=false 매칭에 시도                   |P0 |Unit, Component|Yes  |BR-004, VR:MATCHING_NOT_EXTENDABLE   |
|TC-MT-022|연장 결제 — EXPIRED 매칭에 시도                            |P0 |Unit, Component|Yes  |BR-004, VR:MATCHING_NOT_ACTIVE       |
|TC-MT-023|연장 결제 — 본인 매칭 아님                                  |P0 |Unit, Component|Yes  |VR:NOT_YOUR_MATCHING                 |
|TC-MT-024|연장 결제 — 멘토 계정으로 시도                                |P0 |Component      |Yes  |UC-MATCH-007                         |
|TC-MT-025|연장 결제 — productId 누락                              |P2 |Unit, Component|Yes  |VR:PRODUCT_ID_REQUIRED               |
|TC-MT-026|ACTIVE → EXPIRED 자동 전환 (스케줄러)                     |P0 |Unit, Component|Yes  |BR-005                               |
|TC-MT-027|자동 전환 — validUntil 당일은 ACTIVE 유지 (경계값)            |P3 |Unit           |Yes  |BR-005                               |
|TC-MT-028|자동 전환 — 이미 EXPIRED인 매칭은 변경 없음                     |P3 |Unit           |Yes  |BR-005                               |
|TC-MT-029|매칭 해지 성공 (ACTIVE → EXPIRED)                       |P0 |Unit, Component|Yes  |UC-MATCH-008, BR-006                 |
|TC-MT-030|매칭 해지 — EXPIRED 매칭에 시도                            |P2 |Unit, Component|Yes  |BR-006, VR:MATCHING_NOT_ACTIVE       |
|TC-MT-031|매칭 해지 — ARCHIVED 매칭에 시도                           |P2 |Unit           |Yes  |VR:MATCHING_NOT_ACTIVE               |
|TC-MT-032|매칭 해지 — MATCHING_MANAGEMENT 권한 없음                 |P0 |Unit, Component|Yes  |BR-006                               |
|TC-MT-033|매칭 해지 — 존재하지 않는 matchingId                        |P2 |Component      |Yes  |VR:MATCHING_NOT_FOUND                |
|TC-MT-034|매칭 아카이빙 성공 (EXPIRED → ARCHIVED)                   |P1 |Unit, Component|Yes  |UC-MATCH-009, BR-007                 |
|TC-MT-035|매칭 아카이빙 — ACTIVE 매칭에 시도                           |P2 |Unit, Component|Yes  |BR-007, VR:MATCHING_NOT_EXPIRED      |
|TC-MT-036|매칭 아카이빙 — 이미 ARCHIVED                             |P2 |Unit           |Yes  |VR:MATCHING_NOT_EXPIRED              |
|TC-MT-037|매칭 아카이빙 — MATCHING_MANAGEMENT 권한 없음               |P0 |Unit, Component|Yes  |BR-007                               |
|TC-MT-038|매칭 아카이빙 — 존재하지 않는 matchingId                      |P2 |Component      |Yes  |VR:MATCHING_NOT_FOUND                |
|TC-MT-039|내 매칭 목록 조회 — 멘티 기준 본인 매칭만 반환                      |P1 |Component      |Yes  |—                                    |
|TC-MT-040|내 매칭 목록 조회 — 멘토 기준 본인 매칭만 반환                      |P1 |Component      |Yes  |—                                    |
|TC-MT-041|매칭 상세 조회 — 채널 정보 포함 반환                            |P1 |Component      |Yes  |BR-002                               |
|TC-MT-042|PaymentPort — 결제 실패 시 Matching 미생성                |P0 |Unit, Component|Yes  |BR-001                               |
|TC-MT-043|PaymentPort — 연장 결제 실패 시 validUntil 미변경           |P0 |Unit, Component|Yes  |BR-004                               |

-----

## 테스트 케이스

### [TC-MT-001] 멘토링 상품 결제 → 매칭 성립 성공

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `Matching.create()` 도메인 객체 생성, status=ACTIVE, validFrom/validUntil, extendable=false(기본값), channels 초기화 검증
  - Component — `POST /matchings` API (`@SpringBootTest`, PaymentPort mock 처리)
- **루프 포함**: Yes
- **대상**: `MatchingService.create()`, BR-001
- **연관 요건**: UC-MATCH-005, BR-001

#### 조건

- role=MENTEE 계정으로 인증
- MatchRequest id=200, status=APPROVED, menteeAccountId=본인, mentorAccountId=7
- PaymentPort mock: 결제 성공 반환
- 상품 기간: 90일

#### 입력

```json
{
  "matchRequestId": 200,
  "productId": 99,
  "channels": [
    { "type": "FREE_CHAT", "url": "https://open.kakao.com/o/xxxx" },
    { "type": "ONLINE_MEETING", "url": "https://zoom.us/j/xxxx" }
  ]
}
```

#### 기대 결과

- HTTP 201 Created
- 응답:

```json
{
  "id": any,
  "matchRequestId": 200,
  "mentorAccountId": 7,
  "menteeAccountId": 본인,
  "status": "ACTIVE",
  "validFrom": "오늘날짜",
  "validUntil": "오늘+90일",
  "extendable": false,
  "channels": [
    { "type": "FREE_CHAT", "url": "https://open.kakao.com/o/xxxx" },
    { "type": "ONLINE_MEETING", "url": "https://zoom.us/j/xxxx" }
  ]
}
```

- 부수 효과: Matching 1건 저장(ACTIVE), MatchingChannel 2건 저장

-----

### [TC-MT-002] 매칭 성립 — matchRequestId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_ID_REQUIRED

#### 입력

```json
{ "productId": 99, "channels": [{ "type": "FREE_CHAT", "url": "https://example.com" }] }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "MATCH_REQUEST_ID_REQUIRED", "status": 400 }`

-----

### [TC-MT-003] 매칭 성립 — 존재하지 않는 matchRequestId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:MATCH_REQUEST_NOT_FOUND

#### 입력

```json
{ "matchRequestId": 9999, "productId": 99, "channels": [{ "type": "FREE_CHAT", "url": "https://example.com" }] }
```

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "MATCH_REQUEST_NOT_FOUND", "status": 404 }`

-----

### [TC-MT-004] 매칭 성립 — APPROVED 아닌 MatchRequest (REQUESTED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchingService.create()` 상태 선행 조건 검증 로직
  - Component — `@WebMvcTest` 422 반환
- **루프 포함**: Yes
- **대상**: BR-001, VR:MATCH_REQUEST_NOT_APPROVED
- **연관 요건**: BR-001

#### 조건

- MatchRequest id=200, status=REQUESTED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCH_REQUEST_NOT_APPROVED", "status": 422 }`
- 부수 효과: Matching 저장 없음, 결제 호출 없음

-----

### [TC-MT-005] 매칭 성립 — APPROVED 아닌 MatchRequest (MENTOR_ASSIGNED)

- **중요도**: P0
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-001, VR:MATCH_REQUEST_NOT_APPROVED

#### 조건

- MatchRequest status=MENTOR_ASSIGNED

#### 기대 결과

- 도메인/서비스 레벨 예외 (MATCH_REQUEST_NOT_APPROVED)

-----

### [TC-MT-006] 매칭 성립 — 본인 MatchRequest 아님

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 소유권(menteeAccountId) 검증 로직
  - Component — `@WebMvcTest` 422 반환
- **루프 포함**: Yes
- **대상**: VR:NOT_YOUR_MATCH_REQUEST

#### 조건

- MatchRequest id=200, menteeAccountId=15 (멘티 A)
- 멘티 B (id=16)로 인증하여 결제 시도

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "NOT_YOUR_MATCH_REQUEST", "status": 422 }`

-----

### [TC-MT-007] 매칭 성립 — productId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:PRODUCT_ID_REQUIRED

#### 입력

```json
{ "matchRequestId": 200, "channels": [{ "type": "FREE_CHAT", "url": "https://example.com" }] }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "PRODUCT_ID_REQUIRED", "status": 400 }`

-----

### [TC-MT-008] 매칭 성립 — channels 빈 배열

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:CHANNEL_REQUIRED

#### 입력

```json
{ "matchRequestId": 200, "productId": 99, "channels": [] }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "CHANNEL_REQUIRED", "status": 400 }`

-----

### [TC-MT-009] 매칭 성립 — 채널 URL 형식 오류

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:CHANNEL_URL_INVALID

#### 입력

```json
{
  "matchRequestId": 200,
  "productId": 99,
  "channels": [{ "type": "FREE_CHAT", "url": "not-a-url" }]
}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "CHANNEL_URL_INVALID", "status": 400 }`

-----

### [TC-MT-010] 매칭 성립 — 유효하지 않은 채널 유형

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:CHANNEL_TYPE_INVALID
- **⚠️ 주의**: OQ-007 (ChannelType Enum 목록) 확정 후 유효 값 기준 확정 필요

#### 입력

```json
{
  "matchRequestId": 200,
  "productId": 99,
  "channels": [{ "type": "UNKNOWN_TYPE", "url": "https://example.com" }]
}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "CHANNEL_TYPE_INVALID", "status": 400 }`

-----

### [TC-MT-011] 매칭 성립 — 멘토 계정으로 시도

- **중요도**: P0
- **계층**: Component
  - Component — `@WebMvcTest` 인가 필터 검증
- **루프 포함**: Yes
- **대상**: 멘티 전용 인가

#### 조건

- role=MENTOR 계정으로 인증

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: Matching 저장 없음

-----

### [TC-MT-012] 매칭 채널 validUntil 이후 접근 불가

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `Matching.isChannelAccessible()` 또는 채널 유효성 검증 도메인 메서드
  - Component — 채널 접근 API에서 만료 체크 (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: BR-002 채널 유효 기간
- **연관 요건**: BR-002

#### 조건

- Matching status=ACTIVE, validUntil=어제 날짜 (만료 상태)

#### 기대 결과

- 채널 접근 거부 (도메인 레벨: isChannelAccessible() = false)
- Component 레벨: 채널 조회 API 403 또는 422 반환

-----

### [TC-MT-013] 매칭 채널 validUntil 당일 접근 가능 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-002 경계값

#### 조건

- validUntil = 오늘 날짜

#### 기대 결과

- `isChannelAccessible()` = true (당일 포함)

-----

### [TC-MT-014] 연장 가능 여부 설정 — extendable=true 성공

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Matching.setExtendable()` 상태 조건 + 값 변경 검증
  - Component — `PATCH /matchings/{matchingId}/extendable` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchingService.setExtendable()`, BR-003
- **연관 요건**: UC-MATCH-006, BR-003

#### 조건

- 멘토 A로 인증
- Matching id=300, status=ACTIVE, mentorAccountId=A, extendable=false

#### 입력

```json
{ "extendable": true }
```

#### 기대 결과

- HTTP 200 OK
- 부수 효과: extendable=true

-----

### [TC-MT-015] 연장 가능 여부 설정 — extendable=false 성공 (비활성)

- **중요도**: P1
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-003 양방향 토글

#### 조건

- Matching extendable=true

#### 입력

```json
{ "extendable": false }
```

#### 기대 결과

- extendable=false (예외 없음)

-----

### [TC-MT-016] 연장 가능 여부 설정 — EXPIRED 매칭에 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-003, VR:MATCHING_NOT_ACTIVE

#### 조건

- Matching status=EXPIRED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCHING_NOT_ACTIVE", "status": 422 }`

-----

### [TC-MT-017] 연장 가능 여부 설정 — 타인 매칭에 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 소유권(mentorAccountId) 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-003, VR:NOT_YOUR_MATCHING

#### 조건

- Matching id=300, mentorAccountId=7 (멘토 A)
- 멘토 B로 인증하여 설정 시도

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: extendable 변경 없음

-----

### [TC-MT-018] 연장 가능 여부 설정 — extendable 값 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:EXTENDABLE_REQUIRED

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "EXTENDABLE_REQUIRED", "status": 400 }`

-----

### [TC-MT-019] 연장 가능 여부 설정 — 멘티 계정으로 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘토 전용 인가

#### 조건

- role=MENTEE 계정

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MT-020] 연장 상품 결제 성공 — validUntil 연장 확인

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `Matching.extend()` validUntil 갱신 로직, extendable 조건 검증
  - Component — `POST /matchings/{matchingId}/extend` (`@SpringBootTest`, PaymentPort mock)
- **루프 포함**: Yes
- **대상**: `MatchingService.extend()`, BR-004
- **연관 요건**: UC-MATCH-007, BR-004

#### 조건

- 멘티로 인증 (menteeAccountId=본인)
- Matching id=300, status=ACTIVE, extendable=true, validUntil=2026-09-16
- PaymentPort mock: 결제 성공
- 연장 상품 기간: 90일

#### 입력

- Path: `POST /matchings/300/extend`

```json
{ "productId": 100 }
```

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 300, "status": "ACTIVE", "validUntil": "2026-12-15" }`
- 부수 효과: validUntil 90일 연장, status 유지(ACTIVE), 운영자 승인 없이 즉시 적용

-----

### [TC-MT-021] 연장 결제 — extendable=false 매칭에 시도

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `Matching.extend()` extendable 선행 조건 검증
  - Component — `@WebMvcTest` 422 반환
- **루프 포함**: Yes
- **대상**: BR-004, VR:MATCHING_NOT_EXTENDABLE
- **연관 요건**: BR-004

#### 조건

- Matching id=300, status=ACTIVE, extendable=false

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCHING_NOT_EXTENDABLE", "status": 422 }`
- 부수 효과: validUntil 변경 없음, 결제 호출 없음

-----

### [TC-MT-022] 연장 결제 — EXPIRED 매칭에 시도

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-004, VR:MATCHING_NOT_ACTIVE

#### 조건

- Matching status=EXPIRED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCHING_NOT_ACTIVE", "status": 422 }`

-----

### [TC-MT-023] 연장 결제 — 본인 매칭 아님

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 소유권(menteeAccountId) 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: VR:NOT_YOUR_MATCHING

#### 조건

- Matching menteeAccountId=15
- 멘티 B(id=16)로 인증

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MT-024] 연장 결제 — 멘토 계정으로 시도

- **중요도**: P0
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘티 전용 인가

#### 조건

- role=MENTOR 계정

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MT-025] 연장 결제 — productId 누락

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR:PRODUCT_ID_REQUIRED

#### 입력

```json
{}
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "PRODUCT_ID_REQUIRED", "status": 400 }`

-----

### [TC-MT-026] ACTIVE → EXPIRED 자동 전환 (스케줄러)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 만료 판단 로직 (`validUntil < today`) 단위 검증
  - Component — `@SpringBootTest` 배치 실행 후 DB 상태 검증
- **루프 포함**: Yes
- **대상**: `MatchingService.expireOverdue()` 또는 스케줄러 메서드, BR-005
- **연관 요건**: BR-005

#### 조건

- Matching A: status=ACTIVE, validUntil=어제 (만료 대상)
- Matching B: status=ACTIVE, validUntil=오늘 (유지 대상)
- Matching C: status=ACTIVE, validUntil=내일 (유지 대상)

#### 기대 결과

- 배치 실행 후:
  - Matching A: status=EXPIRED
  - Matching B: status=ACTIVE (유지)
  - Matching C: status=ACTIVE (유지)

-----

### [TC-MT-027] 자동 전환 — validUntil 당일은 ACTIVE 유지 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-005 경계값

#### 조건

- validUntil = 오늘

#### 기대 결과

- 만료 판단 결과: false (ACTIVE 유지)

-----

### [TC-MT-028] 자동 전환 — 이미 EXPIRED인 매칭은 변경 없음

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: 중복 전환 방지

#### 조건

- Matching status=EXPIRED, validUntil=어제

#### 기대 결과

- 배치 대상에서 제외, 상태 변경 없음

-----

### [TC-MT-029] 매칭 해지 성공 (ACTIVE → EXPIRED)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `Matching.terminate()` 상태 전이 검증
  - Component — `DELETE /admin/matchings/{matchingId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchingService.terminate()`, BR-006
- **연관 요건**: UC-MATCH-008, BR-006

#### 조건

- MATCHING_MANAGEMENT 권한 운영자
- Matching id=300, status=ACTIVE

#### 기대 결과

- HTTP 204 No Content
- 부수 효과: Matching status=EXPIRED

-----

### [TC-MT-030] 매칭 해지 — EXPIRED 매칭에 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-006, VR:MATCHING_NOT_ACTIVE

#### 조건

- Matching status=EXPIRED

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCHING_NOT_ACTIVE", "status": 422 }`

-----

### [TC-MT-031] 매칭 해지 — ARCHIVED 매칭에 시도

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:MATCHING_NOT_ACTIVE

#### 조건

- Matching status=ARCHIVED

#### 기대 결과

- 도메인 레벨 예외 (MATCHING_NOT_ACTIVE)

-----

### [TC-MT-032] 매칭 해지 — MATCHING_MANAGEMENT 권한 없음

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 권한 검증 로직
  - Component — `@WebMvcTest` 403 반환
- **루프 포함**: Yes
- **대상**: BR-006

#### 조건

- ACCOUNT_MANAGEMENT 권한만 보유한 운영자

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: Matching status 변경 없음

-----

### [TC-MT-033] 매칭 해지 — 존재하지 않는 matchingId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:MATCHING_NOT_FOUND

#### 조건

- matchingId=9999 없음

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "MATCHING_NOT_FOUND", "status": 404 }`

-----

### [TC-MT-034] 매칭 아카이빙 성공 (EXPIRED → ARCHIVED)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Matching.archive()` 상태 전이, EXPIRED 선행 조건 검증
  - Component — `POST /admin/matchings/{matchingId}/archive` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `MatchingService.archive()`, BR-007
- **연관 요건**: UC-MATCH-009, BR-007

#### 조건

- MATCHING_MANAGEMENT 권한 운영자
- Matching id=300, status=EXPIRED

#### 기대 결과

- HTTP 200 OK
- 응답: `{ "id": 300, "status": "ARCHIVED" }`
- 부수 효과: status=ARCHIVED

-----

### [TC-MT-035] 매칭 아카이빙 — ACTIVE 매칭에 시도

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-007, VR:MATCHING_NOT_EXPIRED

#### 조건

- Matching status=ACTIVE

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "MATCHING_NOT_EXPIRED", "status": 422 }`

-----

### [TC-MT-036] 매칭 아카이빙 — 이미 ARCHIVED

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR:MATCHING_NOT_EXPIRED

#### 조건

- Matching status=ARCHIVED

#### 기대 결과

- 도메인 레벨 예외 (MATCHING_NOT_EXPIRED)

-----

### [TC-MT-037] 매칭 아카이빙 — MATCHING_MANAGEMENT 권한 없음

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-007

#### 조건

- ACCOUNT_MANAGEMENT 권한만 보유한 운영자

#### 기대 결과

- HTTP 403 Forbidden

-----

### [TC-MT-038] 매칭 아카이빙 — 존재하지 않는 matchingId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR:MATCHING_NOT_FOUND

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "MATCHING_NOT_FOUND", "status": 404 }`

-----

### [TC-MT-039] 내 매칭 목록 조회 — 멘티 기준 본인 매칭만 반환

- **중요도**: P1
- **계층**: Component
  - Component — `GET /matchings/me` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `MatchingService.findMine()` 소유 필터

#### 조건

- 멘티 A(id=15) 매칭: id=300, id=301
- 멘티 B(id=16) 매칭: id=302

#### 기대 결과

- HTTP 200 OK
- 반환 목록에 id=300, id=301 포함, id=302 미포함

-----

### [TC-MT-040] 내 매칭 목록 조회 — 멘토 기준 본인 매칭만 반환

- **중요도**: P1
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 멘토 소유 필터 (mentorAccountId 기준)

#### 조건

- 멘토 A(id=7) 매칭: id=300
- 멘토 B(id=8) 매칭: id=301

#### 기대 결과

- HTTP 200 OK
- 멘토 A 조회 시 id=300만 반환

-----

### [TC-MT-041] 매칭 상세 조회 — 채널 정보 포함 반환

- **중요도**: P1
- **계층**: Component
  - Component — `GET /matchings/{matchingId}` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `MatchingService.getById()`, BR-002

#### 조건

- Matching id=300, status=ACTIVE, channels 2건 존재

#### 기대 결과

- HTTP 200 OK
- 응답에 channels 배열 포함 (type, url 각각 확인)

-----

### [TC-MT-042] PaymentPort — 결제 실패 시 Matching 미생성

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchingService.create()` 결제 실패 시 예외 전파 및 저장 롤백 검증
  - Component — `@SpringBootTest` 트랜잭션 롤백 확인
- **루프 포함**: Yes
- **대상**: PaymentPort 실패 처리, 트랜잭션 경계
- **연관 요건**: BR-001

#### 조건

- MatchRequest APPROVED 상태
- PaymentPort mock: 결제 실패(예외) 반환

#### 기대 결과

- HTTP 422 또는 500 (결제 실패 응답)
- 부수 효과: Matching 저장 없음, MatchingChannel 저장 없음 (롤백 확인)

-----

### [TC-MT-043] PaymentPort — 연장 결제 실패 시 validUntil 미변경

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `MatchingService.extend()` 결제 실패 시 validUntil 롤백 검증
  - Component — `@SpringBootTest` 트랜잭션 롤백 확인
- **루프 포함**: Yes
- **대상**: PaymentPort 실패 처리, 트랜잭션 경계
- **연관 요건**: BR-004

#### 조건

- Matching ACTIVE, extendable=true, validUntil=2026-09-16
- PaymentPort mock: 결제 실패 반환

#### 기대 결과

- 결제 실패 응답 반환
- 부수 효과: validUntil 변경 없음 (롤백), DB에 2026-09-16 유지 확인

-----

## 커버리지 매트릭스

|요건                           |커버 TC                                     |비고                   |
|-----------------------------|------------------------------------------|---------------------|
|UC-MATCH-005                 |TC-MT-001~011                             |—                    |
|UC-MATCH-006                 |TC-MT-014~019                             |—                    |
|UC-MATCH-007                 |TC-MT-020~025                             |—                    |
|UC-MATCH-008                 |TC-MT-029~033                             |—                    |
|UC-MATCH-009                 |TC-MT-034~038                             |—                    |
|BR-001                       |TC-MT-001, TC-MT-004~006, TC-MT-042       |P0 — Unit+Component ✅|
|BR-002                       |TC-MT-012~013, TC-MT-041                  |P0 — Unit+Component ✅|
|BR-003                       |TC-MT-014~019                             |P0 — Unit+Component ✅|
|BR-004                       |TC-MT-020~025, TC-MT-043                  |P0 — Unit+Component ✅|
|BR-005                       |TC-MT-026~028                             |P0 — Unit+Component ✅|
|BR-006                       |TC-MT-029~033                             |P0 — Unit+Component ✅|
|BR-007                       |TC-MT-034~038                             |P0 — Unit+Component ✅|
|VR:MATCH_REQUEST_ID_REQUIRED |TC-MT-002                                 |—                    |
|VR:MATCH_REQUEST_NOT_FOUND   |TC-MT-003                                 |—                    |
|VR:MATCH_REQUEST_NOT_APPROVED|TC-MT-004, TC-MT-005                      |—                    |
|VR:NOT_YOUR_MATCH_REQUEST    |TC-MT-006                                 |—                    |
|VR:PRODUCT_ID_REQUIRED       |TC-MT-007, TC-MT-025                      |—                    |
|VR:CHANNEL_REQUIRED          |TC-MT-008                                 |—                    |
|VR:CHANNEL_URL_INVALID       |TC-MT-009                                 |—                    |
|VR:CHANNEL_TYPE_INVALID      |TC-MT-010                                 |OQ-007 의존            |
|VR:EXTENDABLE_REQUIRED       |TC-MT-018                                 |—                    |
|VR:MATCHING_NOT_ACTIVE       |TC-MT-016, TC-MT-022, TC-MT-030, TC-MT-031|—                    |
|VR:NOT_YOUR_MATCHING         |TC-MT-017, TC-MT-023                      |—                    |
|VR:MATCHING_NOT_EXTENDABLE   |TC-MT-021                                 |—                    |
|VR:MATCHING_NOT_FOUND        |TC-MT-033, TC-MT-038                      |—                    |
|VR:MATCHING_NOT_EXPIRED      |TC-MT-035, TC-MT-036                      |—                    |

-----

## 루프 외 케이스 목록

|TC-ID|계층         |실행 환경      |비고                                      |
|-----|-----------|-----------|----------------------------------------|
|—    |Integration|실제 PG 연동 환경|실제 결제 시스템과의 연동 검증 (PaymentAdapter 실제 호출)|

-----

## ⚠️ OQ 의존 케이스

|TC-ID    |의존 OQ |현재 가정                                 |확정 후 조치                                |
|---------|------|--------------------------------------|---------------------------------------|
|TC-MT-010|OQ-007|FREE_CHAT, ONLINE_MEETING 외 값은 INVALID|OQ-007 확정 후 유효 ChannelType Enum 값 목록 반영|