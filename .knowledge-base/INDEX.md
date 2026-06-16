# Knowledge Base — 멘토링 매칭 서비스

버전: 0.1 | 상태: Draft

이 KB는 비즈니스·Tech·QA가 함께 읽는 단일 진실 공급원(Single Source of Truth)이다.
AI 코딩 작업 시 SPEC 분석(Phase 1) 전에 이 INDEX를 먼저 참조한다.

-----

## 파일 맵

|파일                                          |내용                                       |주 독자       |
|--------------------------------------------|-----------------------------------------|-----------|
|[INDEX.md](./INDEX.md)                      |이 파일. 전체 구조와 참조 경로                       |전체         |
|[glossary.md](./glossary.md)                |용어 정의 — 도메인 언어 기준                        |전체         |
|[actors.md](./actors.md)                    |Actor 정의 및 권한 체계                         |비즈니스 / Tech|
|[domain-model.md](./domain-model.md)        |Aggregate / Entity / Value Object / 상태 전이|Tech / QA  |
|[business-rules.md](./business-rules.md)    |BR 전체 목록 (도메인별 분류)                       |비즈니스 / QA  |
|[validation-rules.md](./validation-rules.md)|VR 전체 목록 (필드 단위)                         |Tech / QA  |
|[use-cases.md](./use-cases.md)              |Use Case 목록 및 Actor 매핑                   |비즈니스 / QA  |
|[api-overview.md](./api-overview.md)        |REST API 엔드포인트 전체 목록                     |Tech       |
|[open-questions.md](./open-questions.md)    |미결 항목 (OQ) 추적                            |전체         |

-----

## 도메인 구성

```
멘토링 매칭 서비스
├── 계정 (Account)          → actors.md
├── 분야 (Field)            → domain-model.md#field, business-rules.md#BR-FIELD
├── 멘토 프로필 (MentorProfile)
│   ├── 경력 (Career)       → domain-model.md#career, business-rules.md#BR-CAREER
│   └── 분야 등록           → domain-model.md#mentor-field, business-rules.md#BR-MENTOR-FIELD
├── 매칭 (Matching)         → domain-model.md#matching, business-rules.md#BR-MATCH
│   └── 매칭 채널 (Channel) → domain-model.md#channel
├── 인스턴스 Q&A (InstantQA) → domain-model.md#instant-qa, business-rules.md#BR-QA
└── 결제 (Payment)          → domain-model.md#payment, business-rules.md#BR-PAYMENT
```

-----

## 참조 규칙

- BR-XXX-NNN : 비즈니스 규칙 ID (예: BR-MATCH-001)
- VR-XXX-NNN : Validation Rule ID (예: VR-CAREER-001)
- UC-NNN     : Use Case ID
- OQ-NNN     : Open Question ID
- 각 ID는 파일 내 앵커(`#`)로 연결된다

-----

## AI 코딩 작업 시 참조 순서

```
1. INDEX.md          — 전체 구조 파악
2. glossary.md       — 용어 확인
3. actors.md         — 권한 체계 확인
4. domain-model.md   — 구현 대상 도메인 모델
5. business-rules.md — 해당 도메인 BR
6. validation-rules.md — 해당 도메인 VR
7. use-cases.md      — 구현 대상 UC
8. api-overview.md   — REST API 명세
9. open-questions.md — 미결 항목 확인 후 Phase 1에서 일괄 질의
```