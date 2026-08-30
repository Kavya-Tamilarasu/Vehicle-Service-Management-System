package com.example.usermanagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UsermanagementApplicationTests {

    @Test
    void applicationClassLoads() {
        assertDoesNotThrow(() ->
            Class.forName("com.example.usermanagement.UsermanagementApplication")
        );
    }
}
