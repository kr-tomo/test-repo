# SPEC 목록

KB 참조: [kb/INDEX.md](./kb/INDEX.md)

-----

## 도출 기준

KB의 도메인 구성과 UC 목록을 기반으로, 구현 독립성이 있는 도메인 경계 단위로 SPEC을 분리한다.
의존 방향(하위 → 상위)을 고려해 구현 순서를 정한다.

-----

## SPEC 목록 및 구현 순서

|순서|SPEC 파일                                                 |대상 도메인           |커버 UC                           |선행 SPEC                  |
|--|--------------------------------------------------------|-----------------|--------------------------------|-------------------------|
|1 |[SPEC-01-field.md](./SPEC-01-field.md)                  |분야(Field) 관리     |UC-FIELD-001~004                |—                        |
|2 |[SPEC-02-mentor-profile.md](./SPEC-02-mentor-profile.md)|멘토 프로필 — 경력·분야 등록|UC-CAREER-001~005, UC-MF-001~005|SPEC-01                  |
|3 |[SPEC-03-instant-qa.md](./SPEC-03-instant-qa.md)        |인스턴스 Q&A         |UC-QA-001~005                   |SPEC-01, SPEC-02         |
|4 |[SPEC-04-match-request.md](./SPEC-04-match-request.md)  |매칭 요청            |UC-MATCH-001~004                |SPEC-01, SPEC-02, SPEC-03|
|5 |[SPEC-05-matching.md](./SPEC-05-matching.md)            |매칭 성립·운영         |UC-MATCH-005~009                |SPEC-04                  |

-----

## 미결 항목 (OQ) 영향 범위

SPEC 작성 시 아래 OQ가 미확정 상태임을 주의한다.
Phase 1 관문에서 일괄 확인이 필요하다.

|OQ-ID |영향 SPEC         |요약                             |
|------|----------------|-------------------------------|
|OQ-001|SPEC-01         |상위 분야 비활성화 시 하위 처리 방식          |
|OQ-002|SPEC-02         |REJECTED/WITHDRAWN 분야 재등록 허용 여부|
|OQ-003|SPEC-04, SPEC-05|멘토 매칭 여력 정의                    |
|OQ-004|SPEC-05         |매칭 해지 시 환불 처리                  |
|OQ-005|SPEC-02         |경력 content 최대 길이               |
|OQ-006|SPEC-03         |Q&A·답변 content 최대 길이           |
|OQ-007|SPEC-05         |매칭 채널 유형(ChannelType) Enum 목록  |
|OQ-008|SPEC-03         |InstantQA 삭제/마감 기능 필요 여부       |