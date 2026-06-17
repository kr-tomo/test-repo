# SPEC: 인스턴스 Q&A (InstantQA)

버전: 0.1
상태: Draft
작성일: 2026-06-16
KB 참조: [kb/INDEX.md](./kb/INDEX.md) → [domain-model.md](./kb/domain-model.md#instant-qa), [business-rules.md](./kb/business-rules.md#인스턴스-qa-instant-qa)
선행 SPEC: [SPEC-01-field.md](./SPEC-01-field.md), [SPEC-02-mentor-profile.md](./SPEC-02-mentor-profile.md)

-----

## 1. 개요

### 1.1 목적

멘티가 특정 분야의 전문 멘토에게 즉각적인 질문을 게재하고, 해당 분야를 보유한 멘토가 답변하는 비동기 Q&A 채널을 제공한다.
인스턴스 Q&A는 멘티가 매칭 전에 멘토의 전문성을 확인하고 적합한 멘토를 선택하는 경로로도 활용된다.

### 1.2 범위

**포함**

- 멘티의 Q&A 등록
- 멘토의 Q&A 목록 조회 (분야 필터 기반)
- 멘토의 답변 작성 및 수정
- 멘티의 자신 Q&A 답변 확인

**제외**

- 답변 멘토에 대한 매칭 요청 (→ SPEC-04)
- Q&A 삭제·마감 처리 (OQ-008 미결, 현재 범위 제외)

### 1.3 용어 정의

|용어                 |정의                                    |
|-------------------|--------------------------------------|
|인스턴스 Q&A(InstantQA)|멘티가 분야를 지정하여 게재하는 즉문즉답 형태의 질문         |
|Q&A 답변(QAAnswer)   |멘토가 특정 InstantQA에 보내는 답변. 멘토당 1회 제한   |
|조회 가능 Q&A          |멘토의 APPROVED·visible=true 분야에 매칭되는 Q&A|

-----

## 2. Actor & Use Case

### 2.1 Actor

|Actor|설명                          |
|-----|----------------------------|
|멘티   |Q&A 등록, 자신의 Q&A 답변 확인       |
|멘토   |조회 가능한 Q&A 목록 조회, 답변 작성 및 수정|

### 2.2 Use Case 목록

|UC-ID    |Actor|Use Case       |우선순위|
|---------|-----|---------------|----|
|UC-QA-001|멘티   |Q&A 등록         |Must|
|UC-QA-002|멘토   |조회 가능 Q&A 목록 조회|Must|
|UC-QA-003|멘토   |Q&A 답변 작성      |Must|
|UC-QA-004|멘토   |자신의 답변 수정      |Must|
|UC-QA-005|멘티   |자신의 Q&A 답변 확인  |Must|

-----

## 3. 비즈니스 규칙 (Business Rules)

### BR-001: Q&A 등록 — 분야 필수, ACTIVE만 허용

- **조건**: 멘티가 인스턴스 Q&A를 등록하는 경우
- **결과**: 분야(fieldId)를 반드시 지정해야 하며, 지정된 분야는 ACTIVE 상태여야 한다.
- **연관 UC**: UC-QA-001

### BR-002: 멘토의 Q&A 조회 범위 — 자신의 승인 분야로 한정

- **조건**: 멘토가 인스턴스 Q&A 목록을 조회하는 경우
- **결과**: 멘토의 MentorField 중 status=APPROVED이고 visible=true인 분야와 일치하는 Q&A만 조회 가능하다.
- **정렬**: postedAt 내림차순 (최신 게재 순).
- **예시**: 멘토가 “백엔드(id=10)”, “Java(id=11)” 분야를 승인·노출 상태로 보유 중이면, fieldId가 10 또는 11인 Q&A만 조회된다.
- **연관 UC**: UC-QA-002

### BR-003: 답변 — 멘토당 1회 제한

- **조건**: 멘토가 특정 InstantQA에 답변을 작성하는 경우
- **결과**: 동일 InstantQA에 대해 같은 멘토는 답변을 1개만 작성할 수 있다. 이미 답변한 경우 재답변은 거부된다.
- **연관 UC**: UC-QA-003

### BR-004: 답변 수정 — 본인 답변만 허용

- **조건**: 멘토가 답변을 수정하는 경우
- **결과**: 본인이 작성한 답변(mentorAccountId가 요청자)만 수정할 수 있다. content만 변경 가능.
- **연관 UC**: UC-QA-004

### BR-005: 답변 확인 — 본인 Q&A만 허용

- **조건**: 멘티가 Q&A 답변을 확인하는 경우
- **결과**: 본인이 게재한 InstantQA에 달린 답변만 조회할 수 있다.
- **연관 UC**: UC-QA-005

-----

## 4. Validation Rules

### 4.1 InstantQA 등록

|필드     |규칙                 |오류 메시지                  |오류 코드                        |
|-------|-------------------|------------------------|-----------------------------|
|content|Not Null, Not Blank|“질문 내용은 필수입니다.”         |`QA_CONTENT_REQUIRED`        |
|content|Max 1000자          |“질문 내용은 1000자 이하여야 합니다.”|`QA_CONTENT_TOO_LONG`        |
|fieldId|Not Null           |“분야는 필수입니다.”            |`FIELD_REQUIRED`             |
|fieldId|존재하는 ACTIVE Field  |“존재하지 않거나 비활성화된 분야입니다.” |`FIELD_NOT_FOUND_OR_INACTIVE`|


> OQ-006: content 최대 길이 1000자는 임시값.

### 4.2 QAAnswer 작성/수정

|필드                    |규칙                 |오류 메시지                  |오류 코드                    |
|----------------------|-------------------|------------------------|-------------------------|
|content               |Not Null, Not Blank|“답변 내용은 필수입니다.”         |`ANSWER_CONTENT_REQUIRED`|
|content               |Max 2000자          |“답변 내용은 2000자 이하여야 합니다.”|`ANSWER_CONTENT_TOO_LONG`|
|(작성 시) mentorAccountId|동일 QA에 이미 답변 없어야 함 |“이미 답변한 질문입니다.”         |`ANSWER_ALREADY_EXISTS`  |


> OQ-006: content 최대 길이 2000자는 임시값.

-----

## 5. 도메인 모델

### 5.1 Aggregate / Entity / Value Object

```
InstantQA (Aggregate Root)
├── id: Long
├── menteeAccountId: Long
├── fieldId: Long
├── content: String
├── postedAt: LocalDateTime
└── answers: List<QAAnswer>         # Entity
```

#### InstantQA

|필드             |타입           |설명   |제약                    |
|---------------|-------------|-----|----------------------|
|id             |Long         |식별자  |PK, Not Null          |
|menteeAccountId|Long         |게재 멘티|Not Null, FK → account|
|fieldId        |Long         |지정 분야|Not Null, FK → field  |
|content        |String       |질문 내용|Not Null, Max 1000자   |
|postedAt       |LocalDateTime|게재 시각|Not Null              |

#### QAAnswer (Entity)

|필드             |타입           |설명          |제약                    |
|---------------|-------------|------------|----------------------|
|id             |Long         |식별자         |PK, Not Null          |
|instantQAId    |Long         |소속 InstantQA|Not Null, FK          |
|mentorAccountId|Long         |답변 멘토       |Not Null, FK → account|
|content        |String       |답변 내용       |Not Null, Max 2000자   |
|answeredAt     |LocalDateTime|최초 답변 시각    |Not Null              |
|updatedAt      |LocalDateTime|수정 시각       |Not Null              |

-----

## 6. 아키텍처 및 컴포넌트

### 6.1 레이어별 컴포넌트

|레이어                |컴포넌트                         |역할               |
|-------------------|-----------------------------|-----------------|
|Inbound Adapter    |`InstantQAController`        |Q&A 등록·조회 HTTP 수신|
|Inbound Adapter    |`QAAnswerController`         |답변 작성·수정 HTTP 수신 |
|Inbound Port       |`PostInstantQAUseCase`       |Q&A 등록           |
|Inbound Port       |`GetInstantQAsUseCase`       |Q&A 목록 조회 (멘토용)  |
|Inbound Port       |`GetMyInstantQAsUseCase`     |내 Q&A 답변 확인 (멘티용)|
|Inbound Port       |`AnswerInstantQAUseCase`     |답변 작성            |
|Inbound Port       |`UpdateQAAnswerUseCase`      |답변 수정            |
|Application Service|`InstantQAService`           |Q&A Use Case 구현  |
|Application Service|`QAAnswerService`            |답변 Use Case 구현   |
|Domain             |`InstantQA`                  |Aggregate Root   |
|Domain             |`QAAnswer`                   |Entity           |
|Outbound Port      |`InstantQARepositoryPort`    |영속성 인터페이스        |
|Outbound Adapter   |`InstantQAPersistenceAdapter`|JPA 구현           |

### 6.2 패키지 경로

```
com.<organization>.<app>/
└── qa/
    ├── domain/
    │   ├── model/
    │   │   ├── InstantQA.java
    │   │   └── QAAnswer.java
    │   └── port/
    │       ├── in/
    │       │   ├── PostInstantQAUseCase.java
    │       │   ├── GetInstantQAsUseCase.java
    │       │   ├── GetMyInstantQAsUseCase.java
    │       │   ├── AnswerInstantQAUseCase.java
    │       │   └── UpdateQAAnswerUseCase.java
    │       └── out/
    │           └── InstantQARepositoryPort.java
    ├── application/
    │   └── service/
    │       ├── InstantQAService.java
    │       └── QAAnswerService.java
    └── adapter/
        ├── in/web/
        │   ├── InstantQAController.java
        │   └── QAAnswerController.java
        └── out/persistence/
            └── InstantQAPersistenceAdapter.java
```

-----

## 7. REST API

### 7.1 엔드포인트 목록

|Method|Path                                    |설명                    |성공 코드|연관 UC    |
|------|----------------------------------------|----------------------|-----|---------|
|POST  |`/instant-qas`                          |Q&A 등록                |201  |UC-QA-001|
|GET   |`/instant-qas`                          |Q&A 목록 조회 (멘토용, 분야 필터)|200  |UC-QA-002|
|GET   |`/instant-qas/me`                       |내 Q&A 목록 (멘티용)        |200  |UC-QA-005|
|GET   |`/instant-qas/{qaId}/answers`           |특정 Q&A의 답변 목록 (멘티용)   |200  |UC-QA-005|
|POST  |`/instant-qas/{qaId}/answers`           |답변 작성                 |201  |UC-QA-003|
|PATCH |`/instant-qas/{qaId}/answers/{answerId}`|답변 수정                 |200  |UC-QA-004|

### 7.2 요청/응답 스키마

#### POST `/instant-qas` — 요청

```json
{
  "fieldId": 10,
  "content": "Spring Boot에서 멀티 모듈 구성 시 의존성 관리는 어떻게 하시나요?"
}
```

|필드     |타입    |필수|설명   |연관 VR                                      |
|-------|------|--|-----|-------------------------------------------|
|fieldId|Long  |Y |분야 ID|FIELD_REQUIRED, FIELD_NOT_FOUND_OR_INACTIVE|
|content|String|Y |질문 내용|QA_CONTENT_REQUIRED, QA_CONTENT_TOO_LONG   |

#### POST `/instant-qas` — 응답 (201)

```json
{
  "id": 100,
  "fieldId": 10,
  "fieldName": "백엔드",
  "content": "Spring Boot에서 멀티 모듈 구성 시 의존성 관리는 어떻게 하시나요?",
  "postedAt": "2026-06-16T10:00:00"
}
```

#### GET `/instant-qas` — 응답 (200)

멘토의 APPROVED·visible=true 분야 기반 필터. 최신 게재 순.

```json
{
  "content": [
    {
      "id": 100,
      "fieldId": 10,
      "fieldName": "백엔드",
      "content": "Spring Boot에서 멀티 모듈 구성 시 의존성 관리는 어떻게 하시나요?",
      "postedAt": "2026-06-16T10:00:00",
      "answerCount": 2
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

#### GET `/instant-qas/me` — 응답 (200)

멘티가 게재한 자신의 Q&A 목록.

```json
{
  "content": [
    {
      "id": 100,
      "fieldId": 10,
      "fieldName": "백엔드",
      "content": "...",
      "postedAt": "2026-06-16T10:00:00",
      "answerCount": 2
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

#### GET `/instant-qas/{qaId}/answers` — 응답 (200)

```json
[
  {
    "id": 50,
    "mentorAccountId": 7,
    "content": "gradle multi-project 기준으로 설명드리면...",
    "answeredAt": "2026-06-16T11:00:00",
    "updatedAt": "2026-06-16T11:00:00"
  }
]
```

#### POST `/instant-qas/{qaId}/answers` — 요청

```json
{
  "content": "gradle multi-project 기준으로 설명드리면..."
}
```

|필드     |타입    |필수|설명   |연관 VR                                           |
|-------|------|--|-----|------------------------------------------------|
|content|String|Y |답변 내용|ANSWER_CONTENT_REQUIRED, ANSWER_CONTENT_TOO_LONG|

#### POST `/instant-qas/{qaId}/answers` — 응답 (201)

```json
{
  "id": 50,
  "instantQAId": 100,
  "content": "gradle multi-project 기준으로 설명드리면...",
  "answeredAt": "2026-06-16T11:00:00"
}
```

#### PATCH `/instant-qas/{qaId}/answers/{answerId}` — 요청

```json
{
  "content": "수정된 답변 내용"
}
```

#### 오류 응답

|HTTP|오류 코드                        |발생 조건                 |
|----|-----------------------------|----------------------|
|400 |`QA_CONTENT_REQUIRED`        |content 누락            |
|400 |`QA_CONTENT_TOO_LONG`        |content 1000자 초과      |
|400 |`ANSWER_CONTENT_REQUIRED`    |답변 content 누락         |
|400 |`ANSWER_CONTENT_TOO_LONG`    |답변 content 2000자 초과   |
|400 |`FIELD_REQUIRED`             |fieldId 누락            |
|403 |`FORBIDDEN`                  |본인 Q&A·답변 아님          |
|404 |`INSTANT_QA_NOT_FOUND`       |존재하지 않는 qaId          |
|404 |`QA_ANSWER_NOT_FOUND`        |존재하지 않는 answerId      |
|409 |`ANSWER_ALREADY_EXISTS`      |동일 Q&A 재답변 시도         |
|422 |`FIELD_NOT_FOUND_OR_INACTIVE`|INACTIVE 또는 없는 fieldId|

-----

## 8. 영속화

### 8.1 테이블 설계

```sql
CREATE TABLE instant_qa (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    mentee_account_id   BIGINT          NOT NULL,
    field_id            BIGINT          NOT NULL,
    content             VARCHAR(1000)   NOT NULL,
    posted_at           DATETIME        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (mentee_account_id) REFERENCES account(id),
    FOREIGN KEY (field_id) REFERENCES field(id)
);

CREATE TABLE qa_answer (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    instant_qa_id       BIGINT          NOT NULL,
    mentor_account_id   BIGINT          NOT NULL,
    content             TEXT            NOT NULL,
    answered_at         DATETIME        NOT NULL,
    updated_at          DATETIME        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (instant_qa_id) REFERENCES instant_qa(id),
    FOREIGN KEY (mentor_account_id) REFERENCES account(id)
);
```

### 8.2 인덱스

|인덱스명                               |대상 컬럼                             |목적                    |
|-----------------------------------|----------------------------------|----------------------|
|`idx_instant_qa_field_id_posted_at`|(field_id, posted_at DESC)        |멘토 목록 조회 (분야 필터 + 최신순)|
|`idx_instant_qa_mentee_account_id` |mentee_account_id                 |멘티 자신의 Q&A 조회         |
|`uk_qa_answer_qa_mentor`           |(instant_qa_id, mentor_account_id)|멘토당 1답변 Unique 보장     |
|`idx_qa_answer_instant_qa_id`      |instant_qa_id                     |Q&A별 답변 목록 조회         |

### 8.3 트랜잭션 경계

|Use Case        |트랜잭션 범위                    |비고                 |
|----------------|---------------------------|-------------------|
|UC-QA-001 Q&A 등록|InstantQAService.post() 전체 |InstantQA 단건 저장    |
|UC-QA-003 답변 작성 |QAAnswerService.answer() 전체|중복 체크 + QAAnswer 저장|
|UC-QA-004 답변 수정 |QAAnswerService.update() 전체|QAAnswer content 수정|

-----

## 11. 미결 항목 (Open Questions)

|OQ-ID |질문                |선택지                             |영향                             |
|------|------------------|--------------------------------|-------------------------------|
|OQ-006|Q&A content 최대 길이 |현재 임시 1000자                     |content 컬럼 타입                  |
|OQ-006|답변 content 최대 길이  |현재 임시 2000자                     |content 컬럼 타입 (TEXT vs VARCHAR)|
|OQ-008|Q&A 삭제·마감 기능 필요 여부|A: 없음 / B: 멘티 삭제 / C: 답변 수락 후 마감|InstantQA 상태 필드 추가, UC 추가      |