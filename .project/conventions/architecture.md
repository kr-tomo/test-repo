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
│   │   │   ├── in/         # Inbound Port (Use Case 인터페이스)
│   │   │   └── out/        # Outbound Port (Repository 등 인터페이스)
│   │   └── service/        # Domain Service
│   ├── application/
│   │   └── service/        # Use Case 구현체 (Application Service)
│   └── adapter/
│       ├── in/
│       │   └── web/        # REST Controller
│       └── out/
│           └── persistence/ # Repository 구현체, JPA Entity
```

최상위 패키지명: [TODO - 확정 필요]

-----

## 명명 규칙

|구성 요소              |규칙                         |예시                       |
|-------------------|---------------------------|-------------------------|
|Inbound Port       |[동사+명사] + `UseCase`        |`CreateOrderUseCase`     |
|Outbound Port      |[명사] + `Port`              |`OrderRepositoryPort`    |
|Application Service|[명사] + `Service`           |`OrderService`           |
|Inbound Adapter    |[명사] + `Controller`        |`OrderController`        |
|Outbound Adapter   |[명사] + `PersistenceAdapter`|`OrderPersistenceAdapter`|

[TODO - 팀 컨벤션과 다른 부분이 있으면 위 표를 수정]

-----

## 의존성 방향 규칙

- `adapter` → `application` → `domain` 방향으로만 의존한다
- `domain`은 어떤 외부 레이어도 import하지 않는다
- `adapter`끼리 직접 의존하지 않는다

[TODO - 모듈 간 공유가 필요한 타입(공유 커널 등)이 있는 경우 별도 정의]