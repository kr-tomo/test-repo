# 프로젝트 설계 개요

## 설계 기반

- **Hexagonal Architecture** (Ports & Adapters)
- **Domain-Driven Design (DDD)**
- **REST API** 기반 외부 인터페이스

핵심 목표: 도메인 로직을 외부 기술(Spring, DB, HTTP)로부터 격리한다.
도메인 코드는 프레임워크에 의존하지 않는다.

## 레이어 구조 개요

```
[ Inbound Adapter ]   HTTP 요청 수신 (REST Controller 등)
        ↓
[ Application ]       Use Case 조율, Port 호출
        ↓
[ Domain ]            핵심 비즈니스 로직, 외부 의존 없음
        ↑
[ Outbound Adapter ]  DB, 외부 API 등 (Port 구현체)
```

상세 패키지 구조 및 명명 규칙 → `.project/conventions/architecture.md`
DDD 용어 적용 방식            → `.project/conventions/ddd.md`

## 외부 인터페이스

REST API 설계 규칙 → `.project/conventions/rest-api.md`