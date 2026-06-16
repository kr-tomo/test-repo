# DDD 용어 및 적용 방식

-----

## 핵심 빌딩 블록

|개념                |정의                                |위치                    |
|------------------|----------------------------------|----------------------|
|**Aggregate**     |일관성 경계를 가지는 객체 그룹. 외부는 루트를 통해서만 접근|`domain/model/`       |
|**Aggregate Root**|Aggregate의 진입점. ID로 식별됨           |`domain/model/`       |
|**Entity**        |식별자(ID)로 구분되는 객체                  |`domain/model/`       |
|**Value Object**  |값으로 동등성을 판단하는 불변 객체               |`domain/model/`       |
|**Domain Service**|특정 Entity에 속하지 않는 도메인 로직          |`domain/service/`     |
|**Domain Event**  |도메인 내에서 발생한 사실                    |[TODO - 사용 여부 및 위치 확정]|

-----

## 적용 규칙

- Aggregate 경계 밖에서 Aggregate 내부 Entity를 직접 수정하지 않는다
- Value Object는 불변(immutable)으로 구현한다
- Aggregate Root는 JPA `@Entity`와 1:1 대응하지 않아도 된다 — 영속성 매핑은 Adapter 레이어에서 별도 처리한다

[TODO - Aggregate 식별자 타입 정책 (Long / UUID / 도메인 전용 ID 타입)]
[TODO - Domain Event 발행 및 처리 방식]
[TODO - Bounded Context 경계 및 목록]