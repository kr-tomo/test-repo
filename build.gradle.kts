plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.organization.app" // TODO: 최상위 패키지명 확정 후 수정
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// ── 소스셋 ──────────────────────────────────────────────────────────────────
// Unit test  : src/test/java           (@Tag("unit"))
// Component test : src/test-component/java (@Tag("component"))

sourceSets {
    create("testComponent") {
        java.srcDir("src/test-component/java")
        resources.srcDir("src/test-component/resources")
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

val testComponentImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

val testComponentRuntimeOnly: Configuration by configurations.getting {
    extendsFrom(configurations.testRuntimeOnly.get())
}

// ── 의존성 ──────────────────────────────────────────────────────────────────
dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Swagger UI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // Runtime
    runtimeOnly("com.h2database:h2") // 개발/테스트용; TODO: 운영 DB 드라이버 추가

    // Unit Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Component Test (소스셋 공유이므로 추가 의존 불필요; 필요 시 아래에 추가)
    // testComponentImplementation("...")
}

// ── 태스크 ───────────────────────────────────────────────────────────────────

// 1) Unit Test — Spring 컨텍스트 미사용, @Tag("unit") 대상
tasks.test {
    useJUnitPlatform {
        includeTags("unit")
    }
    description = "Unit 테스트 실행 (@Tag(\"unit\"))"
}

// 2) Component Test — Spring 컨텍스트 사용, H2, @Tag("component") 대상
val testComponentTask = tasks.register<Test>("test-component") {
    description = "Component 테스트 실행 (@Tag(\"component\"))"
    group = "verification"

    testClassesDirs = sourceSets["testComponent"].output.classesDirs
    classpath = sourceSets["testComponent"].runtimeClasspath

    useJUnitPlatform {
        includeTags("component")
    }

    // Unit test 통과 후 실행
    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(testComponentTask)
}
