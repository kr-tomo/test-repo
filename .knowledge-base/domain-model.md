# 도메인 모델

참조: [INDEX.md](./INDEX.md) | 용어: [glossary.md](./glossary.md)
아키텍처 컨벤션: `.project/conventions/architecture.md` | DDD 규칙: `.project/conventions/ddd.md`

-----

## 전체 Aggregate 목록

|Aggregate Root |설명                   |패키지 모듈    |
|---------------|---------------------|----------|
|`Account`      |사용자 계정 (멘토·멘티·운영자 공통)|`account` |
|`Field`        |서비스 분야 카테고리          |`field`   |
|`MentorProfile`|멘토의 경력·분야 등록 및 노출 설정 |`mentor`  |
|`MatchRequest` |매칭 요청 (승인 전 단계)      |`matching`|
|`Matching`     |성립된 매칭 (결제 완료 후)     |`matching`|
|`InstantQA`    |인스턴스 Q&A             |`qa`      |

-----

## Account

```
Account (Aggregate Root)
├── id: AccountId
├── email: Email                  # Value Object
├── role: AccountRole             # Enum: MENTOR / MENTEE / OPERATOR
└── operatorPermissions: Set<OperatorPermission>   # OPERATOR일 때만 유효
                                  # Enum: ACCOUNT_MANAGEMENT, MATCHING_MANAGEMENT
```

|필드                 |타입       |제약                               |
|-------------------|---------|---------------------------------|
|id                 |Long     |PK, Not Null                     |
|email              |String   |Not Null, Unique, 이메일 형식         |
|role               |Enum     |Not Null                         |
|operatorPermissions|Set<Enum>|role=OPERATOR일 때만 의미 있음. 빈 Set 허용|


> VR 참조: [VR-ACCOUNT](./validation-rules.md#account)

-----

## Field {#field}

분야는 계층적 카테고리로 운영자가 관리한다.

```
Field (Aggregate Root)
├── id: FieldId
├── name: String
├── parentId: FieldId | null      # null이면 최상위 분야
└── status: FieldStatus           # Enum: ACTIVE / INACTIVE
```

|필드      |타입    |제약                      |
|--------|------|------------------------|
|id      |Long  |PK, Not Null            |
|name    |String|Not Null, 동일 부모 내 Unique|
|parentId|Long  |Nullable (최상위인 경우)      |
|status  |Enum  |Not Null, 기본값: ACTIVE   |


> BR 참조: [BR-FIELD](./business-rules.md#분야-field)
> VR 참조: [VR-FIELD](./validation-rules.md#field)

-----

## MentorProfile {#mentor-profile}

멘토 1명당 하나의 MentorProfile Aggregate를 가진다.

```
MentorProfile (Aggregate Root)
├── id: MentorProfileId
├── accountId: AccountId          # 멘토 계정 참조
├── careers: List<CareerEntry>    # Entity
└── fieldRegistrations: List<FieldRegistration>  # Entity
```

### CareerEntry (Entity) {#career}

```
CareerEntry
├── id: CareerEntryId
├── content: String               # 자유 기술
├── status: RegistrationStatus    # PENDING / APPROVED / REJECTED / WITHDRAWN
└── visible: Boolean              # APPROVED 상태에서만 의미 있음
```

|필드     |타입     |제약                                 |
|-------|-------|-----------------------------------|
|id     |Long   |PK, Not Null                       |
|content|String |Not Null, Not Blank                |
|status |Enum   |Not Null, 기본값: PENDING             |
|visible|Boolean|Not Null, 기본값: true (APPROVED 전환 시)|

#### CareerEntry 상태 전이

```
[PENDING] ──승인──▶ [APPROVED]
    │                    (visible 설정 가능)
    ├──반려──▶ [REJECTED]
    └──철회──▶ [WITHDRAWN]
```

|전이                   |행위자                     |조건                |참조 BR        |
|---------------------|------------------------|------------------|-------------|
|PENDING → APPROVED   |운영자 (ACCOUNT_MANAGEMENT)|—                 |BR-CAREER-001|
|PENDING → REJECTED   |운영자 (ACCOUNT_MANAGEMENT)|—                 |BR-CAREER-001|
|PENDING → WITHDRAWN  |멘토                      |본인 소유             |BR-CAREER-003|
|APPROVED → visible 변경|멘토                      |본인 소유, APPROVED 상태|BR-CAREER-004|


> VR 참조: [VR-CAREER](./validation-rules.md#career)

-----

### FieldRegistration (Entity) {#mentor-field}

```
FieldRegistration
├── id: FieldRegistrationId
├── fieldId: FieldId              # 서비스 Field 참조
├── status: RegistrationStatus    # PENDING / APPROVED / REJECTED / WITHDRAWN
└── visible: Boolean              # APPROVED 상태에서만 의미 있음
```

|필드     |타입     |제약                                 |
|-------|-------|-----------------------------------|
|id     |Long   |PK, Not Null                       |
|fieldId|Long   |Not Null, 유효한 ACTIVE Field만 허용     |
|status |Enum   |Not Null, 기본값: PENDING             |
|visible|Boolean|Not Null, 기본값: true (APPROVED 전환 시)|

#### FieldRegistration 상태 전이

CareerEntry와 동일한 패턴을 따른다.

```
[PENDING] ──승인──▶ [APPROVED]
    │
    ├──반려──▶ [REJECTED]
    └──철회──▶ [WITHDRAWN]
```

|전이                   |행위자                     |조건                |참조 BR              |
|---------------------|------------------------|------------------|-------------------|
|PENDING → APPROVED   |운영자 (ACCOUNT_MANAGEMENT)|—                 |BR-MENTOR-FIELD-001|
|PENDING → REJECTED   |운영자 (ACCOUNT_MANAGEMENT)|—                 |BR-MENTOR-FIELD-001|
|PENDING → WITHDRAWN  |멘토                      |본인 소유             |BR-MENTOR-FIELD-003|
|APPROVED → visible 변경|멘토                      |본인 소유, APPROVED 상태|BR-MENTOR-FIELD-004|


> BR 참조: [BR-MENTOR-FIELD](./business-rules.md#멘토-분야-등록-mentor-field)
> VR 참조: [VR-MENTOR-FIELD](./validation-rules.md#mentor-field)

-----

## MatchRequest {#match-request}

매칭 성립 전 요청 단계.

```
MatchRequest (Aggregate Root)
├── id: MatchRequestId
├── menteeAccountId: AccountId
├── mentorAccountId: AccountId | null   # 멘티 지정 or 운영자 지정
├── fieldId: FieldId                    # 요청 분야
├── status: MatchRequestStatus
│         # REQUESTED / MENTOR_ASSIGNED / APPROVED / CANCELLED
└── requestedAt: LocalDateTime
```

#### MatchRequest 상태 전이 {#match-request-status}

```
[REQUESTED]
    │
    ├──운영자 멘토 지정──▶ [MENTOR_ASSIGNED]
    │   (또는 멘티가 직접 멘토 지정 후 REQUESTED로 시작)
    │
    └──운영자 승인──▶ [APPROVED] ──결제 완료──▶ Matching 생성
    
    언제든──▶ [CANCELLED]
```

|전이                         |행위자                      |조건         |참조 BR       |
|---------------------------|-------------------------|-----------|------------|
|생성 (REQUESTED)             |멘티                       |—          |BR-MATCH-001|
|생성 (멘토 지정 포함)              |멘티                       |멘토 매칭 여력 필요|BR-MATCH-002|
|REQUESTED → MENTOR_ASSIGNED|운영자 (MATCHING_MANAGEMENT)|—          |BR-MATCH-003|
|→ APPROVED                 |운영자 (MATCHING_MANAGEMENT)|멘토가 지정된 상태 |BR-MATCH-004|
|→ CANCELLED                |운영자 (MATCHING_MANAGEMENT)|—          |BR-MATCH-007|


> VR 참조: [VR-MATCH-REQUEST](./validation-rules.md#match-request)

-----

## Matching {#matching}

결제 완료 후 성립된 멘토-멘티 관계.

```
Matching (Aggregate Root)
├── id: MatchingId
├── matchRequestId: MatchRequestId
├── mentorAccountId: AccountId
├── menteeAccountId: AccountId
├── status: MatchingStatus         # ACTIVE / EXPIRED / ARCHIVED
├── validFrom: LocalDate
├── validUntil: LocalDate          # 결제 상품 기간 기준
├── extendable: Boolean            # 멘토가 설정
└── channels: List<MatchingChannel>  # Value Object
```

### MatchingChannel (Value Object) {#channel}

```
MatchingChannel
├── type: ChannelType              # Enum: FREE_CHAT / ONLINE_MEETING / ...
└── url: String
```

|필드  |타입    |제약              |
|----|------|----------------|
|type|Enum  |Not Null        |
|url |String|Not Null, URL 형식|

#### Matching 상태 전이 {#matching-status}

```
[ACTIVE] ──기간 만료──▶ [EXPIRED]
    │
    └──운영자 아카이빙──▶ [ARCHIVED]

연장: [ACTIVE] 중 extendable=true 이면 결제 후 validUntil 연장
```

|전이               |행위자                      |조건                       |참조 BR         |
|-----------------|-------------------------|-------------------------|--------------|
|생성 (ACTIVE)      |시스템                      |결제 완료                    |BR-PAYMENT-001|
|ACTIVE → EXPIRED |시스템                      |validUntil 경과            |BR-MATCH-005  |
|ACTIVE → ARCHIVED|운영자 (MATCHING_MANAGEMENT)|—                        |BR-MATCH-008  |
|validUntil 연장    |시스템                      |extendable=true, 연장 상품 결제|BR-PAYMENT-002|


> BR 참조: [BR-MATCH](./business-rules.md#매칭-matching)
> VR 참조: [VR-MATCHING](./validation-rules.md#matching)

-----

## InstantQA {#instant-qa}

```
InstantQA (Aggregate Root)
├── id: InstantQAId
├── menteeAccountId: AccountId
├── fieldId: FieldId              # 분야 지정 필수
├── content: String
├── postedAt: LocalDateTime
└── answers: List<QAAnswer>       # Entity
```

### QAAnswer (Entity)

```
QAAnswer
├── id: QAAnswerId
├── mentorAccountId: AccountId
├── content: String
└── answeredAt: LocalDateTime
```

|필드             |타입    |제약                               |
|---------------|------|---------------------------------|
|mentorAccountId|Long  |Not Null, 멘토당 InstantQA 1개 답변만 허용|
|content        |String|Not Null, Not Blank              |


> BR 참조: [BR-QA](./business-rules.md#인스턴스-qa-instant-qa)
> VR 참조: [VR-QA](./validation-rules.md#instant-qa)