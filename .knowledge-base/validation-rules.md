# Validation Rules (VR)

참조: [INDEX.md](./INDEX.md) | 도메인 모델: [domain-model.md](./domain-model.md)

VR은 입력값의 형식·범위·존재 여부를 검증하는 규칙이다.
비즈니스 의미 규칙(조건 → 결과)은 [business-rules.md](./business-rules.md)를 참조한다.

-----

## Account {#account}

|VR-ID         |필드                 |규칙                                            |오류 메시지                    |오류 코드                   |
|--------------|-------------------|----------------------------------------------|--------------------------|------------------------|
|VR-ACCOUNT-001|email              |Not Null, Not Blank                           |“이메일은 필수입니다.”             |`EMAIL_REQUIRED`        |
|VR-ACCOUNT-002|email              |이메일 형식 (`RFC 5322`)                           |“올바른 이메일 형식이 아닙니다.”       |`EMAIL_INVALID_FORMAT`  |
|VR-ACCOUNT-003|email              |Unique (전체 계정)                                |“이미 사용 중인 이메일입니다.”        |`EMAIL_DUPLICATE`       |
|VR-ACCOUNT-004|role               |Not Null, Enum 값 중 하나 (MENTOR/MENTEE/OPERATOR)|“올바른 역할 값이 아닙니다.”         |`ROLE_INVALID`          |
|VR-ACCOUNT-005|operatorPermissions|role=OPERATOR가 아닌 경우 비어 있어야 함                 |“운영자 계정에만 권한을 설정할 수 있습니다.”|`PERMISSION_NOT_ALLOWED`|

-----

## Field {#field}

|VR-ID       |필드      |규칙                         |오류 메시지                        |오류 코드                   |
|------------|--------|---------------------------|------------------------------|------------------------|
|VR-FIELD-001|name    |Not Null, Not Blank        |“분야 이름은 필수입니다.”               |`FIELD_NAME_REQUIRED`   |
|VR-FIELD-002|name    |동일 parentId 내 Unique       |“같은 상위 분야에 동일한 이름의 분야가 존재합니다.”|`FIELD_NAME_DUPLICATE`  |
|VR-FIELD-003|name    |Max 100자                   |“분야 이름은 100자 이하여야 합니다.”       |`FIELD_NAME_TOO_LONG`   |
|VR-FIELD-004|parentId|존재하는 Field ID여야 함 (null 제외)|“존재하지 않는 상위 분야입니다.”           |`PARENT_FIELD_NOT_FOUND`|
|VR-FIELD-005|parentId|참조하는 상위 Field가 ACTIVE여야 함  |“비활성화된 상위 분야입니다.”             |`PARENT_FIELD_INACTIVE` |

-----

## Career {#career}

|VR-ID        |필드     |규칙                 |오류 메시지                  |오류 코드                    |
|-------------|-------|-------------------|------------------------|-------------------------|
|VR-CAREER-001|content|Not Null, Not Blank|“경력 내용은 필수입니다.”         |`CAREER_CONTENT_REQUIRED`|
|VR-CAREER-002|content|Max 2000자          |“경력 내용은 2000자 이하여야 합니다.”|`CAREER_CONTENT_TOO_LONG`|


> OQ 참조: [OQ-005](./open-questions.md#OQ-005) — 경력 content 최대 길이 확정 필요

-----

## MentorField (FieldRegistration) {#mentor-field}

|VR-ID              |필드     |규칙                                                 |오류 메시지                 |오류 코드                         |
|-------------------|-------|---------------------------------------------------|-----------------------|------------------------------|
|VR-MENTOR-FIELD-001|fieldId|Not Null                                           |“분야는 필수입니다.”           |`FIELD_REQUIRED`              |
|VR-MENTOR-FIELD-002|fieldId|존재하는 ACTIVE Field여야 함                              |“존재하지 않거나 비활성화된 분야입니다.”|`FIELD_NOT_FOUND_OR_INACTIVE` |
|VR-MENTOR-FIELD-003|fieldId|동일 멘토의 PENDING/APPROVED 상태 FieldRegistration과 중복 불가|“이미 등록 중이거나 승인된 분야입니다.”|`FIELD_REGISTRATION_DUPLICATE`|

-----

## MatchRequest {#match-request}

|VR-ID           |필드             |규칙                      |오류 메시지                 |오류 코드                        |
|----------------|---------------|------------------------|-----------------------|-----------------------------|
|VR-MATCH-REQ-001|fieldId        |Not Null                |“분야는 필수입니다.”           |`FIELD_REQUIRED`             |
|VR-MATCH-REQ-002|fieldId        |존재하는 ACTIVE Field여야 함   |“존재하지 않거나 비활성화된 분야입니다.”|`FIELD_NOT_FOUND_OR_INACTIVE`|
|VR-MATCH-REQ-003|mentorAccountId|지정 시 role=MENTOR 계정이어야 함|“멘토 계정만 지정할 수 있습니다.”   |`NOT_A_MENTOR`               |

-----

## Matching {#matching}

|VR-ID          |필드             |규칙                              |오류 메시지             |오류 코드                       |
|---------------|---------------|--------------------------------|-------------------|----------------------------|
|VR-MATCHING-001|matchRequestId |APPROVED 상태의 MatchRequest만 참조 가능|“승인된 매칭 요청이 아닙니다.” |`MATCH_REQUEST_NOT_APPROVED`|
|VR-MATCHING-002|channels[].url |Not Null, URL 형식                |“올바른 URL 형식이 아닙니다.”|`CHANNEL_URL_INVALID`       |
|VR-MATCHING-003|channels[].type|Not Null, Enum 값 중 하나           |“올바른 채널 유형이 아닙니다.” |`CHANNEL_TYPE_INVALID`      |

-----

## InstantQA {#instant-qa}

|VR-ID    |필드     |규칙                   |오류 메시지                  |오류 코드                        |
|---------|-------|---------------------|------------------------|-----------------------------|
|VR-QA-001|content|Not Null, Not Blank  |“질문 내용은 필수입니다.”         |`QA_CONTENT_REQUIRED`        |
|VR-QA-002|content|Max 1000자            |“질문 내용은 1000자 이하여야 합니다.”|`QA_CONTENT_TOO_LONG`        |
|VR-QA-003|fieldId|Not Null             |“분야는 필수입니다.”            |`FIELD_REQUIRED`             |
|VR-QA-004|fieldId|존재하는 ACTIVE Field여야 함|“존재하지 않거나 비활성화된 분야입니다.” |`FIELD_NOT_FOUND_OR_INACTIVE`|

### QAAnswer

|VR-ID        |필드             |규칙                             |오류 메시지                  |오류 코드                    |
|-------------|---------------|-------------------------------|------------------------|-------------------------|
|VR-QA-ANS-001|content        |Not Null, Not Blank            |“답변 내용은 필수입니다.”         |`ANSWER_CONTENT_REQUIRED`|
|VR-QA-ANS-002|content        |Max 2000자                      |“답변 내용은 2000자 이하여야 합니다.”|`ANSWER_CONTENT_TOO_LONG`|
|VR-QA-ANS-003|mentorAccountId|동일 InstantQA에 이미 답변한 멘토는 재답변 불가|“이미 답변한 질문입니다.”         |`ANSWER_ALREADY_EXISTS`  |


> OQ 참조: [OQ-006](./open-questions.md#OQ-006) — content 최대 길이 확정 필요