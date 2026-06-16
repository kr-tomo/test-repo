# Use Case 목록

참조: [INDEX.md](./INDEX.md) | Actor: [actors.md](./actors.md) | BR: [business-rules.md](./business-rules.md)

-----

## 분야 관리 (Field)

|UC-ID       |Actor                   |Use Case     |우선순위|연관 BR                     |
|------------|------------------------|-------------|----|--------------------------|
|UC-FIELD-001|운영자 (ACCOUNT_MANAGEMENT)|분야 생성        |Must|BR-FIELD-001              |
|UC-FIELD-002|운영자 (ACCOUNT_MANAGEMENT)|분야 수정        |Must|BR-FIELD-001              |
|UC-FIELD-003|운영자 (ACCOUNT_MANAGEMENT)|분야 비활성화      |Must|BR-FIELD-001, BR-FIELD-003|
|UC-FIELD-004|전체                      |분야 목록 조회 (계층)|Must|—                         |

-----

## 멘토 경력 관리 (Career)

|UC-ID        |Actor                   |Use Case           |우선순위|연관 BR                   |
|-------------|------------------------|-------------------|----|------------------------|
|UC-CAREER-001|멘토                      |경력 등록 요청           |Must|BR-CAREER-001           |
|UC-CAREER-002|멘토                      |검토 전 경력 수정         |Must|BR-CAREER-002           |
|UC-CAREER-003|멘토                      |검토 전 경력 철회         |Must|BR-CAREER-003           |
|UC-CAREER-004|멘토                      |경력 노출 설정 변경        |Must|BR-CAREER-004           |
|UC-CAREER-005|운영자 (ACCOUNT_MANAGEMENT)|경력 등록 요청 검토 (승인/반려)|Must|BR-OP-001, BR-CAREER-001|

-----

## 멘토 분야 등록 관리 (MentorField)

|UC-ID    |Actor                   |Use Case           |우선순위|연관 BR                                                 |
|---------|------------------------|-------------------|----|------------------------------------------------------|
|UC-MF-001|멘토                      |분야 등록 요청           |Must|BR-MENTOR-FIELD-001, BR-FIELD-002, BR-MENTOR-FIELD-005|
|UC-MF-002|멘토                      |검토 전 분야 수정         |Must|BR-MENTOR-FIELD-002                                   |
|UC-MF-003|멘토                      |검토 전 분야 철회         |Must|BR-MENTOR-FIELD-003                                   |
|UC-MF-004|멘토                      |분야 노출 설정 변경        |Must|BR-MENTOR-FIELD-004                                   |
|UC-MF-005|운영자 (ACCOUNT_MANAGEMENT)|분야 등록 요청 검토 (승인/반려)|Must|BR-OP-001, BR-MENTOR-FIELD-001                        |

-----

## 매칭 (Matching)

|UC-ID       |Actor                    |Use Case         |우선순위  |연관 BR                  |
|------------|-------------------------|-----------------|------|-----------------------|
|UC-MATCH-001|멘티                       |분야 기반 매칭 요청      |Must  |BR-MATCH-001           |
|UC-MATCH-002|멘티                       |Q&A 답변 멘토에게 매칭 요청|Must  |BR-MATCH-002, BR-QA-006|
|UC-MATCH-003|운영자 (MATCHING_MANAGEMENT)|매칭 요청에 멘토 지정     |Must  |BR-OP-002, BR-MATCH-003|
|UC-MATCH-004|운영자 (MATCHING_MANAGEMENT)|매칭 승인            |Must  |BR-OP-002, BR-MATCH-004|
|UC-MATCH-005|멘티                       |멘토링 상품 결제 (매칭 성립)|Must  |BR-PAYMENT-001         |
|UC-MATCH-006|멘토                       |매칭 연장 가능 여부 설정   |Must  |BR-MATCH-006           |
|UC-MATCH-007|멘티                       |연장 상품 결제         |Must  |BR-PAYMENT-002         |
|UC-MATCH-008|운영자 (MATCHING_MANAGEMENT)|매칭 해지            |Must  |BR-OP-002, BR-MATCH-007|
|UC-MATCH-009|운영자 (MATCHING_MANAGEMENT)|매칭 아카이빙          |Should|BR-OP-002, BR-MATCH-008|

-----

## 인스턴스 Q&A (InstantQA)

|UC-ID    |Actor|Use Case       |우선순위|연관 BR    |
|---------|-----|---------------|----|---------|
|UC-QA-001|멘티   |Q&A 등록         |Must|BR-QA-001|
|UC-QA-002|멘토   |조회 가능 Q&A 목록 조회|Must|BR-QA-002|
|UC-QA-003|멘토   |Q&A 답변 작성      |Must|BR-QA-003|
|UC-QA-004|멘토   |자신의 답변 수정      |Must|BR-QA-004|
|UC-QA-005|멘티   |자신의 Q&A 답변 확인  |Must|BR-QA-005|