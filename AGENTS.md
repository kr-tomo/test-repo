# 프로젝트 에이전트 및 가이드라인

이 문서는 이 프로젝트에서 작업하는 AI 에이전트의 행동 표준을 정의합니다.

## 전역 지침

모든 에이전트는 반드시 **Karpathy Guidelines** 행동 스킬을 준수해야 합니다. 이 스킬은 다음을 강제합니다:
- 가정과 트레이드오프를 명시적으로 언급할 것.
- 최소한의 비투기적(non-speculative) 코드를 우선시할 것.
- 필요한 부분만 수정하는 정밀한(surgical) 변경을 수행할 것.
- 목표 지향적 실행 및 검증 루프를 따를 것.

### 활성 스킬
다음 스킬이 설치되어 있으며 모든 개발 작업에 대해 활성화되어야 합니다:
- **karpathy-guidelines**: `./.gemini/skills/karpathy-guidelines/SKILL.md` 에 위치함.

## 프로젝트 개요

<!-- 이 프로젝트가 무엇인지 간략히 기술 -->

## 기술 스택

- Language: Java
- Framework: Spring Boot
- Test: JUnit 5 + Mockito
- Component Test DB: H2 (in-memory)
- Build: <!-- Maven / Gradle -->

## 주요 경로

<!-- src/main/java/, src/test/java/, 설정 파일 위치 등 -->

## 프로젝트 설계 및 컨벤션

설계 원칙 및 아키텍처 개요    → `.project/overview.md`
아키텍처 / 패키지 컨벤션      → `.project/conventions/architecture.md`
DDD 용어 및 적용 방식         → `.project/conventions/ddd.md`
REST API 설계 규칙            → `.project/conventions/rest-api.md`
테스트 전략 및 실행 환경      → `.project/testing/strategy.md`
Component Test 설정 및 패턴   → `.project/testing/component-test.md`

## AI 작업 방식

이 프로젝트의 AI 코딩 절차는 `.harness/INDEX.md`를 따른다.
