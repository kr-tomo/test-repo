# REST API 개요

참조: [INDEX.md](./INDEX.md) | Use Cases: [use-cases.md](./use-cases.md)
API 설계 규칙: `.project/conventions/rest-api.md`

-----

## 공통 규칙

- 리소스는 복수 명사, 소문자·하이픈 사용
- 오류 응답 공통 형식:
  
  ```json
  { "code": "ERROR_CODE", "message": "설명", "status": 400 }
  ```
- 인증/인가 실패: 401 `UNAUTHORIZED` / 403 `FORBIDDEN`

-----

## 분야 (Field)

|Method|Path               |설명           |성공 코드|Actor                   |UC          |
|------|-------------------|-------------|-----|------------------------|------------|
|GET   |`/fields`          |분야 목록 조회 (계층)|200  |전체                      |UC-FIELD-004|
|POST  |`/fields`          |분야 생성        |201  |운영자 (ACCOUNT_MANAGEMENT)|UC-FIELD-001|
|PATCH |`/fields/{fieldId}`|분야 수정        |200  |운영자 (ACCOUNT_MANAGEMENT)|UC-FIELD-002|
|DELETE|`/fields/{fieldId}`|분야 비활성화      |204  |운영자 (ACCOUNT_MANAGEMENT)|UC-FIELD-003|

-----

## 멘토 경력 (Career)

|Method|Path                                               |설명               |성공 코드|Actor                   |UC           |
|------|---------------------------------------------------|-----------------|-----|------------------------|-------------|
|POST  |`/mentor-profiles/me/careers`                      |경력 등록 요청         |201  |멘토                      |UC-CAREER-001|
|PATCH |`/mentor-profiles/me/careers/{careerId}`           |경력 수정 (PENDING만) |200  |멘토                      |UC-CAREER-002|
|DELETE|`/mentor-profiles/me/careers/{careerId}`           |경력 철회 (PENDING만) |204  |멘토                      |UC-CAREER-003|
|PATCH |`/mentor-profiles/me/careers/{careerId}/visibility`|노출 설정 (APPROVED만)|200  |멘토                      |UC-CAREER-004|
|POST  |`/admin/careers/{careerId}/review`                 |경력 승인/반려         |200  |운영자 (ACCOUNT_MANAGEMENT)|UC-CAREER-005|

-----

## 멘토 분야 등록 (MentorField)

|Method|Path                                                                 |설명               |성공 코드|Actor                   |UC       |
|------|---------------------------------------------------------------------|-----------------|-----|------------------------|---------|
|POST  |`/mentor-profiles/me/field-registrations`                            |분야 등록 요청         |201  |멘토                      |UC-MF-001|
|PATCH |`/mentor-profiles/me/field-registrations/{registrationId}`           |분야 수정 (PENDING만) |200  |멘토                      |UC-MF-002|
|DELETE|`/mentor-profiles/me/field-registrations/{registrationId}`           |분야 철회 (PENDING만) |204  |멘토                      |UC-MF-003|
|PATCH |`/mentor-profiles/me/field-registrations/{registrationId}/visibility`|노출 설정 (APPROVED만)|200  |멘토                      |UC-MF-004|
|POST  |`/admin/field-registrations/{registrationId}/review`                 |분야 등록 승인/반려      |200  |운영자 (ACCOUNT_MANAGEMENT)|UC-MF-005|

-----

## 매칭 요청 (MatchRequest)

|Method|Path                                       |설명       |성공 코드|Actor                    |UC                        |
|------|-------------------------------------------|---------|-----|-------------------------|--------------------------|
|POST  |`/match-requests`                          |매칭 요청 생성 |201  |멘티                       |UC-MATCH-001, UC-MATCH-002|
|PATCH |`/admin/match-requests/{requestId}/mentor` |운영자 멘토 지정|200  |운영자 (MATCHING_MANAGEMENT)|UC-MATCH-003              |
|POST  |`/admin/match-requests/{requestId}/approve`|매칭 승인    |200  |운영자 (MATCHING_MANAGEMENT)|UC-MATCH-004              |

-----

## 매칭 (Matching)

|Method|Path                                   |설명               |성공 코드|Actor                    |UC          |
|------|---------------------------------------|-----------------|-----|-------------------------|------------|
|POST  |`/matchings`                           |멘토링 상품 결제 → 매칭 성립|201  |멘티                       |UC-MATCH-005|
|PATCH |`/matchings/{matchingId}/extendable`   |연장 가능 여부 설정      |200  |멘토                       |UC-MATCH-006|
|POST  |`/matchings/{matchingId}/extend`       |연장 상품 결제         |200  |멘티                       |UC-MATCH-007|
|DELETE|`/admin/matchings/{matchingId}`        |매칭 해지            |204  |운영자 (MATCHING_MANAGEMENT)|UC-MATCH-008|
|POST  |`/admin/matchings/{matchingId}/archive`|매칭 아카이빙          |200  |운영자 (MATCHING_MANAGEMENT)|UC-MATCH-009|

-----

## 인스턴스 Q&A (InstantQA)

|Method|Path                                    |설명                |성공 코드|Actor|UC       |
|------|----------------------------------------|------------------|-----|-----|---------|
|POST  |`/instant-qas`                          |Q&A 등록            |201  |멘티   |UC-QA-001|
|GET   |`/instant-qas`                          |조회 가능 Q&A 목록 (멘토용)|200  |멘토   |UC-QA-002|
|GET   |`/instant-qas/me`                       |본인 Q&A 목록 (멘티용)   |200  |멘티   |UC-QA-005|
|GET   |`/instant-qas/{qaId}/answers`           |특정 Q&A 답변 목록      |200  |멘티   |UC-QA-005|
|POST  |`/instant-qas/{qaId}/answers`           |Q&A 답변 작성         |201  |멘토   |UC-QA-003|
|PATCH |`/instant-qas/{qaId}/answers/{answerId}`|답변 수정             |200  |멘토   |UC-QA-004|

-----

## 공통 오류 응답 코드

|HTTP|오류 코드                    |설명                         |
|----|-------------------------|---------------------------|
|400 |`INVALID_INPUT`          |Validation 실패              |
|401 |`UNAUTHORIZED`           |인증 실패                      |
|403 |`FORBIDDEN`              |권한 없음                      |
|404 |`NOT_FOUND`              |리소스 없음 (세부 코드는 각 도메인 VR 참조)|
|409 |`CONFLICT`               |중복 또는 상태 충돌                |
|422 |`BUSINESS_RULE_VIOLATION`|BR 위반                      |