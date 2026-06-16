# TEST_DOC: 분야(Field) 관리

연관 SPEC: SPEC-01-field.md (v0.1)
작성일: 2026-06-16
개발 루프 대상 레벨: Unit, Component

-----

## 케이스 요약

|TC-ID   |이름                            |중요도|계층             |루프 포함|연관 요건                                  |
|--------|------------------------------|---|---------------|-----|---------------------------------------|
|TC-F-001|최상위 분야 생성 성공                  |P1 |Unit, Component|Yes  |UC-FIELD-001, BR-001                   |
|TC-F-002|하위 분야 생성 성공                   |P1 |Unit, Component|Yes  |UC-FIELD-001, BR-002                   |
|TC-F-003|분야 생성 — name 누락               |P2 |Unit, Component|Yes  |UC-FIELD-001, VR:FIELD_NAME_REQUIRED   |
|TC-F-004|분야 생성 — name 공백 문자열           |P2 |Unit, Component|Yes  |UC-FIELD-001, VR:FIELD_NAME_REQUIRED   |
|TC-F-005|분야 생성 — name 100자 초과          |P2 |Unit, Component|Yes  |UC-FIELD-001, VR:FIELD_NAME_TOO_LONG   |
|TC-F-006|분야 생성 — name 정확히 100자 (경계값)   |P3 |Unit           |Yes  |UC-FIELD-001, VR:FIELD_NAME_TOO_LONG   |
|TC-F-007|분야 생성 — 동일 계층 name 중복         |P2 |Unit, Component|Yes  |UC-FIELD-001, BR-003                   |
|TC-F-008|분야 생성 — 다른 계층 동일 name 허용      |P3 |Unit           |Yes  |UC-FIELD-001, BR-003                   |
|TC-F-009|분야 생성 — 존재하지 않는 parentId      |P2 |Unit, Component|Yes  |UC-FIELD-001, BR-002                   |
|TC-F-010|분야 생성 — INACTIVE 상위 분야 지정     |P2 |Unit, Component|Yes  |UC-FIELD-001, BR-002                   |
|TC-F-011|분야 생성 — 권한 없는 운영자             |P0 |Unit, Component|Yes  |UC-FIELD-001, BR-001                   |
|TC-F-012|분야 생성 — 멘티 계정으로 시도            |P0 |Component      |Yes  |UC-FIELD-001, BR-001                   |
|TC-F-013|분야 수정 성공                      |P1 |Unit, Component|Yes  |UC-FIELD-002, BR-001                   |
|TC-F-014|분야 수정 — 동일 계층 name 중복         |P2 |Unit, Component|Yes  |UC-FIELD-002, BR-003                   |
|TC-F-015|분야 수정 — name 100자 초과          |P2 |Unit           |Yes  |UC-FIELD-002, VR:FIELD_NAME_TOO_LONG   |
|TC-F-016|분야 수정 — 존재하지 않는 fieldId       |P2 |Component      |Yes  |UC-FIELD-002, VR:FIELD_NOT_FOUND       |
|TC-F-017|분야 수정 — 권한 없는 운영자             |P0 |Unit, Component|Yes  |UC-FIELD-002, BR-001                   |
|TC-F-018|분야 비활성화 성공 (하위 없음)            |P1 |Unit, Component|Yes  |UC-FIELD-003, BR-001                   |
|TC-F-019|분야 비활성화 — 하위 분야 연쇄 INACTIVE   |P0 |Unit, Component|Yes  |UC-FIELD-003, BR-004                   |
|TC-F-020|분야 비활성화 — 다단계 하위 연쇄 INACTIVE  |P0 |Unit, Component|Yes  |UC-FIELD-003, BR-004                   |
|TC-F-021|분야 비활성화 — 이미 INACTIVE         |P2 |Unit, Component|Yes  |UC-FIELD-003, VR:FIELD_ALREADY_INACTIVE|
|TC-F-022|분야 비활성화 — 존재하지 않는 fieldId     |P2 |Component      |Yes  |UC-FIELD-003, VR:FIELD_NOT_FOUND       |
|TC-F-023|분야 비활성화 — 권한 없는 운영자           |P0 |Unit, Component|Yes  |UC-FIELD-003, BR-001                   |
|TC-F-024|분야 목록 조회 — 계층 트리 반환           |P1 |Component      |Yes  |UC-FIELD-004                           |
|TC-F-025|분야 목록 조회 — INACTIVE 분야 포함 반환  |P3 |Component      |Yes  |UC-FIELD-004                           |
|TC-F-026|분야 목록 조회 — 분야 없을 때 빈 배열       |P3 |Component      |Yes  |UC-FIELD-004                           |
|TC-F-027|Repository — 하위 분야 일괄 상태 변경 쿼리|P0 |Component      |Yes  |BR-004                                 |

-----

## 테스트 케이스

### [TC-F-001] 최상위 분야 생성 성공

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Field.create()` 도메인 객체 생성 및 초기 상태 검증
  - Component — `POST /fields` API 진입점 검증 (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `FieldService.create()`, `FieldController.create()`
- **연관 요건**: UC-FIELD-001, BR-001

#### 조건

- ACCOUNT_MANAGEMENT 권한을 가진 운영자 계정으로 인증
- 동일 이름의 최상위 분야 없음

#### 입력

```json
{ "name": "개발", "parentId": null }
```

#### 기대 결과

- HTTP 201 Created
- 응답 본문: `{ "id": any, "name": "개발", "parentId": null, "status": "ACTIVE" }`
- 부수 효과: Field 레코드 1건 저장, status=ACTIVE

-----

### [TC-F-002] 하위 분야 생성 성공

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Field` 도메인 객체 parentId 설정 검증
  - Component — `POST /fields` with parentId (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `FieldService.create()`, BR-002 검증 로직
- **연관 요건**: UC-FIELD-001, BR-002

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- parentId=1인 ACTIVE 최상위 분야 존재
- 동일 parentId 하위에 “백엔드” 이름 없음

#### 입력

```json
{ "name": "백엔드", "parentId": 1 }
```

#### 기대 결과

- HTTP 201 Created
- 응답 본문: `{ "id": any, "name": "백엔드", "parentId": 1, "status": "ACTIVE" }`

-----

### [TC-F-003] 분야 생성 — name 누락

- **중요도**: P2
- **계층**: Unit, Component
  - Unit — 도메인/서비스 레벨 name null 검증
  - Component — Controller VR 검증 (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: VR: FIELD_NAME_REQUIRED
- **연관 요건**: UC-FIELD-001, VR:FIELD_NAME_REQUIRED

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자

#### 입력

```json
{ "parentId": null }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_NAME_REQUIRED", "status": 400 }`

-----

### [TC-F-004] 분야 생성 — name 공백 문자열

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR: FIELD_NAME_REQUIRED (Not Blank)
- **연관 요건**: UC-FIELD-001, VR:FIELD_NAME_REQUIRED

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자

#### 입력

```json
{ "name": "   ", "parentId": null }
```

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_NAME_REQUIRED", "status": 400 }`

-----

### [TC-F-005] 분야 생성 — name 100자 초과

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR: FIELD_NAME_TOO_LONG
- **연관 요건**: UC-FIELD-001, VR:FIELD_NAME_TOO_LONG

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자

#### 입력

```json
{ "name": "가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아", "parentId": null }
```

> name 길이: 101자

#### 기대 결과

- HTTP 400 Bad Request
- `{ "code": "FIELD_NAME_TOO_LONG", "status": 400 }`

-----

### [TC-F-006] 분야 생성 — name 정확히 100자 (경계값)

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR: FIELD_NAME_TOO_LONG 경계값
- **연관 요건**: VR:FIELD_NAME_TOO_LONG

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자

#### 입력

- name: 100자 문자열 (정확히)

#### 기대 결과

- 도메인 객체 생성 성공 (예외 없음)

-----

### [TC-F-007] 분야 생성 — 동일 계층 name 중복

- **중요도**: P2
- **계층**: Unit, Component
  - Unit — `FieldService.create()` 중복 체크 로직
  - Component — `@SpringBootTest` 실제 DB 중복 확인
- **루프 포함**: Yes
- **대상**: BR-003
- **연관 요건**: UC-FIELD-001, BR-003

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- parentId=null인 “개발” 분야 이미 존재

#### 입력

```json
{ "name": "개발", "parentId": null }
```

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "FIELD_NAME_DUPLICATE", "status": 409 }`

-----

### [TC-F-008] 분야 생성 — 다른 계층 동일 name 허용

- **중요도**: P3
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: BR-003 — 계층 단위 Unique 범위 확인
- **연관 요건**: BR-003

#### 조건

- parentId=null인 “개발” (id=1) 존재
- parentId=1 하위에 “개발”은 없음

#### 입력

- parentId=1, name=“개발” (최상위와 같은 이름이나 다른 계층)

#### 기대 결과

- 도메인 레벨: 중복 아님으로 판단, 생성 성공

-----

### [TC-F-009] 분야 생성 — 존재하지 않는 parentId

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-002, VR:PARENT_FIELD_NOT_FOUND
- **연관 요건**: UC-FIELD-001, BR-002

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- id=9999인 Field 존재하지 않음

#### 입력

```json
{ "name": "백엔드", "parentId": 9999 }
```

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "PARENT_FIELD_NOT_FOUND", "status": 404 }`

-----

### [TC-F-010] 분야 생성 — INACTIVE 상위 분야 지정

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-002, VR:PARENT_FIELD_INACTIVE
- **연관 요건**: UC-FIELD-001, BR-002

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- parentId=1인 Field가 status=INACTIVE

#### 입력

```json
{ "name": "백엔드", "parentId": 1 }
```

#### 기대 결과

- HTTP 422 Unprocessable Entity
- `{ "code": "PARENT_FIELD_INACTIVE", "status": 422 }`

-----

### [TC-F-011] 분야 생성 — 권한 없는 운영자 (ACCOUNT_MANAGEMENT 미보유)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `FieldService.create()` 권한 검증 로직
  - Component — `@WebMvcTest` 권한 체크 필터 검증
- **루프 포함**: Yes
- **대상**: BR-001
- **연관 요건**: BR-001

#### 조건

- MATCHING_MANAGEMENT 권한만 가진 운영자 (ACCOUNT_MANAGEMENT 없음)

#### 입력

```json
{ "name": "개발", "parentId": null }
```

#### 기대 결과

- HTTP 403 Forbidden
- `{ "code": "FORBIDDEN", "status": 403 }`
- 부수 효과: Field 저장 없음

-----

### [TC-F-012] 분야 생성 — 멘티 계정으로 시도

- **중요도**: P0
- **계층**: Component
  - Component — `@WebMvcTest` 인가 필터 검증
- **루프 포함**: Yes
- **대상**: BR-001
- **연관 요건**: BR-001

#### 조건

- role=MENTEE 계정으로 인증

#### 입력

```json
{ "name": "개발", "parentId": null }
```

#### 기대 결과

- HTTP 403 Forbidden
- `{ "code": "FORBIDDEN", "status": 403 }`

-----

### [TC-F-013] 분야 수정 성공

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Field.updateName()` 도메인 메서드 검증
  - Component — `PATCH /fields/{fieldId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: UC-FIELD-002, `FieldService.update()`
- **연관 요건**: UC-FIELD-002, BR-001

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- id=10인 ACTIVE Field 존재, 동일 계층에 “백엔드 개발” 없음

#### 입력

- Path: `/fields/10`

```json
{ "name": "백엔드 개발" }
```

#### 기대 결과

- HTTP 200 OK
- 응답 본문: `{ "id": 10, "name": "백엔드 개발", "parentId": 1, "status": "ACTIVE" }`
- 부수 효과: DB name 컬럼 갱신

-----

### [TC-F-014] 분야 수정 — 동일 계층 name 중복

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-003
- **연관 요건**: UC-FIELD-002, BR-003

#### 조건

- parentId=1 하위에 “프론트엔드”(id=11)와 “백엔드”(id=10) 둘 다 존재
- id=10을 “프론트엔드”로 수정 시도

#### 입력

- Path: `/fields/10`

```json
{ "name": "프론트엔드" }
```

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "FIELD_NAME_DUPLICATE", "status": 409 }`

-----

### [TC-F-015] 분야 수정 — name 100자 초과

- **중요도**: P2
- **계층**: Unit
- **루프 포함**: Yes
- **대상**: VR: FIELD_NAME_TOO_LONG
- **연관 요건**: VR:FIELD_NAME_TOO_LONG

#### 조건

- id=10인 Field 존재

#### 입력

- name: 101자 문자열

#### 기대 결과

- 도메인 레벨 예외 발생 (FIELD_NAME_TOO_LONG)

-----

### [TC-F-016] 분야 수정 — 존재하지 않는 fieldId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR: FIELD_NOT_FOUND
- **연관 요건**: VR:FIELD_NOT_FOUND

#### 조건

- id=9999인 Field 없음

#### 입력

- Path: `/fields/9999`

```json
{ "name": "수정이름" }
```

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "FIELD_NOT_FOUND", "status": 404 }`

-----

### [TC-F-017] 분야 수정 — 권한 없는 운영자

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-001
- **연관 요건**: BR-001

#### 조건

- ACCOUNT_MANAGEMENT 권한 없는 계정

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: Field 변경 없음

-----

### [TC-F-018] 분야 비활성화 성공 (하위 없음)

- **중요도**: P1
- **계층**: Unit, Component
  - Unit — `Field.deactivate()` 상태 전이 검증
  - Component — `DELETE /fields/{fieldId}` (`@WebMvcTest`)
- **루프 포함**: Yes
- **대상**: `FieldService.deactivate()`, 상태 전이 ACTIVE→INACTIVE
- **연관 요건**: UC-FIELD-003, BR-001

#### 조건

- ACCOUNT_MANAGEMENT 권한 운영자
- id=10인 ACTIVE Field, 하위 분야 없음

#### 입력

- Path: `DELETE /fields/10`

#### 기대 결과

- HTTP 204 No Content
- 부수 효과: id=10 Field의 status=INACTIVE로 변경

-----

### [TC-F-019] 분야 비활성화 — 하위 분야 연쇄 INACTIVE (1단계)

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — `FieldService.deactivate()` 연쇄 처리 로직
  - Component — `@SpringBootTest` 실제 DB 연쇄 반영 검증
- **루프 포함**: Yes
- **대상**: BR-004
- **연관 요건**: UC-FIELD-003, BR-004

#### 조건

- id=1 “개발” (ACTIVE), 하위: id=10 “백엔드” (ACTIVE), id=11 “프론트엔드” (ACTIVE)

#### 입력

- Path: `DELETE /fields/1`

#### 기대 결과

- HTTP 204 No Content
- 부수 효과:
  - id=1 status=INACTIVE
  - id=10 status=INACTIVE
  - id=11 status=INACTIVE
  - 단일 트랜잭션으로 처리됨

-----

### [TC-F-020] 분야 비활성화 — 다단계 하위 연쇄 INACTIVE

- **중요도**: P0
- **계층**: Unit, Component
  - Unit — 재귀 비활성화 로직 단위 검증
  - Component — `@SpringBootTest` 3단계 계층 전체 검증
- **루프 포함**: Yes
- **대상**: BR-004 재귀 처리
- **연관 요건**: BR-004

#### 조건

- id=1 “개발” → id=10 “백엔드” → id=20 “Java” (3단계 계층)
- 모두 ACTIVE

#### 입력

- Path: `DELETE /fields/1`

#### 기대 결과

- id=1, id=10, id=20 모두 status=INACTIVE
- 부수 효과: 모두 단일 트랜잭션 내 처리

-----

### [TC-F-021] 분야 비활성화 — 이미 INACTIVE

- **중요도**: P2
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: VR: FIELD_ALREADY_INACTIVE
- **연관 요건**: VR:FIELD_ALREADY_INACTIVE

#### 조건

- id=10인 Field가 status=INACTIVE

#### 입력

- Path: `DELETE /fields/10`

#### 기대 결과

- HTTP 409 Conflict
- `{ "code": "FIELD_ALREADY_INACTIVE", "status": 409 }`

-----

### [TC-F-022] 분야 비활성화 — 존재하지 않는 fieldId

- **중요도**: P2
- **계층**: Component
- **루프 포함**: Yes
- **대상**: VR: FIELD_NOT_FOUND
- **연관 요건**: VR:FIELD_NOT_FOUND

#### 조건

- id=9999 Field 없음

#### 기대 결과

- HTTP 404 Not Found
- `{ "code": "FIELD_NOT_FOUND", "status": 404 }`

-----

### [TC-F-023] 분야 비활성화 — 권한 없는 운영자

- **중요도**: P0
- **계층**: Unit, Component
- **루프 포함**: Yes
- **대상**: BR-001
- **연관 요건**: BR-001

#### 조건

- ACCOUNT_MANAGEMENT 권한 없는 계정

#### 기대 결과

- HTTP 403 Forbidden
- 부수 효과: 어떤 Field의 status도 변경되지 않음

-----

### [TC-F-024] 분야 목록 조회 — 계층 트리 반환

- **중요도**: P1
- **계층**: Component
  - Component — `GET /fields` (`@SpringBootTest`)
- **루프 포함**: Yes
- **대상**: `FieldService.getAll()`, 계층 트리 조합 로직
- **연관 요건**: UC-FIELD-004

#### 조건

- id=1 “개발” (ACTIVE, parentId=null) 존재
- id=10 “백엔드” (ACTIVE, parentId=1) 존재

#### 기대 결과

- HTTP 200 OK
- 응답:

```json
[
  {
    "id": 1, "name": "개발", "parentId": null, "status": "ACTIVE",
    "children": [
      { "id": 10, "name": "백엔드", "parentId": 1, "status": "ACTIVE", "children": [] }
    ]
  }
]
```

-----

### [TC-F-025] 분야 목록 조회 — INACTIVE 분야 포함 반환

- **중요도**: P3
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 조회 결과에 INACTIVE 포함 여부 정책 확인
- **연관 요건**: UC-FIELD-004

#### 조건

- id=1 “개발” ACTIVE, id=10 “백엔드” INACTIVE

#### 기대 결과

- HTTP 200 OK
- INACTIVE 분야도 트리에 포함되어 반환됨 (status 필드로 구분)

-----

### [TC-F-026] 분야 목록 조회 — 분야 없을 때 빈 배열

- **중요도**: P3
- **계층**: Component
- **루프 포함**: Yes
- **대상**: 빈 상태 처리
- **연관 요건**: UC-FIELD-004

#### 조건

- Field 데이터 없음

#### 기대 결과

- HTTP 200 OK
- 응답: `[]`

-----

### [TC-F-027] Repository — 하위 분야 일괄 상태 변경 쿼리

- **중요도**: P0
- **계층**: Component
  - Component — `@DataJpaTest` Repository 쿼리 정확성 검증
- **루프 포함**: Yes
- **대상**: `FieldRepositoryPort.findAllDescendants()` 또는 재귀 쿼리
- **연관 요건**: BR-004

#### 조건

- id=1 하위에 id=10, id=11 존재. id=10 하위에 id=20 존재

#### 입력

- parentId=1로 하위 전체 조회

#### 기대 결과

- 반환 목록: [id=10, id=11, id=20] (재귀 포함)
- 부수 효과: 전체를 INACTIVE로 일괄 업데이트 후 DB 반영 확인

-----

## 커버리지 매트릭스

|요건                       |커버 TC                                 |비고                          |
|-------------------------|--------------------------------------|----------------------------|
|UC-FIELD-001             |TC-F-001~012                          |생성 정상/경계/예외 전체              |
|UC-FIELD-002             |TC-F-013~017                          |수정 정상/경계/예외                 |
|UC-FIELD-003             |TC-F-018~023                          |비활성화 정상/경계/예외               |
|UC-FIELD-004             |TC-F-024~026                          |조회 정상/경계                    |
|BR-001                   |TC-F-011, TC-F-012, TC-F-017, TC-F-023|P0 — Unit+Component 2계층 커버 ✅|
|BR-002                   |TC-F-002, TC-F-009, TC-F-010          |정상/예외                       |
|BR-003                   |TC-F-007, TC-F-008, TC-F-014          |중복/허용 경계                    |
|BR-004                   |TC-F-019, TC-F-020, TC-F-027          |P0 — Unit+Component 2계층 커버 ✅|
|VR:FIELD_NAME_REQUIRED   |TC-F-003, TC-F-004                    |null, blank                 |
|VR:FIELD_NAME_TOO_LONG   |TC-F-005, TC-F-006                    |초과, 경계값                     |
|VR:FIELD_NAME_DUPLICATE  |TC-F-007, TC-F-014                    |—                           |
|VR:PARENT_FIELD_NOT_FOUND|TC-F-009                              |—                           |
|VR:PARENT_FIELD_INACTIVE |TC-F-010                              |—                           |
|VR:FIELD_NOT_FOUND       |TC-F-016, TC-F-022                    |—                           |
|VR:FIELD_ALREADY_INACTIVE|TC-F-021                              |—                           |

-----

## 루프 외 케이스 목록

해당 없음. 모든 케이스가 Unit 또는 Component 계층 내 검증 가능.