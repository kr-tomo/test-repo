# 테스트 전략

테스트 레벨 정의 → `.harness/references/test-levels.md`

-----

## 이 프로젝트의 개발 루프 대상

|레벨         |개발 루프 포함|비고                         |
|-----------|--------|---------------------------|
|Unit       |✅       |Spring 컨텍스트 불필요            |
|Component  |✅       |Spring 컨텍스트 필요할 수 있음, H2 사용|
|Integration|❌       |이번 프로젝트에서 제외               |
|Contract   |❌       |로컬 루프 미포함                  |
|E2E        |❌       |로컬 루프 미포함                  |

-----

## 실행 분리

Unit test와 Component test는 동일한 빌드를 사용하되 **별도 스텝으로 실행**한다.

```
스텝 1: Unit Test
  - Spring 컨텍스트를 로드하지 않는다
  - 외부 의존은 Mockito로 처리한다
  - 실행 대상: @Tag("unit") 또는 별도 소스셋 [TODO - 분리 방식 확정]

스텝 2: Component Test
  - Spring 컨텍스트를 로드할 수 있다
  - DB는 H2 in-memory를 사용한다
  - 외부 시스템은 mock/stub으로 대체한다
  - 실행 대상: @Tag("component") 또는 별도 소스셋 [TODO - 분리 방식 확정]
```

[TODO - 빌드 도구(Maven/Gradle) 확정 후 실행 명령 추가]

-----

## 테스트 파일 위치

```
src/
├── main/java/
└── test/java/
    ├── ...<package>/         # Unit test (기본 test 소스셋)
    └── ...<package>/         # Component test
```

[TODO - Unit / Component test 소스셋 분리 구조 확정]

-----

## 프레임워크

- **JUnit 5** (`@Test`, `@ExtendWith` 등)
- **Mockito** (`@Mock`, `@InjectMocks`, `when/verify`)
- **Spring Test** (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest` 등) — Component test에 한함
- **H2** — Component test DB