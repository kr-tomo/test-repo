# REST API 설계 규칙

-----

## URL 설계

- 리소스는 복수 명사로 표현한다: `/orders`, `/products`
- 계층 관계는 경로로 표현한다: `/orders/{orderId}/items`
- 동사는 HTTP Method로 표현한다. URL에 동사를 포함하지 않는다
- 소문자와 하이픈(`-`)을 사용한다: `/order-items` (언더스코어 사용 금지)

## HTTP Method

|Method|용도   |성공 응답 코드|
|------|-----|--------|
|GET   |조회   |200     |
|POST  |생성   |201     |
|PUT   |전체 수정|200     |
|PATCH |부분 수정|200     |
|DELETE|삭제   |204     |

## 응답 형식

- 본문은 JSON을 사용한다
- 단건 조회: 객체 반환
- 목록 조회: 배열 또는 페이지 래퍼 반환

```json
// 페이지 래퍼 예시
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0
}
```

[TODO - 공통 응답 래퍼 사용 여부 및 구조]

## 에러 응답

```json
{
  "code": "ORDER_NOT_FOUND",
  "message": "주문을 찾을 수 없습니다.",
  "status": 404
}
```

[TODO - 에러 코드 체계 및 공통 예외 처리 방식 확정]

## API 버전 관리

[TODO - URL 버전 (`/v1/`) 또는 헤더 버전 방식 결정]