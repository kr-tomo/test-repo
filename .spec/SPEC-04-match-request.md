# SPEC: 매칭 요청 (MatchRequest)

버전: 0.1
상태: Draft
작성일: 2026-06-16
KB 참조: [kb/INDEX.md](./kb/INDEX.md) → [domain-model.md](./kb/domain-model.md#match-request), [business-rules.md](./kb/business-rules.md#매칭-matching)
선행 SPEC: [SPEC-01-field.md](./SPEC-01-field.md), [SPEC-02-mentor-profile.md](./SPEC-02-mentor-profile.md), [SPEC-03-instant-qa.md](./SPEC-03-instant-qa.md)

-----

## 1. 개요

### 1.1 목적

멘티가 멘토링을 받기 위해 매칭을 신청하고, 운영자가 이를 검토하여 멘토를 지정하고 승인하는 절차를 제공한다.
승인된 매칭 요청은 멘티의 결제 단계(SPEC-05)로 이어진다.

### 1.2 범위

**포함**

- 멘티의 분야 기반 매칭 요청 생성
- 멘티의 Q&A 답변 멘토 지정 매칭 요청
- 운영자의 멘토 지정
- 운영자의 매칭 승인

**제외**

- 매칭 성립 이후(결제 및 채널 제공) (→ SPEC-05)
- 인스턴스 Q&A 자체 (→ SPEC-03)

### 1.3 용어 정의

|용어                 |정의                                          |
|-------------------|--------------------------------------------|
|매칭 요청(MatchRequest)|멘티가 매칭을 신청하여 생성되는 레코드. 승인 전 단계              |
|매칭 여력              |멘토가 추가 매칭을 수락할 수 있는 상태 (OQ-003 미확정)         |
|운영자 멘토 지정          |운영자가 MatchRequest에 멘토를 배정하는 행위              |
|매칭 승인              |운영자가 멘토가 지정된 MatchRequest를 APPROVED로 확정하는 행위|

-----

## 2. Actor & Use Case

### 2.1 Actor

|Actor                    |설명                          |
|-------------------------|----------------------------|
|멘티                       |매칭 요청 생성 (분야 기반 또는 특정 멘토 지정)|
|운영자 (MATCHING_MANAGEMENT)|매칭 요청에 멘토 지정, 매칭 승인         |

### 2.2 Use Case 목록

|UC-ID       |Actor                    |Use Case          |우선순위|
|------------|-------------------------|------------------|----|
|UC-MATCH-001|멘티                       |분야 기반 매칭 요청       |Must|
|UC-MATCH-002|멘티                       |Q&A 답변 멘토 지정 매칭 요청|Must|
|UC-MATCH-003|운영자 (MATCHING_MANAGEMENT)|매칭 요청에 멘토 지정      |Must|
|UC-MATCH-004|운영자 (MATCHING_MANAGEMENT)|매칭 승인             |Must|

-----

## 3. 비즈니스 규칙 (Business Rules)

### BR-001: 매칭 요청 생성 — 분야 기반

- **조건**: 멘티가 분야를 통해 매칭을 요청하는 경우 (특정 멘토 미지정)
- **결과**: MatchRequest가 생성된다. mentorAccountId는 null. 상태는 REQUESTED.
- **연관 UC**: UC-MATCH-001

### BR-002: 매칭 요청 생성 — 특정 멘토 지정

- **조건**: 멘티가 특정 멘토를 지정하여 매칭을 요청하는 경우 (Q&A 답변 멘토 포함)
- **결과**: 해당 멘토의 **매칭 여력**이 있어야 한다. 여력이 없으면 거부. MatchRequest가 생성되고 mentorAccountId 지정. 상태는 REQUESTED.
- **연관 UC**: UC-MATCH-002
- **OQ**: OQ-003 — 매칭 여력의 구체적 정의 미확정. SPEC 구현 전 반드시 확인.

### BR-003: 운영자 멘토 지정 — MATCHING_MANAGEMENT 권한 필요

- **조건**: 운영자가 MatchRequest에 멘토를 지정하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. 지정된 mentorAccountId는 role=MENTOR여야 하며 매칭 여력이 있어야 한다. 상태는 MENTOR_ASSIGNED로 전환.
- **연관 UC**: UC-MATCH-003
- **OQ**: OQ-003 — 매칭 여력 정의 동일하게 미확정.

### BR-004: 매칭 승인 — 멘토 지정 상태여야 함

- **조건**: 운영자가 MatchRequest를 승인하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. mentorAccountId가 지정된 상태(REQUESTED with 멘토 or MENTOR_ASSIGNED)에서만 APPROVED로 전환 가능. 멘토 미지정 상태에서 승인은 거부.
- **연관 UC**: UC-MATCH-004

### BR-005: 매칭 요청 소유 — 멘티 본인만 조회

- **조건**: 멘티가 자신의 매칭 요청 목록을 조회하는 경우
- **결과**: 본인(menteeAccountId)의 MatchRequest만 조회된다.

-----

## 4. Validation Rules

### 4.1 매칭 요청 생성

|필드             |규칙                 |오류 메시지                  |오류 코드                        |
|---------------|-------------------|------------------------|-----------------------------|
|fieldId        |Not Null           |“분야는 필수입니다.”            |`FIELD_REQUIRED`             |
|fieldId        |존재하는 ACTIVE Field  |“존재하지 않거나 비활성화된 분야입니다.” |`FIELD_NOT_FOUND_OR_INACTIVE`|
|mentorAccountId|지정 시 role=MENTOR 계정|“멘토 계정만 지정할 수 있습니다.”    |`NOT_A_MENTOR`               |
|mentorAccountId|지정 시 매칭 여력 보유      |“해당 멘토는 현재 매칭 여력이 없습니다.”|`MENTOR_NO_CAPACITY`         |


> OQ-003: `MENTOR_NO_CAPACITY` 판단 로직 미확정. 구현 전 반드시 결정 필요.

### 4.2 운영자 멘토 지정

|필드              |규칙                             |오류 메시지                  |오류 코드                         |
|----------------|-------------------------------|------------------------|------------------------------|
|mentorAccountId |Not Null                       |“멘토는 필수입니다.”            |`MENTOR_REQUIRED`             |
|mentorAccountId |role=MENTOR 계정                 |“멘토 계정만 지정할 수 있습니다.”    |`NOT_A_MENTOR`                |
|mentorAccountId |매칭 여력 보유                       |“해당 멘토는 현재 매칭 여력이 없습니다.”|`MENTOR_NO_CAPACITY`          |
|requestId (path)|REQUESTED 또는 MENTOR_ASSIGNED 상태|“이미 승인되었거나 취소된 요청입니다.”  |`MATCH_REQUEST_NOT_ASSIGNABLE`|

### 4.3 매칭 승인

|필드              |규칙                             |오류 메시지                       |오류 코드                            |
|----------------|-------------------------------|-----------------------------|---------------------------------|
|requestId (path)|존재하는 MatchRequest              |“존재하지 않는 매칭 요청입니다.”          |`MATCH_REQUEST_NOT_FOUND`        |
|requestId (path)|mentorAccountId 지정 상태          |“멘토가 지정되지 않은 요청은 승인할 수 없습니다.”|`MENTOR_NOT_ASSIGNED`            |
|requestId (path)|REQUESTED 또는 MENTOR_ASSIGNED 상태|“이미 처리된 요청입니다.”              |`MATCH_REQUEST_ALREADY_PROCESSED`|

-----

## 5. 도메인 모델

### 5.1 Aggregate / Entity / Value Object

```
MatchRequest (Aggregate Root)
├── id: Long
├── menteeAccountId: Long
├── mentorAccountId: Long | null       # null: 멘토 미지정
├── fieldId: Long
├── status: MatchRequestStatus
└── requestedAt: LocalDateTime
```

#### MatchRequest

|필드             |타입                |설명   |제약                      |
|---------------|------------------|-----|------------------------|
|id             |Long              |식별자  |PK, Not Null            |
|menteeAccountId|Long              |요청 멘티|Not Null, FK → account  |
|mentorAccountId|Long              |지정 멘토|Nullable, FK → account  |
|fieldId        |Long              |요청 분야|Not Null, FK → field    |
|status         |MatchRequestStatus|처리 상태|Not Null, 기본값: REQUESTED|
|requestedAt    |LocalDateTime     |요청 시각|Not Null                |

#### MatchRequestStatus (Enum)

|값              |설명                          |
|---------------|----------------------------|
|REQUESTED      |요청됨 (멘토 미지정 or 멘티가 직접 멘토 지정)|
|MENTOR_ASSIGNED|운영자가 멘토를 지정한 상태             |
|APPROVED       |운영자가 승인 완료                  |
|CANCELLED      |취소됨                         |

### 5.2 상태 전이

```
                    ┌──멘티 멘토 직접 지정──┐
                    │                     ↓
[REQUESTED] ──운영자 멘토 지정──▶ [MENTOR_ASSIGNED]
    │                                   │
    └────────────────────────────────────┤
                                        ↓
                               운영자 승인 가능
                                        │
                                        ▼
                                  [APPROVED] ──▶ 결제(SPEC-05)

언제든 ──▶ [CANCELLED]
```

|전이                                    |행위자|조건                         |연관 BR         |
|--------------------------------------|---|---------------------------|--------------|
|생성 → REQUESTED                        |멘티 |—                          |BR-001, BR-002|
|REQUESTED → MENTOR_ASSIGNED           |운영자|MATCHING_MANAGEMENT, 멘토 여력 |BR-003        |
|REQUESTED / MENTOR_ASSIGNED → APPROVED|운영자|MATCHING_MANAGEMENT, 멘토 지정됨|BR-004        |
|→ CANCELLED                           |운영자|MATCHING_MANAGEMENT        |—             |

-----

## 6. 아키텍처 및 컴포넌트

### 6.1 레이어별 컴포넌트

|레이어                |컴포넌트                            |역할                  |
|-------------------|--------------------------------|--------------------|
|Inbound Adapter    |`MatchRequestController`        |멘티 매칭 요청 HTTP 수신    |
|Inbound Adapter    |`AdminMatchRequestController`   |운영자 멘토 지정·승인 HTTP 수신|
|Inbound Port       |`CreateMatchRequestUseCase`     |매칭 요청 생성            |
|Inbound Port       |`AssignMentorUseCase`           |운영자 멘토 지정           |
|Inbound Port       |`ApproveMatchRequestUseCase`    |매칭 승인               |
|Application Service|`MatchRequestService`           |Use Case 구현         |
|Domain             |`MatchRequest`                  |Aggregate Root      |
|Outbound Port      |`MatchRequestRepositoryPort`    |영속성 인터페이스           |
|Outbound Adapter   |`MatchRequestPersistenceAdapter`|JPA 구현              |

### 6.2 패키지 경로

```
com.<organization>.<app>/
└── matching/
    ├── domain/
    │   ├── model/
    │   │   ├── MatchRequest.java
    │   │   └── MatchRequestStatus.java
    │   └── port/
    │       ├── in/
    │       │   ├── CreateMatchRequestUseCase.java
    │       │   ├── AssignMentorUseCase.java
    │       │   └── ApproveMatchRequestUseCase.java
    │       └── out/
    │           └── MatchRequestRepositoryPort.java
    ├── application/
    │   └── service/
    │       └── MatchRequestService.java
    └── adapter/
        ├── in/web/
        │   ├── MatchRequestController.java
        │   └── AdminMatchRequestController.java
        └── out/persistence/
            └── MatchRequestPersistenceAdapter.java
```

-----

## 7. REST API

### 7.1 엔드포인트 목록

|Method|Path                                       |설명             |성공 코드|연관 UC                     |
|------|-------------------------------------------|---------------|-----|--------------------------|
|POST  |`/match-requests`                          |매칭 요청 생성       |201  |UC-MATCH-001, UC-MATCH-002|
|GET   |`/match-requests/me`                       |내 매칭 요청 목록 (멘티)|200  |—                         |
|PATCH |`/admin/match-requests/{requestId}/mentor` |운영자 멘토 지정      |200  |UC-MATCH-003              |
|POST  |`/admin/match-requests/{requestId}/approve`|매칭 승인          |200  |UC-MATCH-004              |

### 7.2 요청/응답 스키마

#### POST `/match-requests` — 요청

```json
{
  "fieldId": 10,
  "mentorAccountId": 7
}
```

|필드             |타입  |필수|설명                         |연관 VR                                      |
|---------------|----|--|---------------------------|-------------------------------------------|
|fieldId        |Long|Y |요청 분야 ID                   |FIELD_REQUIRED, FIELD_NOT_FOUND_OR_INACTIVE|
|mentorAccountId|Long|N |특정 멘토 지정 시. null이면 분야 기반 요청|NOT_A_MENTOR, MENTOR_NO_CAPACITY           |

#### POST `/match-requests` — 응답 (201)

```json
{
  "id": 200,
  "fieldId": 10,
  "fieldName": "백엔드",
  "mentorAccountId": 7,
  "status": "REQUESTED",
  "requestedAt": "2026-06-16T10:00:00"
}
```

#### PATCH `/admin/match-requests/{requestId}/mentor` — 요청

```json
{
  "mentorAccountId": 7
}
```

|필드             |타입  |필수|설명    |연관 VR                                            |
|---------------|----|--|------|-------------------------------------------------|
|mentorAccountId|Long|Y |지정할 멘토|MENTOR_REQUIRED, NOT_A_MENTOR, MENTOR_NO_CAPACITY|

#### PATCH `/admin/match-requests/{requestId}/mentor` — 응답 (200)

```json
{
  "id": 200,
  "mentorAccountId": 7,
  "status": "MENTOR_ASSIGNED"
}
```

#### POST `/admin/match-requests/{requestId}/approve` — 응답 (200)

```json
{
  "id": 200,
  "status": "APPROVED"
}
```

#### 오류 응답

|HTTP|오류 코드                            |발생 조건                        |
|----|---------------------------------|-----------------------------|
|400 |`FIELD_REQUIRED`                 |fieldId 누락                   |
|400 |`MENTOR_REQUIRED`                |멘토 지정 요청 시 mentorAccountId 누락|
|403 |`FORBIDDEN`                      |권한 없음                        |
|404 |`MATCH_REQUEST_NOT_FOUND`        |존재하지 않는 requestId            |
|422 |`FIELD_NOT_FOUND_OR_INACTIVE`    |INACTIVE 또는 없는 fieldId       |
|422 |`NOT_A_MENTOR`                   |멘토 아닌 계정 지정                  |
|422 |`MENTOR_NO_CAPACITY`             |매칭 여력 없는 멘토                  |
|422 |`MATCH_REQUEST_NOT_ASSIGNABLE`   |멘토 지정 불가 상태                  |
|422 |`MENTOR_NOT_ASSIGNED`            |멘토 미지정 상태에서 승인 시도            |
|422 |`MATCH_REQUEST_ALREADY_PROCESSED`|이미 APPROVED/CANCELLED 상태     |

-----

## 8. 영속화

### 8.1 테이블 설계

```sql
CREATE TABLE match_request (
    id                  BIGINT      NOT NULL AUTO_INCREMENT,
    mentee_account_id   BIGINT      NOT NULL,
    mentor_account_id   BIGINT      NULL,
    field_id            BIGINT      NOT NULL,
    status              VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    requested_at        DATETIME    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (mentee_account_id) REFERENCES account(id),
    FOREIGN KEY (mentor_account_id) REFERENCES account(id),
    FOREIGN KEY (field_id) REFERENCES field(id)
);
```

### 8.2 인덱스

|인덱스명                             |대상 컬럼            |목적         |
|---------------------------------|-----------------|-----------|
|`idx_match_req_mentee_account_id`|mentee_account_id|멘티별 요청 조회  |
|`idx_match_req_mentor_account_id`|mentor_account_id|멘토별 요청 조회  |
|`idx_match_req_status`           |status           |운영자 처리 큐 조회|

### 8.3 트랜잭션 경계

|Use Case           |트랜잭션 범위                              |비고                     |
|-------------------|-------------------------------------|-----------------------|
|UC-MATCH-001/002 생성|MatchRequestService.create() 전체      |여력 체크 + MatchRequest 저장|
|UC-MATCH-003 멘토 지정 |MatchRequestService.assignMentor() 전체|status 수정              |
|UC-MATCH-004 승인    |MatchRequestService.approve() 전체     |status → APPROVED      |

-----

## 11. 미결 항목 (Open Questions)

|OQ-ID |질문          |선택지                                  |영향                                                                 |
|------|------------|-------------------------------------|-------------------------------------------------------------------|
|OQ-003|멘토 매칭 여력의 정의|A: ACTIVE 매칭 수 상한 / B: 상태 토글 / C: A+B|BR-002~003, MentorProfile 모델(capacity 필드), MENTOR_NO_CAPACITY 판단 로직|