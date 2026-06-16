# SPEC: 멘토 프로필 — 경력·분야 등록 관리

버전: 0.1
상태: Draft
작성일: 2026-06-16
KB 참조: [kb/INDEX.md](./kb/INDEX.md) → [domain-model.md](./kb/domain-model.md#mentor-profile), [business-rules.md](./kb/business-rules.md#멘토-경력-등록-career)
선행 SPEC: [SPEC-01-field.md](./SPEC-01-field.md)

-----

## 1. 개요

### 1.1 목적

멘토가 자신의 전문성(경력, 분야)을 서비스에 등록하고 운영자 검토를 거쳐 공식 프로필로 확정되는 절차를 제공한다.
등록된 정보는 매칭·Q&A에서 멘토의 신뢰도 기반으로 활용된다.

### 1.2 범위

**포함**

- 경력(Career) 등록 요청·수정·철회·노출 설정 (멘토)
- 분야(FieldRegistration) 등록 요청·수정·철회·노출 설정 (멘토)
- 운영자의 경력·분야 등록 요청 승인/반려

**제외**

- 분야 카테고리 자체 관리 (→ SPEC-01)
- 멘토 계정 생성 및 인증 (별도 계정 SPEC)
- 매칭에서 멘토 프로필 조회 (→ SPEC-04)

### 1.3 용어 정의

|용어                      |정의                                       |
|------------------------|-----------------------------------------|
|경력(Career)              |멘토가 자유 기술로 등록하는 업무·학습 이력                 |
|분야 등록(FieldRegistration)|멘토가 서비스 분야 중 자신의 전문 분야를 등록 요청하는 레코드      |
|등록 요청 상태                |PENDING / APPROVED / REJECTED / WITHDRAWN|
|노출 설정(visible)          |APPROVED 상태 항목을 외부에 표시할지 멘토가 제어하는 플래그    |

-----

## 2. Actor & Use Case

### 2.1 Actor

|Actor                   |설명                       |
|------------------------|-------------------------|
|멘토                      |경력·분야를 등록 요청하고 자신의 항목을 관리|
|운영자 (ACCOUNT_MANAGEMENT)|등록 요청을 검토하여 승인 또는 반려     |

### 2.2 Use Case 목록

|UC-ID        |Actor                   |Use Case           |우선순위|
|-------------|------------------------|-------------------|----|
|UC-CAREER-001|멘토                      |경력 등록 요청           |Must|
|UC-CAREER-002|멘토                      |검토 전 경력 수정         |Must|
|UC-CAREER-003|멘토                      |검토 전 경력 철회         |Must|
|UC-CAREER-004|멘토                      |경력 노출 설정 변경        |Must|
|UC-CAREER-005|운영자 (ACCOUNT_MANAGEMENT)|경력 등록 요청 검토 (승인/반려)|Must|
|UC-MF-001    |멘토                      |분야 등록 요청           |Must|
|UC-MF-002    |멘토                      |검토 전 분야 수정         |Must|
|UC-MF-003    |멘토                      |검토 전 분야 철회         |Must|
|UC-MF-004    |멘토                      |분야 노출 설정 변경        |Must|
|UC-MF-005    |운영자 (ACCOUNT_MANAGEMENT)|분야 등록 요청 검토 (승인/반려)|Must|

-----

## 3. 비즈니스 규칙 (Business Rules)

### BR-001: 경력 등록 요청 — PENDING 생성

- **조건**: 멘토가 경력을 등록 요청하는 경우
- **결과**: CareerEntry가 PENDING 상태로 생성된다. 운영자 검토 전까지 PENDING 유지.
- **연관 UC**: UC-CAREER-001

### BR-002: 경력 수정 — PENDING 상태만 허용

- **조건**: 멘토가 자신의 경력 항목을 수정하려는 경우
- **결과**: 해당 CareerEntry의 상태가 PENDING인 경우에만 수정할 수 있다. APPROVED·REJECTED·WITHDRAWN이면 거부.
- **연관 UC**: UC-CAREER-002

### BR-003: 경력 철회 — PENDING 상태만 허용

- **조건**: 멘토가 자신의 경력 항목을 철회하려는 경우
- **결과**: 상태가 PENDING인 경우에만 WITHDRAWN으로 전환된다.
- **연관 UC**: UC-CAREER-003

### BR-004: 경력 노출 설정 — APPROVED 상태만 허용

- **조건**: 멘토가 자신의 경력 항목의 노출 여부를 변경하려는 경우
- **결과**: 상태가 APPROVED인 경우에만 visible 값을 변경할 수 있다.
- **연관 UC**: UC-CAREER-004

### BR-005: 경력 검토 — ACCOUNT_MANAGEMENT 권한 필요

- **조건**: 운영자가 경력 등록 요청을 승인 또는 반려하는 경우
- **결과**: `ACCOUNT_MANAGEMENT` 권한이 있어야 한다. 승인 시 APPROVED, 반려 시 REJECTED로 전환.
- **연관 UC**: UC-CAREER-005

### BR-006: 분야 등록 요청 — ACTIVE 분야만 허용

- **조건**: 멘토가 분야 등록을 요청하는 경우
- **결과**: 선택한 fieldId는 서비스에서 관리하는 ACTIVE 상태의 Field여야 한다.
- **연관 UC**: UC-MF-001

### BR-007: 분야 등록 중복 방지

- **조건**: 멘토가 이미 PENDING 또는 APPROVED 상태인 분야를 다시 등록 요청하는 경우
- **결과**: 중복 등록 요청은 거부된다.
- **예시**: fieldId=10이 APPROVED인데 같은 멘토가 fieldId=10을 다시 요청 → 거부.
- **연관 UC**: UC-MF-001
- **OQ**: OQ-002 — REJECTED/WITHDRAWN 상태의 분야 재등록 허용 여부 미확정. 현재 PENDING/APPROVED만 중복 차단으로 작성.

### BR-008: 분야 수정 — PENDING 상태만 허용

- **조건**: 멘토가 분야 등록 항목의 fieldId를 변경하려는 경우
- **결과**: 상태가 PENDING인 경우에만 수정할 수 있다.
- **연관 UC**: UC-MF-002

### BR-009: 분야 철회 — PENDING 상태만 허용

- **조건**: 멘토가 분야 등록 항목을 철회하려는 경우
- **결과**: 상태가 PENDING인 경우에만 WITHDRAWN으로 전환된다.
- **연관 UC**: UC-MF-003

### BR-010: 분야 노출 설정 — APPROVED 상태만 허용

- **조건**: 멘토가 분야 등록 항목의 노출 여부를 변경하려는 경우
- **결과**: 상태가 APPROVED인 경우에만 visible 값을 변경할 수 있다.
- **연관 UC**: UC-MF-004

### BR-011: 분야 검토 — ACCOUNT_MANAGEMENT 권한 필요

- **조건**: 운영자가 분야 등록 요청을 승인 또는 반려하는 경우
- **결과**: `ACCOUNT_MANAGEMENT` 권한이 있어야 한다. 승인 시 APPROVED(visible=true 기본값), 반려 시 REJECTED.
- **연관 UC**: UC-MF-005

### BR-012: 본인 항목만 수정·철회·노출 설정 가능

- **조건**: 멘토가 경력 또는 분야 항목을 수정·철회·노출 설정하는 경우
- **결과**: 해당 항목의 mentorProfileId가 요청자의 것이어야 한다. 타인 항목 조작 시 403.
- **연관 UC**: UC-CAREER-002~004, UC-MF-002~004

-----

## 4. Validation Rules

### 4.1 경력(Career) 등록/수정 요청

|필드     |규칙                 |오류 메시지                  |오류 코드                    |
|-------|-------------------|------------------------|-------------------------|
|content|Not Null, Not Blank|“경력 내용은 필수입니다.”         |`CAREER_CONTENT_REQUIRED`|
|content|Max 2000자          |“경력 내용은 2000자 이하여야 합니다.”|`CAREER_CONTENT_TOO_LONG`|


> OQ-005: content 최대 길이 2000자는 임시값. 확정 필요.

### 4.2 분야 등록(FieldRegistration) 요청/수정

|필드     |규칙                           |오류 메시지                 |오류 코드                         |
|-------|-----------------------------|-----------------------|------------------------------|
|fieldId|Not Null                     |“분야는 필수입니다.”           |`FIELD_REQUIRED`              |
|fieldId|존재하는 ACTIVE Field            |“존재하지 않거나 비활성화된 분야입니다.”|`FIELD_NOT_FOUND_OR_INACTIVE` |
|fieldId|동일 멘토의 PENDING/APPROVED 중복 불가|“이미 등록 중이거나 승인된 분야입니다.”|`FIELD_REGISTRATION_DUPLICATE`|

### 4.3 경력/분야 상태 전이 요청

|상황     |규칙                    |오류 메시지                      |오류 코드                          |
|-------|----------------------|----------------------------|-------------------------------|
|수정 요청 시|status = PENDING이어야 함 |“검토 전 상태인 항목만 수정할 수 있습니다.”  |`MODIFICATION_NOT_ALLOWED`     |
|철회 요청 시|status = PENDING이어야 함 |“검토 전 상태인 항목만 철회할 수 있습니다.”  |`WITHDRAWAL_NOT_ALLOWED`       |
|노출 설정 시|status = APPROVED이어야 함|“승인된 항목만 노출 설정을 변경할 수 있습니다.”|`VISIBILITY_CHANGE_NOT_ALLOWED`|

### 4.4 운영자 검토 요청

|필드    |규칙                               |오류 메시지                    |오류 코드                      |
|------|---------------------------------|--------------------------|---------------------------|
|action|Not Null, Enum (APPROVE / REJECT)|“검토 결과는 필수입니다.”           |`REVIEW_ACTION_REQUIRED`   |
|검토 대상 |status = PENDING이어야 함            |“검토 전 상태인 항목만 검토할 수 있습니다.”|`REVIEW_TARGET_NOT_PENDING`|

-----

## 5. 도메인 모델

### 5.1 Aggregate / Entity / Value Object

```
MentorProfile (Aggregate Root)
├── id: Long
├── accountId: Long                         # 멘토 Account 참조
├── careers: List<CareerEntry>              # Entity
└── fieldRegistrations: List<FieldRegistration>  # Entity
```

#### MentorProfile

|필드       |타입  |설명      |제약                            |
|---------|----|--------|------------------------------|
|id       |Long|식별자     |PK, Not Null                  |
|accountId|Long|멘토 계정 ID|Not Null, Unique, FK → account|

#### CareerEntry (Entity)

|필드             |타입                |설명              |제약                                    |
|---------------|------------------|----------------|--------------------------------------|
|id             |Long              |식별자             |PK, Not Null                          |
|mentorProfileId|Long              |소유 MentorProfile|Not Null, FK                          |
|content        |String            |자유 기술 경력 내용     |Not Null, Max 2000자                   |
|status         |RegistrationStatus|등록 상태           |Not Null, 기본값: PENDING                |
|visible        |Boolean           |노출 여부           |Not Null, 기본값: true (APPROVED 전환 시 설정)|
|createdAt      |LocalDateTime     |생성 시각           |Not Null                              |
|updatedAt      |LocalDateTime     |수정 시각           |Not Null                              |

#### FieldRegistration (Entity)

|필드             |타입                |설명              |제약                                    |
|---------------|------------------|----------------|--------------------------------------|
|id             |Long              |식별자             |PK, Not Null                          |
|mentorProfileId|Long              |소유 MentorProfile|Not Null, FK                          |
|fieldId        |Long              |서비스 분야 ID       |Not Null, FK → field                  |
|status         |RegistrationStatus|등록 상태           |Not Null, 기본값: PENDING                |
|visible        |Boolean           |노출 여부           |Not Null, 기본값: true (APPROVED 전환 시 설정)|
|createdAt      |LocalDateTime     |생성 시각           |Not Null                              |
|updatedAt      |LocalDateTime     |수정 시각           |Not Null                              |

#### RegistrationStatus (Enum)

|값        |설명           |
|---------|-------------|
|PENDING  |운영자 검토 대기 중  |
|APPROVED |승인 완료 (정식 등록)|
|REJECTED |반려됨          |
|WITHDRAWN|멘토가 직접 철회    |

### 5.2 상태 전이

```
[PENDING] ──운영자 승인──▶ [APPROVED]  (visible 기본값: true)
    │
    ├──운영자 반려──▶ [REJECTED]
    └──멘토 철회──▶ [WITHDRAWN]
```

|전이                   |행위자|조건                   |연관 BR                 |
|---------------------|---|---------------------|----------------------|
|생성 → PENDING         |멘토 |—                    |BR-001, BR-006        |
|PENDING → APPROVED   |운영자|ACCOUNT_MANAGEMENT 권한|BR-005, BR-011        |
|PENDING → REJECTED   |운영자|ACCOUNT_MANAGEMENT 권한|BR-005, BR-011        |
|PENDING → WITHDRAWN  |멘토 |본인 항목                |BR-003, BR-009, BR-012|
|APPROVED → visible 변경|멘토 |본인 항목, APPROVED 상태   |BR-004, BR-010, BR-012|

-----

## 6. 아키텍처 및 컴포넌트

### 6.1 레이어별 컴포넌트

|레이어                |컴포넌트                                |역할                     |
|-------------------|------------------------------------|-----------------------|
|Inbound Adapter    |`MentorCareerController`            |경력 관련 HTTP 요청 수신       |
|Inbound Adapter    |`MentorFieldRegistrationController` |분야 등록 관련 HTTP 요청 수신    |
|Inbound Adapter    |`AdminMentorProfileReviewController`|운영자 검토 HTTP 요청 수신      |
|Inbound Port       |`RegisterCareerUseCase`             |경력 등록 요청               |
|Inbound Port       |`ModifyCareerUseCase`               |경력 수정/철회/노출 설정         |
|Inbound Port       |`ReviewCareerUseCase`               |경력 운영자 검토              |
|Inbound Port       |`RegisterFieldUseCase`              |분야 등록 요청               |
|Inbound Port       |`ModifyFieldRegistrationUseCase`    |분야 수정/철회/노출 설정         |
|Inbound Port       |`ReviewFieldRegistrationUseCase`    |분야 운영자 검토              |
|Application Service|`MentorCareerService`               |경력 Use Case 구현         |
|Application Service|`MentorFieldRegistrationService`    |분야 Use Case 구현         |
|Domain             |`MentorProfile`                     |Aggregate Root         |
|Domain             |`CareerEntry`                       |Entity                 |
|Domain             |`FieldRegistration`                 |Entity                 |
|Outbound Port      |`MentorProfileRepositoryPort`       |MentorProfile 영속성 인터페이스|
|Outbound Adapter   |`MentorProfilePersistenceAdapter`   |JPA 구현                 |

### 6.2 패키지 경로

```
com.<organization>.<app>/
└── mentor/
    ├── domain/
    │   ├── model/
    │   │   ├── MentorProfile.java
    │   │   ├── CareerEntry.java
    │   │   ├── FieldRegistration.java
    │   │   └── RegistrationStatus.java
    │   └── port/
    │       ├── in/
    │       │   ├── RegisterCareerUseCase.java
    │       │   ├── ModifyCareerUseCase.java
    │       │   ├── ReviewCareerUseCase.java
    │       │   ├── RegisterFieldUseCase.java
    │       │   ├── ModifyFieldRegistrationUseCase.java
    │       │   └── ReviewFieldRegistrationUseCase.java
    │       └── out/
    │           └── MentorProfileRepositoryPort.java
    ├── application/
    │   └── service/
    │       ├── MentorCareerService.java
    │       └── MentorFieldRegistrationService.java
    └── adapter/
        ├── in/web/
        │   ├── MentorCareerController.java
        │   ├── MentorFieldRegistrationController.java
        │   └── AdminMentorProfileReviewController.java
        └── out/persistence/
            └── MentorProfilePersistenceAdapter.java
```

-----

## 7. REST API

### 7.1 엔드포인트 목록

|Method|Path                                                                 |설명                 |성공 코드|연관 UC        |
|------|---------------------------------------------------------------------|-------------------|-----|-------------|
|POST  |`/mentor-profiles/me/careers`                                        |경력 등록 요청           |201  |UC-CAREER-001|
|PATCH |`/mentor-profiles/me/careers/{careerId}`                             |경력 내용 수정 (PENDING) |200  |UC-CAREER-002|
|DELETE|`/mentor-profiles/me/careers/{careerId}`                             |경력 철회 (PENDING)    |204  |UC-CAREER-003|
|PATCH |`/mentor-profiles/me/careers/{careerId}/visibility`                  |경력 노출 설정 (APPROVED)|200  |UC-CAREER-004|
|POST  |`/admin/careers/{careerId}/review`                                   |경력 등록 요청 검토        |200  |UC-CAREER-005|
|POST  |`/mentor-profiles/me/field-registrations`                            |분야 등록 요청           |201  |UC-MF-001    |
|PATCH |`/mentor-profiles/me/field-registrations/{registrationId}`           |분야 수정 (PENDING)    |200  |UC-MF-002    |
|DELETE|`/mentor-profiles/me/field-registrations/{registrationId}`           |분야 철회 (PENDING)    |204  |UC-MF-003    |
|PATCH |`/mentor-profiles/me/field-registrations/{registrationId}/visibility`|분야 노출 설정 (APPROVED)|200  |UC-MF-004    |
|POST  |`/admin/field-registrations/{registrationId}/review`                 |분야 등록 요청 검토        |200  |UC-MF-005    |

### 7.2 요청/응답 스키마

#### POST `/mentor-profiles/me/careers` — 요청

```json
{
  "content": "카카오 백엔드 개발자 3년. Java/Spring 기반 서비스 개발 및 운영 경험."
}
```

|필드     |타입    |필수|설명         |연관 VR                                           |
|-------|------|--|-----------|------------------------------------------------|
|content|String|Y |자유 기술 경력 내용|CAREER_CONTENT_REQUIRED, CAREER_CONTENT_TOO_LONG|

#### POST `/mentor-profiles/me/careers` — 응답 (201)

```json
{
  "id": 1,
  "content": "카카오 백엔드 개발자 3년. Java/Spring 기반 서비스 개발 및 운영 경험.",
  "status": "PENDING",
  "visible": null
}
```

> visible은 APPROVED 전까지 null 또는 미포함.

#### PATCH `/mentor-profiles/me/careers/{careerId}` — 요청

```json
{
  "content": "수정된 경력 내용"
}
```

#### PATCH `/mentor-profiles/me/careers/{careerId}/visibility` — 요청

```json
{
  "visible": false
}
```

|필드     |타입     |필수|설명                 |
|-------|-------|--|-------------------|
|visible|Boolean|Y |true: 노출, false: 숨김|

#### POST `/admin/careers/{careerId}/review` — 요청

```json
{
  "action": "APPROVE"
}
```

|필드    |타입  |필수|설명               |
|------|----|--|-----------------|
|action|Enum|Y |APPROVE 또는 REJECT|

#### POST `/admin/careers/{careerId}/review` — 응답 (200)

```json
{
  "id": 1,
  "status": "APPROVED"
}
```

#### POST `/mentor-profiles/me/field-registrations` — 요청

```json
{
  "fieldId": 10
}
```

|필드     |타입  |필수|설명       |연관 VR                                                                    |
|-------|----|--|---------|-------------------------------------------------------------------------|
|fieldId|Long|Y |등록할 분야 ID|FIELD_REQUIRED, FIELD_NOT_FOUND_OR_INACTIVE, FIELD_REGISTRATION_DUPLICATE|

#### POST `/mentor-profiles/me/field-registrations` — 응답 (201)

```json
{
  "id": 5,
  "fieldId": 10,
  "fieldName": "백엔드",
  "status": "PENDING",
  "visible": null
}
```

#### 오류 응답 (공통)

```json
{
  "code": "MODIFICATION_NOT_ALLOWED",
  "message": "검토 전 상태인 항목만 수정할 수 있습니다.",
  "status": 422
}
```

|HTTP|오류 코드                          |발생 조건                      |
|----|-------------------------------|---------------------------|
|400 |`CAREER_CONTENT_REQUIRED`      |content 누락                 |
|400 |`CAREER_CONTENT_TOO_LONG`      |content 2000자 초과           |
|400 |`FIELD_REQUIRED`               |fieldId 누락                 |
|400 |`REVIEW_ACTION_REQUIRED`       |action 누락                  |
|403 |`FORBIDDEN`                    |본인 항목 아님 또는 권한 없음          |
|404 |`CAREER_NOT_FOUND`             |존재하지 않는 careerId           |
|404 |`FIELD_REGISTRATION_NOT_FOUND` |존재하지 않는 registrationId     |
|409 |`FIELD_REGISTRATION_DUPLICATE` |PENDING/APPROVED 분야 중복 등록  |
|422 |`FIELD_NOT_FOUND_OR_INACTIVE`  |INACTIVE 또는 존재하지 않는 fieldId|
|422 |`MODIFICATION_NOT_ALLOWED`     |PENDING이 아닌 항목 수정 시도       |
|422 |`WITHDRAWAL_NOT_ALLOWED`       |PENDING이 아닌 항목 철회 시도       |
|422 |`VISIBILITY_CHANGE_NOT_ALLOWED`|APPROVED가 아닌 항목 노출 설정 시도   |
|422 |`REVIEW_TARGET_NOT_PENDING`    |PENDING이 아닌 항목 검토 시도       |

-----

## 8. 영속화

### 8.1 테이블 설계

```sql
CREATE TABLE mentor_profile (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    account_id  BIGINT      NOT NULL UNIQUE,
    created_at  DATETIME    NOT NULL,
    updated_at  DATETIME    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE career_entry (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    mentor_profile_id   BIGINT          NOT NULL,
    content             TEXT            NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    visible             TINYINT(1)      NULL,
    created_at          DATETIME        NOT NULL,
    updated_at          DATETIME        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (mentor_profile_id) REFERENCES mentor_profile(id)
);

CREATE TABLE field_registration (
    id                  BIGINT      NOT NULL AUTO_INCREMENT,
    mentor_profile_id   BIGINT      NOT NULL,
    field_id            BIGINT      NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    visible             TINYINT(1)  NULL,
    created_at          DATETIME    NOT NULL,
    updated_at          DATETIME    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (mentor_profile_id) REFERENCES mentor_profile(id),
    FOREIGN KEY (field_id) REFERENCES field(id)
);
```

### 8.2 인덱스

|인덱스명                              |대상 컬럼                                |목적                                               |
|----------------------------------|-------------------------------------|-------------------------------------------------|
|`idx_career_mentor_profile_id`    |career_entry.mentor_profile_id       |멘토별 경력 조회                                        |
|`idx_career_status`               |career_entry.status                  |상태별 경력 조회 (운영자 검토 큐)                             |
|`idx_field_reg_mentor_profile_id` |field_registration.mentor_profile_id |멘토별 분야 등록 조회                                     |
|`uk_field_reg_mentor_field_active`|(mentor_profile_id, field_id, status)|중복 등록 방지 인덱스 (PENDING/APPROVED 유일성은 애플리케이션 레벨 적용)|

### 8.3 트랜잭션 경계

|Use Case           |트랜잭션 범위                                  |비고                        |
|-------------------|-----------------------------------------|--------------------------|
|UC-CAREER-001 경력 등록|MentorCareerService.register() 전체        |CareerEntry 단건 저장         |
|UC-CAREER-002 경력 수정|MentorCareerService.modify() 전체          |CareerEntry 단건 수정         |
|UC-CAREER-003 경력 철회|MentorCareerService.withdraw() 전체        |status → WITHDRAWN        |
|UC-CAREER-004 노출 설정|MentorCareerService.updateVisibility() 전체|visible 단건 수정             |
|UC-CAREER-005 검토   |MentorCareerService.review() 전체          |status → APPROVED/REJECTED|
|UC-MF-001~005      |MentorFieldRegistrationService 각 메서드     |동일 패턴 적용                  |

-----

## 11. 미결 항목 (Open Questions)

|OQ-ID |질문                             |선택지              |영향                             |
|------|-------------------------------|-----------------|-------------------------------|
|OQ-002|REJECTED/WITHDRAWN 분야 재등록 허용 여부|A: 재등록 허용 / B: 불가|BR-007, VR 중복 체크 조건            |
|OQ-005|경력 content 최대 길이               |현재 임시 2000자      |content 컬럼 타입 (TEXT vs VARCHAR)|