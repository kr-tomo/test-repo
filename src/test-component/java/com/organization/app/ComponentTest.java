package com.organization.app;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Tag("component")
class ComponentTest {
    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
