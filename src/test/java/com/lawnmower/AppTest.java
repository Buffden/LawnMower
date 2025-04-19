package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    @DisplayName("App should start without exceptions")
    void testAppStart() {
        assertDoesNotThrow(() -> App.main(new String[]{}),
            "App should start without throwing exceptions");
    }

    @Test
    @DisplayName("App should handle command line arguments")
    void testAppWithArguments() {
        assertDoesNotThrow(() -> App.main(new String[]{"test"}),
            "App should handle command line arguments");
    }
} 