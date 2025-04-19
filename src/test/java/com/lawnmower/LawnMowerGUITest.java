package com.lawnmower;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LawnMowerGUITest {
    private static PrintStream originalOut;
    private static ByteArrayOutputStream outputStream;

    @BeforeAll
    static void setUp() {
        // Save original System.out
        originalOut = System.out;
        // Create a new output stream to capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        // Always run in headless mode for tests
        System.setProperty("java.awt.headless", "true");
    }

    @AfterAll
    static void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("LawnMowerGUI should handle headless mode gracefully")
    void testHeadlessMode() {
        // Run the main method
        assertDoesNotThrow(() -> LawnMowerGUI.main(new String[]{}),
            "Main method should not throw exceptions in headless mode");
        
        // Verify the correct message was printed
        String output = outputStream.toString();
        assertTrue(output.contains("Cannot run GUI in headless mode"),
            "Should print headless mode message");
    }

    @Test
    @DisplayName("LawnMowerGUI main method should handle arguments")
    void testMainWithArguments() {
        assertDoesNotThrow(() -> LawnMowerGUI.main(new String[]{"test"}),
            "Main method should handle arguments without throwing exceptions");
    }
} 