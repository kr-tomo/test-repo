# 아키텍처 및 패키지 컨벤션

기반: Hexagonal Architecture (Ports & Adapters)

-----

## 패키지 구조

```
com.<organization>.<app>/
├── <domain-module>/
│   ├── domain/
│   │   ├── model/          # Aggregate, Entity, Value Object
│   │   ├── port/
│   │   │   ├── in/         # Inbound Port (Command Use Case 인터페이스)
│   │   │   └── out/        # Outbound Port (Repository 등 인터페이스)
│   │   └── service/        # Domain Service
│   ├── application/
│   │   └── service/        # Command Use Case 구현체 (Application Service)
│   ├── adapter/
│   │   ├── in/
│   │   │   └── web/        # REST Controller
│   │   └── out/
│   │       └── persistence/ # Repository 구현체 (Persistence Adapter), JPA Entity
│   └── query/              # Query 영역 (CQRS)
│       ├── port/
│       │   └── in/         # Query Use Case 인터페이스
│       └── service/        # Query 구현체 (도메인 모델 거치지 않고 Entity -> DTO 직접 조회)
```

최상위 패키지명: `com.organization.app`

-----

## 명명 규칙

|구성 요소              |규칙                         |예시                       |
|-------------------|---------------------------|-------------------------|
|Inbound Port (Cmd) |[동사+명사] + `UseCase`        |`CreateOrderUseCase`     |
|Inbound Port (Query)|[동사+명사] + `Query`          |`GetOrderQuery`          |
|Outbound Port      |[명사] + `Port`              |`OrderRepositoryPort`    |
|Application Service|[명사] + `Service`           |`OrderService`           |
|Inbound Adapter    |[명사] + `Controller`        |`OrderController`        |
|Outbound Adapter   |[명사] + `PersistenceAdapter`|`OrderPersistenceAdapter`|

-----

## 의존성 방향 규칙

- `adapter` → `application` → `domain` 방향으로만 의존한다 (Command)
- `adapter` → `query` 방향으로만 의존한다 (Query)
- `query` 영역은 `domain` 영역을 의존하지 않고 `adapter.out.persistence` 의 Entity 를 직접 사용할 수 있다.
- `domain`은 어떤 외부 레이어도 import하지 않는다.
- `adapter`끼리 직접 의존하지 않는다.

-----

## 트랜잭션 관리 규칙

- **트랜잭션 경계는 Repository (Persistence Adapter) 이다.**
- Application Service 나 Query Service 에서는 `@Transactional`을 사용하지 않는다.
- 영속성 어댑터의 public 메서드에 `@Transactional`을 선언하여 데이터 정합성을 보장한다.

-----

## CQRS (Command Query Responsibility Segregation)

- **Command 와 Query 의 분리**: 상태를 변경하는 요청(Command)과 상태를 조회하는 요청(Query)을 완전히 분리한다.
- **조회 최적화**: 조회 요청은 도메인 모델(Aggregate)을 거치지 않는다.
- **데이터 흐름**: `Persistence Entity` -> `Projection/DTO` (조회 모델) 로 데이터를 직접 전송하여 불필요한 도메인 변환 비용을 줄인다.