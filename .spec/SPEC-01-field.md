# SPEC: 분야(Field) 관리

버전: 0.1
상태: Draft
작성일: 2026-06-16
KB 참조: [kb/INDEX.md](./kb/INDEX.md) → [domain-model.md](./kb/domain-model.md#field), [business-rules.md](./kb/business-rules.md#분야-field)

-----

## 1. 개요

### 1.1 목적

멘토링 서비스에서 멘토의 전문 분야와 멘티의 관심 분야를 체계적으로 분류하기 위한 계층형 카테고리 시스템을 제공한다.
분야는 운영자가 단독으로 관리하며, 멘토 분야 등록·Q&A·매칭 요청 등 모든 분야 기반 기능의 기준점이 된다.

### 1.2 범위

**포함**

- 분야 생성·수정·비활성화 (운영자)
- 분야 계층 조회 (전체 사용자)
- 상위 분야 비활성화 시 하위 분야 연쇄 처리

**제외**

- 멘토의 분야 등록 요청 (→ SPEC-02)
- 분야 기반 매칭 요청 (→ SPEC-04)
- 분야 기반 Q&A 등록 (→ SPEC-03)

### 1.3 용어 정의

|용어       |정의                                            |
|---------|----------------------------------------------|
|분야(Field)|서비스가 계층적 카테고리로 관리하는 전문 영역. 멘토 등록·매칭·Q&A의 기준 단위|
|최상위 분야   |parentId가 null인 분야                            |
|하위 분야    |parentId가 존재하는 분야                             |
|ACTIVE   |멘토 등록 및 서비스 내 참조가 가능한 정상 상태                   |
|INACTIVE |비활성화되어 신규 참조가 불가한 상태                          |

-----

## 2. Actor & Use Case

### 2.1 Actor

|Actor                   |설명                                         |
|------------------------|-------------------------------------------|
|운영자 (ACCOUNT_MANAGEMENT)|`ACCOUNT_MANAGEMENT` 권한을 가진 운영자. 분야 CRUD 전담|
|전체 사용자                  |멘토·멘티·운영자 포함. 분야 목록 조회만 허용                 |

### 2.2 Use Case 목록

|UC-ID       |Actor                   |Use Case     |우선순위|
|------------|------------------------|-------------|----|
|UC-FIELD-001|운영자 (ACCOUNT_MANAGEMENT)|분야 생성        |Must|
|UC-FIELD-002|운영자 (ACCOUNT_MANAGEMENT)|분야 수정        |Must|
|UC-FIELD-003|운영자 (ACCOUNT_MANAGEMENT)|분야 비활성화      |Must|
|UC-FIELD-004|전체 사용자                  |분야 목록 조회 (계층)|Must|

-----

## 3. 비즈니스 규칙 (Business Rules)

### BR-001: 분야 관리 권한

- **조건**: 분야를 생성·수정·비활성화하려는 경우
- **결과**: `ACCOUNT_MANAGEMENT` 권한을 가진 운영자만 처리할 수 있다. 권한이 없으면 403을 반환한다.
- **연관 UC**: UC-FIELD-001, UC-FIELD-002, UC-FIELD-003

### BR-002: 상위 분야 존재 및 활성 검증

- **조건**: 하위 분야를 생성할 때 parentId를 지정하는 경우
- **결과**: 지정한 parentId는 존재하고 ACTIVE 상태인 Field여야 한다. 그렇지 않으면 생성이 거부된다.
- **연관 UC**: UC-FIELD-001

### BR-003: 동일 계층 내 이름 중복 금지

- **조건**: 분야를 생성하거나 이름을 수정하는 경우
- **결과**: 동일 parentId(최상위 분야끼리는 parentId=null) 안에서 name이 중복될 수 없다.
- **연관 UC**: UC-FIELD-001, UC-FIELD-002

### BR-004: 상위 분야 비활성화 시 하위 연쇄 비활성화

- **조건**: 운영자가 분야를 비활성화(INACTIVE)하는 경우
- **결과**: 해당 분야의 모든 하위 분야도 재귀적으로 INACTIVE로 전환된다.
- **예시**: “개발” 분야를 INACTIVE로 전환하면 “백엔드”, “프론트엔드” 등 모든 하위 분야가 함께 INACTIVE.
- **연관 UC**: UC-FIELD-003
- **OQ**: OQ-001 — 연쇄 처리 방식이 선택지 A(자동 INACTIVE)로 결정된 것을 전제로 작성. 변경 시 이 BR 수정 필요.

### BR-005: INACTIVE 분야 신규 참조 금지

- **조건**: 멘토 분야 등록, Q&A 등록, 매칭 요청 시 fieldId를 지정하는 경우
- **결과**: 지정된 분야가 INACTIVE이면 해당 요청이 거부된다. (각 SPEC에서 VR로 적용)
- **연관 UC**: 해당 없음 (다른 SPEC의 VR에서 적용)

-----

## 4. Validation Rules

### 4.1 Field 생성/수정 요청

|필드      |규칙                     |오류 메시지                        |오류 코드                   |
|--------|-----------------------|------------------------------|------------------------|
|name    |Not Null, Not Blank    |“분야 이름은 필수입니다.”               |`FIELD_NAME_REQUIRED`   |
|name    |Max 100자               |“분야 이름은 100자 이하여야 합니다.”       |`FIELD_NAME_TOO_LONG`   |
|name    |동일 parentId 내 Unique   |“같은 상위 분야에 동일한 이름의 분야가 존재합니다.”|`FIELD_NAME_DUPLICATE`  |
|parentId|존재하는 Field ID (null 제외)|“존재하지 않는 상위 분야입니다.”           |`PARENT_FIELD_NOT_FOUND`|
|parentId|참조 Field가 ACTIVE       |“비활성화된 상위 분야입니다.”             |`PARENT_FIELD_INACTIVE` |

### 4.2 Field 비활성화 요청

|필드            |규칙              |오류 메시지           |오류 코드                   |
|--------------|----------------|-----------------|------------------------|
|fieldId (path)|존재하는 Field ID   |“존재하지 않는 분야입니다.” |`FIELD_NOT_FOUND`       |
|fieldId (path)|이미 INACTIVE이면 거부|“이미 비활성화된 분야입니다.”|`FIELD_ALREADY_INACTIVE`|

-----

## 5. 도메인 모델

### 5.1 Aggregate / Entity / Value Object

```
Field (Aggregate Root)
├── id: Long
├── name: String
├── parentId: Long | null
└── status: FieldStatus         # Enum: ACTIVE / INACTIVE
```

#### Field

|필드      |타입         |설명      |제약                          |
|--------|-----------|--------|----------------------------|
|id      |Long       |식별자     |PK, Not Null, Auto Increment|
|name    |String     |분야 이름   |Not Null, Max 100자          |
|parentId|Long       |상위 분야 ID|Nullable (최상위이면 null)       |
|status  |FieldStatus|활성화 상태  |Not Null, 기본값: ACTIVE       |

### 5.2 상태 전이

```
[ACTIVE] ──운영자 비활성화──▶ [INACTIVE]
```

|전이               |행위자|조건                   |연관 BR         |
|-----------------|---|---------------------|--------------|
|생성 시 → ACTIVE    |운영자|—                    |BR-001        |
|ACTIVE → INACTIVE|운영자|ACCOUNT_MANAGEMENT 권한|BR-001, BR-004|


> INACTIVE → ACTIVE 재활성화는 현재 범위 제외. (OQ-001과 독립)

-----

## 6. 아키텍처 및 컴포넌트

### 6.1 레이어별 컴포넌트

|레이어                |컴포넌트                     |역할                 |
|-------------------|-------------------------|-------------------|
|Inbound Adapter    |`FieldController`        |HTTP 요청 수신         |
|Inbound Port       |`CreateFieldUseCase`     |분야 생성              |
|Inbound Port       |`UpdateFieldUseCase`     |분야 수정              |
|Inbound Port       |`DeactivateFieldUseCase` |분야 비활성화            |
|Inbound Port       |`GetFieldsUseCase`       |분야 목록 조회           |
|Application Service|`FieldService`           |Use Case 구현, 도메인 조율|
|Domain             |`Field`                  |비즈니스 로직, 상태 관리     |
|Outbound Port      |`FieldRepositoryPort`    |영속성 인터페이스          |
|Outbound Adapter   |`FieldPersistenceAdapter`|JPA 구현             |

### 6.2 패키지 경로

```
com.<organization>.<app>/
└── field/
    ├── domain/
    │   ├── model/
    │   │   ├── Field.java
    │   │   └── FieldStatus.java
    │   └── port/
    │       ├── in/
    │       │   ├── CreateFieldUseCase.java
    │       │   ├── UpdateFieldUseCase.java
    │       │   ├── DeactivateFieldUseCase.java
    │       │   └── GetFieldsUseCase.java
    │       └── out/
    │           └── FieldRepositoryPort.java
    ├── application/
    │   └── service/
    │       └── FieldService.java
    └── adapter/
        ├── in/web/
        │   └── FieldController.java
        └── out/persistence/
            └── FieldPersistenceAdapter.java
```

-----

## 7. REST API

### 7.1 엔드포인트 목록

|Method|Path               |설명              |성공 코드|연관 UC       |
|------|-------------------|----------------|-----|------------|
|GET   |`/fields`          |분야 목록 조회 (계층 트리)|200  |UC-FIELD-004|
|POST  |`/fields`          |분야 생성           |201  |UC-FIELD-001|
|PATCH |`/fields/{fieldId}`|분야 수정           |200  |UC-FIELD-002|
|DELETE|`/fields/{fieldId}`|분야 비활성화         |204  |UC-FIELD-003|

### 7.2 요청/응답 스키마

#### POST `/fields` — 요청

```json
{
  "name": "백엔드",
  "parentId": 1
}
```

|필드      |타입    |필수|설명                  |연관 VR             |
|--------|------|--|--------------------|------------------|
|name    |String|Y |분야 이름               |VR: name 규칙 전체    |
|parentId|Long  |N |상위 분야 ID. null이면 최상위|VR: parentId 규칙 전체|

#### POST `/fields` — 응답 (201)

```json
{
  "id": 10,
  "name": "백엔드",
  "parentId": 1,
  "status": "ACTIVE"
}
```

#### PATCH `/fields/{fieldId}` — 요청

```json
{
  "name": "백엔드 개발"
}
```

|필드  |타입    |필수|설명       |연관 VR         |
|----|------|--|---------|--------------|
|name|String|Y |변경할 분야 이름|VR: name 규칙 전체|

#### PATCH `/fields/{fieldId}` — 응답 (200)

```json
{
  "id": 10,
  "name": "백엔드 개발",
  "parentId": 1,
  "status": "ACTIVE"
}
```

#### GET `/fields` — 응답 (200)

계층 트리 구조로 반환한다.

```json
[
  {
    "id": 1,
    "name": "개발",
    "parentId": null,
    "status": "ACTIVE",
    "children": [
      {
        "id": 10,
        "name": "백엔드",
        "parentId": 1,
        "status": "ACTIVE",
        "children": []
      }
    ]
  }
]
```

#### 오류 응답

```json
{
  "code": "FIELD_NAME_DUPLICATE",
  "message": "같은 상위 분야에 동일한 이름의 분야가 존재합니다.",
  "status": 409
}
```

|HTTP|오류 코드                   |발생 조건                   |
|----|------------------------|------------------------|
|400 |`FIELD_NAME_REQUIRED`   |name 누락                 |
|400 |`FIELD_NAME_TOO_LONG`   |name 100자 초과            |
|403 |`FORBIDDEN`             |ACCOUNT_MANAGEMENT 권한 없음|
|404 |`FIELD_NOT_FOUND`       |존재하지 않는 fieldId         |
|404 |`PARENT_FIELD_NOT_FOUND`|존재하지 않는 parentId        |
|409 |`FIELD_NAME_DUPLICATE`  |동일 계층 name 중복           |
|409 |`FIELD_ALREADY_INACTIVE`|이미 비활성화된 분야             |
|422 |`PARENT_FIELD_INACTIVE` |INACTIVE 상위 분야 지정       |

-----

## 8. 영속화

### 8.1 테이블 설계

```sql
CREATE TABLE field (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    parent_id   BIGINT          NULL,
    status      VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    created_at  DATETIME        NOT NULL,
    updated_at  DATETIME        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES field(id)
);
```

|컬럼        |타입          |제약                        |설명               |
|----------|------------|--------------------------|-----------------|
|id        |BIGINT      |PK, NOT NULL              |식별자              |
|name      |VARCHAR(100)|NOT NULL                  |분야 이름            |
|parent_id |BIGINT      |NULL, FK → field(id)      |상위 분야 ID         |
|status    |VARCHAR(20) |NOT NULL, DEFAULT ‘ACTIVE’|ACTIVE / INACTIVE|
|created_at|DATETIME    |NOT NULL                  |생성 시각            |
|updated_at|DATETIME    |NOT NULL                  |수정 시각            |

### 8.2 인덱스

|인덱스명                     |대상 컬럼            |목적                    |
|-------------------------|-----------------|----------------------|
|`idx_field_parent_id`    |parent_id        |하위 분야 조회 성능           |
|`uk_field_name_parent_id`|(name, parent_id)|동일 계층 내 name Unique 보장|

### 8.3 트랜잭션 경계

|Use Case         |트랜잭션 범위                     |비고                             |
|-----------------|----------------------------|-------------------------------|
|UC-FIELD-001 생성  |FieldService.create() 전체    |Field 단건 저장                    |
|UC-FIELD-002 수정  |FieldService.update() 전체    |Field 단건 수정                    |
|UC-FIELD-003 비활성화|FieldService.deactivate() 전체|대상 + 하위 분야 전체 INACTIVE. 단일 트랜잭션|

-----

## 11. 미결 항목 (Open Questions)

|OQ-ID |질문                   |선택지                                        |영향                  |
|------|---------------------|-------------------------------------------|--------------------|
|OQ-001|상위 분야 비활성화 시 하위 처리 방식|A: 자동 연쇄 INACTIVE (현재 BR-004 가정) / B: 독립 유지|BR-004, UC-FIELD-003|