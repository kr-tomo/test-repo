# SPEC: 매칭 성립 및 운영 (Matching)

버전: 0.1
상태: Draft
작성일: 2026-06-16
KB 참조: [kb/INDEX.md](./kb/INDEX.md) → [domain-model.md](./kb/domain-model.md#matching), [business-rules.md](./kb/business-rules.md#매칭-matching)
선행 SPEC: [SPEC-04-match-request.md](./SPEC-04-match-request.md)

-----

## 1. 개요

### 1.1 목적

운영자가 승인한 매칭 요청에 대해 멘티가 멘토링 상품을 결제하면 매칭이 성립되고, 결제 기간 동안 유효한 소통 채널이 제공된다.
성립된 매칭의 연장·해지·아카이빙을 통해 멘토링 관계 전체 생명주기를 관리한다.

### 1.2 범위

**포함**

- 멘토링 상품 결제 → 매칭 성립 (ACTIVE)
- 매칭 채널 제공 (validUntil까지)
- 멘토의 연장 가능 여부 설정
- 멘티의 연장 상품 결제
- 운영자의 매칭 해지·아카이빙

**제외**

- 결제 수단·PG 연동 (외부 결제 도메인)
- 매칭 요청 생성·승인 (→ SPEC-04)
- 환불 처리 (OQ-004 미결, 현재 범위 외)

### 1.3 용어 정의

|용어          |정의                                                 |
|------------|---------------------------------------------------|
|매칭(Matching)|결제 완료 후 성립된 멘토-멘티 관계. validFrom~validUntil 기간 동안 유효|
|매칭 채널       |성립된 매칭에서 제공되는 소통 수단 (URL 기반)                       |
|멘토링 상품      |멘토링 기간을 포함한 결제 단위                                  |
|연장 상품       |기존 매칭의 validUntil을 연장하는 추가 결제 단위                   |
|extendable  |멘토가 현재 매칭에 대해 연장을 허용할지 여부를 설정하는 플래그                |
|아카이빙        |EXPIRED 상태 매칭을 보관 처리하는 운영자 행위                      |

-----

## 2. Actor & Use Case

### 2.1 Actor

|Actor                    |설명                                    |
|-------------------------|--------------------------------------|
|멘티                       |결제로 매칭 성립, 연장 상품 결제                   |
|멘토                       |매칭 연장 가능 여부 설정                        |
|운영자 (MATCHING_MANAGEMENT)|매칭 해지, 매칭 아카이빙                        |
|시스템                      |validUntil 경과 시 ACTIVE → EXPIRED 자동 전환|

### 2.2 Use Case 목록

|UC-ID       |Actor                    |Use Case         |우선순위  |
|------------|-------------------------|-----------------|------|
|UC-MATCH-005|멘티                       |멘토링 상품 결제 → 매칭 성립|Must  |
|UC-MATCH-006|멘토                       |매칭 연장 가능 여부 설정   |Must  |
|UC-MATCH-007|멘티                       |연장 상품 결제         |Must  |
|UC-MATCH-008|운영자 (MATCHING_MANAGEMENT)|매칭 해지            |Must  |
|UC-MATCH-009|운영자 (MATCHING_MANAGEMENT)|매칭 아카이빙          |Should|

-----

## 3. 비즈니스 규칙 (Business Rules)

### BR-001: 매칭 성립 — APPROVED 요청 및 결제 완료 조건

- **조건**: 멘티가 멘토링 상품을 결제하는 경우
- **결과**: MatchRequest의 status가 APPROVED인 경우에만 결제가 가능하다. 결제 완료 시 Matching이 생성(ACTIVE)되고 상품의 기간(validFrom~validUntil)이 설정된다.
- **연관 UC**: UC-MATCH-005

### BR-002: 매칭 채널 유효 기간

- **조건**: 성립된 Matching에서 채널을 사용하는 경우
- **결과**: 매칭 채널은 Matching의 validUntil까지만 유효하다. validUntil이 경과하면 채널 접근이 불가하다.
- **연관 UC**: UC-MATCH-005 (채널 생성), 시스템 (채널 만료)

### BR-003: 연장 가능 여부 설정 — 본인 ACTIVE 매칭만

- **조건**: 멘토가 매칭의 연장 가능 여부를 설정하는 경우
- **결과**: 본인(mentorAccountId)이 포함된 ACTIVE 상태의 Matching에 대해서만 extendable 값을 변경할 수 있다.
- **연관 UC**: UC-MATCH-006

### BR-004: 연장 결제 — extendable=true 조건

- **조건**: 멘티가 연장 상품을 결제하는 경우
- **결과**: 해당 Matching의 extendable=true인 경우에만 결제할 수 있다. 결제 완료 시 별도 운영자 승인 없이 validUntil이 연장된다.
- **연관 UC**: UC-MATCH-007

### BR-005: ACTIVE → EXPIRED 자동 전환

- **조건**: Matching의 validUntil이 경과한 경우
- **결과**: 시스템이 Matching 상태를 EXPIRED로 자동 전환한다.
- **연관 UC**: 시스템 스케줄러

### BR-006: 매칭 해지 — MATCHING_MANAGEMENT 권한 필요

- **조건**: 운영자가 ACTIVE 상태의 Matching을 해지하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. ACTIVE → EXPIRED로 전환된다.
- **연관 UC**: UC-MATCH-008
- **OQ**: OQ-004 — 해지 시 환불 처리 방식 미확정.

### BR-007: 아카이빙 — EXPIRED 상태만 허용

- **조건**: 운영자가 Matching을 아카이빙하는 경우
- **결과**: `MATCHING_MANAGEMENT` 권한이 있어야 한다. EXPIRED 상태 Matching만 ARCHIVED로 전환 가능.
- **연관 UC**: UC-MATCH-009

-----

## 4. Validation Rules

### 4.1 멘토링 상품 결제 (매칭 성립)

|필드             |규칙                  |오류 메시지                  |오류 코드                       |
|---------------|--------------------|------------------------|----------------------------|
|matchRequestId |Not Null            |“매칭 요청 ID는 필수입니다.”      |`MATCH_REQUEST_ID_REQUIRED` |
|matchRequestId |존재하는 MatchRequest   |“존재하지 않는 매칭 요청입니다.”     |`MATCH_REQUEST_NOT_FOUND`   |
|matchRequestId |status=APPROVED     |“승인된 매칭 요청이 아닙니다.”      |`MATCH_REQUEST_NOT_APPROVED`|
|matchRequestId |본인(멘티)의 MatchRequest|“본인의 매칭 요청만 결제할 수 있습니다.”|`NOT_YOUR_MATCH_REQUEST`    |
|productId      |Not Null            |“상품 ID는 필수입니다.”         |`PRODUCT_ID_REQUIRED`       |
|channels       |Not Empty           |“채널 정보는 1개 이상이어야 합니다.”  |`CHANNEL_REQUIRED`          |
|channels[].type|Not Null, Enum 값    |“올바른 채널 유형이 아닙니다.”      |`CHANNEL_TYPE_INVALID`      |
|channels[].url |Not Null, URL 형식    |“올바른 URL 형식이 아닙니다.”     |`CHANNEL_URL_INVALID`       |


> OQ-007: channels[].type Enum 목록 미확정.

### 4.2 연장 가능 여부 설정

|필드               |규칙           |오류 메시지                  |오류 코드                |
|-----------------|-------------|------------------------|---------------------|
|matchingId (path)|존재하는 Matching|“존재하지 않는 매칭입니다.”        |`MATCHING_NOT_FOUND` |
|matchingId (path)|status=ACTIVE|“활성 상태의 매칭만 설정할 수 있습니다.”|`MATCHING_NOT_ACTIVE`|
|matchingId (path)|본인(멘토) 매칭    |“본인의 매칭만 설정할 수 있습니다.”   |`NOT_YOUR_MATCHING`  |
|extendable       |Not Null     |“연장 가능 여부는 필수입니다.”      |`EXTENDABLE_REQUIRED`|

### 4.3 연장 상품 결제

|필드               |규칙             |오류 메시지                  |오류 코드                    |
|-----------------|---------------|------------------------|-------------------------|
|matchingId (path)|존재하는 Matching  |“존재하지 않는 매칭입니다.”        |`MATCHING_NOT_FOUND`     |
|matchingId (path)|status=ACTIVE  |“활성 상태의 매칭만 연장할 수 있습니다.”|`MATCHING_NOT_ACTIVE`    |
|matchingId (path)|extendable=true|“연장이 허용되지 않는 매칭입니다.”    |`MATCHING_NOT_EXTENDABLE`|
|matchingId (path)|본인(멘티) 매칭      |“본인의 매칭만 연장할 수 있습니다.”   |`NOT_YOUR_MATCHING`      |
|productId        |Not Null       |“상품 ID는 필수입니다.”         |`PRODUCT_ID_REQUIRED`    |

### 4.4 매칭 해지 (운영자)

|필드               |규칙           |오류 메시지                  |오류 코드                |
|-----------------|-------------|------------------------|---------------------|
|matchingId (path)|존재하는 Matching|“존재하지 않는 매칭입니다.”        |`MATCHING_NOT_FOUND` |
|matchingId (path)|status=ACTIVE|“활성 상태의 매칭만 해지할 수 있습니다.”|`MATCHING_NOT_ACTIVE`|

### 4.5 매칭 아카이빙 (운영자)

|필드               |규칙            |오류 메시지                 |오류 코드                 |
|-----------------|--------------|-----------------------|----------------------|
|matchingId (path)|존재하는 Matching |“존재하지 않는 매칭입니다.”       |`MATCHING_NOT_FOUND`  |
|matchingId (path)|status=EXPIRED|“만료된 매칭만 아카이빙할 수 있습니다.”|`MATCHING_NOT_EXPIRED`|

-----

## 5. 도메인 모델

### 5.1 Aggregate / Entity / Value Object

```
Matching (Aggregate Root)
├── id: Long
├── matchRequestId: Long
├── mentorAccountId: Long
├── menteeAccountId: Long
├── status: MatchingStatus          # ACTIVE / EXPIRED / ARCHIVED
├── validFrom: LocalDate
├── validUntil: LocalDate
├── extendable: Boolean
└── channels: List<MatchingChannel>  # Value Object
```

#### Matching

|필드             |타입            |설명      |제약                                  |
|---------------|--------------|--------|------------------------------------|
|id             |Long          |식별자     |PK, Not Null                        |
|matchRequestId |Long          |원 매칭 요청 |Not Null, Unique, FK → match_request|
|mentorAccountId|Long          |멘토      |Not Null, FK → account              |
|menteeAccountId|Long          |멘티      |Not Null, FK → account              |
|status         |MatchingStatus|매칭 상태   |Not Null, 기본값: ACTIVE               |
|validFrom      |LocalDate     |매칭 시작일  |Not Null                            |
|validUntil     |LocalDate     |매칭 종료일  |Not Null                            |
|extendable     |Boolean       |연장 허용 여부|Not Null, 기본값: false                |
|createdAt      |LocalDateTime |생성 시각   |Not Null                            |
|updatedAt      |LocalDateTime |수정 시각   |Not Null                            |

#### MatchingChannel (Value Object)

|필드  |타입         |설명    |제약              |
|----|-----------|------|----------------|
|type|ChannelType|채널 유형 |Not Null, Enum  |
|url |String     |채널 URL|Not Null, URL 형식|

#### ChannelType (Enum)

|값             |설명          |
|--------------|------------|
|FREE_CHAT     |자유 대화 채널 URL|
|ONLINE_MEETING|온라인 미팅 URL  |


> OQ-007: ChannelType Enum 목록 확정 필요.

#### MatchingStatus (Enum)

|값       |설명              |
|--------|----------------|
|ACTIVE  |유효한 매칭. 채널 사용 가능|
|EXPIRED |기간 만료 또는 운영자 해지됨|
|ARCHIVED|운영자가 아카이빙 처리    |

### 5.2 상태 전이

```
결제 완료
    │
    ▼
 [ACTIVE] ──validUntil 경과 / 운영자 해지──▶ [EXPIRED] ──운영자 아카이빙──▶ [ARCHIVED]
    │
    └── extendable=true 시 연장 결제 → validUntil 연장 (상태 유지: ACTIVE)
```

|전이                  |행위자|조건                          |연관 BR |
|--------------------|---|----------------------------|------|
|생성 → ACTIVE         |시스템|결제 완료, MatchRequest APPROVED|BR-001|
|ACTIVE → EXPIRED    |시스템|validUntil 경과               |BR-005|
|ACTIVE → EXPIRED    |운영자|MATCHING_MANAGEMENT         |BR-006|
|EXPIRED → ARCHIVED  |운영자|MATCHING_MANAGEMENT         |BR-007|
|ACTIVE.validUntil 연장|시스템|extendable=true, 결제 완료      |BR-004|

### 5.3 도메인 이벤트

|이벤트명              |발생 시점                |포함 데이터                                                  |
|------------------|---------------------|--------------------------------------------------------|
|`MatchingCreated` |Matching 생성 시        |matchingId, mentorAccountId, menteeAccountId, validUntil|
|`MatchingExpired` |ACTIVE → EXPIRED 전환 시|matchingId                                              |
|`MatchingExtended`|validUntil 연장 시      |matchingId, newValidUntil                               |

-----

## 6. 아키텍처 및 컴포넌트

### 6.1 레이어별 컴포넌트

|레이어                |컴포넌트                        |역할                 |
|-------------------|----------------------------|-------------------|
|Inbound Adapter    |`MatchingController`        |멘티·멘토 매칭 HTTP 수신   |
|Inbound Adapter    |`AdminMatchingController`   |운영자 해지·아카이빙 HTTP 수신|
|Inbound Port       |`CreateMatchingUseCase`     |결제 후 매칭 성립         |
|Inbound Port       |`SetExtendableUseCase`      |연장 가능 여부 설정        |
|Inbound Port       |`ExtendMatchingUseCase`     |연장 상품 결제           |
|Inbound Port       |`TerminateMatchingUseCase`  |매칭 해지              |
|Inbound Port       |`ArchiveMatchingUseCase`    |매칭 아카이빙            |
|Application Service|`MatchingService`           |Use Case 구현        |
|Domain             |`Matching`                  |Aggregate Root     |
|Domain             |`MatchingChannel`           |Value Object       |
|Outbound Port      |`MatchingRepositoryPort`    |Matching 영속성 인터페이스 |
|Outbound Port      |`PaymentPort`               |결제 처리 외부 시스템 인터페이스 |
|Outbound Adapter   |`MatchingPersistenceAdapter`|JPA 구현             |
|Outbound Adapter   |`PaymentAdapter`            |외부 결제 시스템 연동       |

### 6.2 패키지 경로

```
com.<organization>.<app>/
└── matching/
    ├── domain/
    │   ├── model/
    │   │   ├── Matching.java
    │   │   ├── MatchingChannel.java
    │   │   ├── MatchingStatus.java
    │   │   └── ChannelType.java
    │   └── port/
    │       ├── in/
    │       │   ├── CreateMatchingUseCase.java
    │       │   ├── SetExtendableUseCase.java
    │       │   ├── ExtendMatchingUseCase.java
    │       │   ├── TerminateMatchingUseCase.java
    │       │   └── ArchiveMatchingUseCase.java
    │       └── out/
    │           ├── MatchingRepositoryPort.java
    │           └── PaymentPort.java
    ├── application/
    │   └── service/
    │       └── MatchingService.java
    └── adapter/
        ├── in/web/
        │   ├── MatchingController.java
        │   └── AdminMatchingController.java
        └── out/
            ├── persistence/
            │   └── MatchingPersistenceAdapter.java
            └── payment/
                └── PaymentAdapter.java
```

-----

## 7. REST API

### 7.1 엔드포인트 목록

|Method|Path                                   |설명               |성공 코드|연관 UC       |
|------|---------------------------------------|-----------------|-----|------------|
|POST  |`/matchings`                           |멘토링 상품 결제 → 매칭 성립|201  |UC-MATCH-005|
|GET   |`/matchings/me`                        |내 매칭 목록 (멘티/멘토)  |200  |—           |
|GET   |`/matchings/{matchingId}`              |매칭 상세 조회         |200  |—           |
|PATCH |`/matchings/{matchingId}/extendable`   |연장 가능 여부 설정 (멘토) |200  |UC-MATCH-006|
|POST  |`/matchings/{matchingId}/extend`       |연장 상품 결제 (멘티)    |200  |UC-MATCH-007|
|DELETE|`/admin/matchings/{matchingId}`        |매칭 해지            |204  |UC-MATCH-008|
|POST  |`/admin/matchings/{matchingId}/archive`|매칭 아카이빙          |200  |UC-MATCH-009|

### 7.2 요청/응답 스키마

#### POST `/matchings` — 요청

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

|필드             |타입    |필수|설명              |연관 VR                                              |
|---------------|------|--|----------------|---------------------------------------------------|
|matchRequestId |Long  |Y |승인된 매칭 요청 ID    |MATCH_REQUEST_NOT_FOUND, MATCH_REQUEST_NOT_APPROVED|
|productId      |Long  |Y |결제할 멘토링 상품 ID   |PRODUCT_ID_REQUIRED                                |
|channels       |Array |Y |매칭 채널 목록 (1개 이상)|CHANNEL_REQUIRED                                   |
|channels[].type|Enum  |Y |채널 유형           |CHANNEL_TYPE_INVALID                               |
|channels[].url |String|Y |채널 URL          |CHANNEL_URL_INVALID                                |

#### POST `/matchings` — 응답 (201)

```json
{
  "id": 300,
  "matchRequestId": 200,
  "mentorAccountId": 7,
  "menteeAccountId": 15,
  "status": "ACTIVE",
  "validFrom": "2026-06-16",
  "validUntil": "2026-09-16",
  "extendable": false,
  "channels": [
    { "type": "FREE_CHAT", "url": "https://open.kakao.com/o/xxxx" },
    { "type": "ONLINE_MEETING", "url": "https://zoom.us/j/xxxx" }
  ]
}
```

#### PATCH `/matchings/{matchingId}/extendable` — 요청

```json
{
  "extendable": true
}
```

#### POST `/matchings/{matchingId}/extend` — 요청

```json
{
  "productId": 100
}
```

#### POST `/matchings/{matchingId}/extend` — 응답 (200)

```json
{
  "id": 300,
  "status": "ACTIVE",
  "validUntil": "2026-12-16"
}
```

#### 오류 응답

|HTTP|오류 코드                       |발생 조건                   |
|----|----------------------------|------------------------|
|400 |`MATCH_REQUEST_ID_REQUIRED` |matchRequestId 누락       |
|400 |`PRODUCT_ID_REQUIRED`       |productId 누락            |
|400 |`CHANNEL_REQUIRED`          |channels 빈 배열           |
|400 |`CHANNEL_TYPE_INVALID`      |유효하지 않은 채널 유형           |
|400 |`CHANNEL_URL_INVALID`       |URL 형식 오류               |
|400 |`EXTENDABLE_REQUIRED`       |extendable 누락           |
|403 |`FORBIDDEN`                 |권한 없음 또는 본인 매칭 아님       |
|404 |`MATCH_REQUEST_NOT_FOUND`   |존재하지 않는 matchRequestId  |
|404 |`MATCHING_NOT_FOUND`        |존재하지 않는 matchingId      |
|422 |`MATCH_REQUEST_NOT_APPROVED`|APPROVED 아닌 MatchRequest|
|422 |`NOT_YOUR_MATCH_REQUEST`    |본인 MatchRequest 아님      |
|422 |`MATCHING_NOT_ACTIVE`       |ACTIVE 아닌 Matching      |
|422 |`MATCHING_NOT_EXTENDABLE`   |extendable=false        |
|422 |`MATCHING_NOT_EXPIRED`      |아카이빙 대상이 EXPIRED 아님     |

-----

## 8. 영속화

### 8.1 테이블 설계

```sql
CREATE TABLE matching (
    id                  BIGINT      NOT NULL AUTO_INCREMENT,
    match_request_id    BIGINT      NOT NULL UNIQUE,
    mentor_account_id   BIGINT      NOT NULL,
    mentee_account_id   BIGINT      NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    valid_from          DATE        NOT NULL,
    valid_until         DATE        NOT NULL,
    extendable          TINYINT(1)  NOT NULL DEFAULT 0,
    created_at          DATETIME    NOT NULL,
    updated_at          DATETIME    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (match_request_id) REFERENCES match_request(id),
    FOREIGN KEY (mentor_account_id) REFERENCES account(id),
    FOREIGN KEY (mentee_account_id) REFERENCES account(id)
);

CREATE TABLE matching_channel (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    matching_id BIGINT          NOT NULL,
    type        VARCHAR(30)     NOT NULL,
    url         VARCHAR(2048)   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (matching_id) REFERENCES matching(id)
);
```

### 8.2 인덱스

|인덱스명                              |대상 컬럼                |목적           |
|----------------------------------|---------------------|-------------|
|`idx_matching_mentor_account_id`  |mentor_account_id    |멘토별 매칭 조회    |
|`idx_matching_mentee_account_id`  |mentee_account_id    |멘티별 매칭 조회    |
|`idx_matching_status_valid_until` |(status, valid_until)|만료 스케줄러 배치 조회|
|`idx_matching_channel_matching_id`|matching_id          |채널 조회        |

### 8.3 트랜잭션 경계

|Use Case          |트랜잭션 범위                           |비고                           |
|------------------|----------------------------------|-----------------------------|
|UC-MATCH-005 매칭 성립|MatchingService.create() 전체       |결제 확인 + Matching + Channel 저장|
|UC-MATCH-006 연장 설정|MatchingService.setExtendable() 전체|extendable 단건 수정             |
|UC-MATCH-007 연장 결제|MatchingService.extend() 전체       |결제 확인 + validUntil 수정        |
|UC-MATCH-008 해지   |MatchingService.terminate() 전체    |status → EXPIRED             |
|UC-MATCH-009 아카이빙 |MatchingService.archive() 전체      |status → ARCHIVED            |

-----

## 9. 미들웨어 및 외부 연동

### 9.1 외부 결제 시스템

|외부 시스템     |연동 목적       |호출 시점                     |실패 처리                    |
|-----------|------------|--------------------------|-------------------------|
|결제 시스템 (PG)|멘토링 상품 결제 확인|UC-MATCH-005, UC-MATCH-007|결제 실패 응답 반환, Matching 미생성|

### 9.2 스케줄러 (만료 자동 처리)

|작업      |주기                      |처리 내용                                                    |비고       |
|--------|------------------------|---------------------------------------------------------|---------|
|매칭 만료 처리|매일 자정 (또는 validUntil 기준)|status=ACTIVE이고 validUntil < 오늘인 Matching을 EXPIRED로 일괄 전환|BR-005 적용|

-----

## 11. 미결 항목 (Open Questions)

|OQ-ID |질문                   |선택지                              |영향                       |
|------|---------------------|---------------------------------|-------------------------|
|OQ-004|매칭 해지 시 환불 처리        |A: 자동 환불 / B: 수동 / C: 범위 외       |BR-006, PaymentPort 인터페이스|
|OQ-007|ChannelType Enum 값 목록|FREE_CHAT, ONLINE_MEETING 외 추가 여부|ChannelType.java, VR 체크  |