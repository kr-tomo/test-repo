# Harness Index

AI 코딩 작업의 진입점. 이 프로젝트에서 AI가 일하는 방식을 정의한다.

-----

## 항상 적용되는 원칙

- 작업 흐름의 단계를 건너뛰지 않는다
- 변경은 요청된 범위 내에서만 한다
- 추측으로 진행하지 않는다 — 불확실하면 Phase 1에서 일괄 확인한다
- Phase 1 관문 이후 요구사항 불명확성을 이유로 진행을 멈추지 않는다

-----

## 참조 구조

|상황                |파일                                  |
|------------------|------------------------------------|
|새 작업 시작 / 전체 흐름 파악|`.harness/workflow.md`              |
|SPEC을 받았을 때       |`.harness/phases/spec-analysis.md`  |
|테스트 케이스 작성 시      |`.harness/phases/test-doc.md`       |
|테스트 레벨 정의 확인      |`.harness/references/test-levels.md`|
|구현 시작 시           |`.harness/phases/implementation.md` |
|구현 완료 시           |`.harness/phases/report.md`         |

-----

## 프로젝트 설정 위임

아래 항목은 프로젝트 루트의 `AGENTS.md` 또는 별도 지침에서 정의한다.
정의가 없으면 각 phase 파일의 기본값을 따른다.

- 개발 루프에 포함할 테스트 레벨
- 테스트 프레임워크 및 실행 명령
- 코딩 컨벤션, 디렉토리 구조
- 기타 프로젝트 고유 규칙