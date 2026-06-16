# Component Test

Spring 컨텍스트를 사용하는 테스트. 서비스 내부 경계 안에서 실제 흐름을 검증한다.
외부 시스템은 mock/stub으로 대체한다. DB는 H2 in-memory를 사용한다.

-----

## 적용 범위

- Application Service가 Port를 통해 도메인 로직과 영속성을 올바르게 조율하는지 검증
- Inbound Adapter(Controller)의 요청/응답 직렬화, 유효성 검사 검증
- Outbound Adapter(Persistence)의 쿼리 결과가 도메인 모델로 올바르게 매핑되는지 검증

-----

## Spring Test 슬라이스 선택 기준

|검증 대상                |사용 어노테이션         |로드 범위            |
|---------------------|-----------------|-----------------|
|Controller 레이어       |`@WebMvcTest`    |Web 레이어만         |
|JPA Repository       |`@DataJpaTest`   |JPA 관련만, H2 자동 적용|
|전체 흐름 (Use Case → DB)|`@SpringBootTest`|전체 컨텍스트          |

비용 순서: `@WebMvcTest` < `@DataJpaTest` < `@SpringBootTest`
필요한 최소 슬라이스를 선택한다.

-----

## H2 설정

`@DataJpaTest`는 H2를 자동으로 사용한다.
`@SpringBootTest` 사용 시 test용 `application.yml`에 H2를 명시한다.

```yaml
# src/test/resources/application.yml (Component test용)
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
```

[TODO - 실제 DB 방언과 H2 간 호환성 이슈 발생 시 처리 방식 정의]

-----

## 외부 시스템 처리

Component test 범위 밖의 외부 시스템(외부 API, 메시지 브로커 등)은 mock으로 대체한다.

```java
// Outbound Port를 mock으로 교체하는 예시
@MockBean
ExternalPaymentPort externalPaymentPort;
```

-----

## 테스트 구조 예시

```java
@WebMvcTest(OrderController.class)
class OrderControllerComponentTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreateOrderUseCase createOrderUseCase;  // Inbound Port

    @Test
    void 주문_생성_요청이_성공하면_201을_반환한다() throws Exception {
        // given
        given(createOrderUseCase.create(any())).willReturn(...);

        // when & then
        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content("{...}"))
            .andExpect(status().isCreated());
    }
}
```

[TODO - 공통 테스트 설정 클래스 또는 베이스 클래스 사용 여부]