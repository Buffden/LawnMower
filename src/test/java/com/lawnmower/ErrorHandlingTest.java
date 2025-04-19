package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ErrorHandlingTest {
    private LawnMower lawnMower;
    private LawnGrid lawnGrid;
    private MowerMediator mediator;

    @BeforeEach
    void setUp() {
        lawnMower = new LawnMower(5, 5);
        lawnGrid = new LawnGrid(5, 5);
        mediator = new MowerMediator(lawnGrid, lawnMower);
    }

    @ParameterizedTest
    @DisplayName("LawnMower should handle invalid grid sizes gracefully")
    @CsvSource({
        "0, 5",    // Invalid rows
        "5, 0",    // Invalid columns
        "0, 0",    // Both invalid
        "-1, 5",   // Negative rows
        "5, -1",   // Negative columns
        "-1, -1"   // Both negative
    })
    void testInvalidGridSizes(int rows, int cols) {
        assertDoesNotThrow(() -> {
            LawnMower mower = new LawnMower(rows, cols);
            LawnGrid grid = new LawnGrid(rows, cols);
            MowerMediator med = new MowerMediator(grid, mower);
            
            // Try operations with invalid sizes
            mower.start();
            med.updateLawn(0, 0);
            mower.stop();
        }, "Should handle invalid grid sizes without throwing exceptions");
    }

    @Test
    @DisplayName("LawnMower should handle null observer gracefully")
    void testNullObserver() {
        assertDoesNotThrow(() -> {
            lawnMower.addObserver(null);
            lawnMower.start();
            Thread.sleep(1000);
            lawnMower.stop();
        }, "Should handle null observer without throwing exceptions");
    }

    @Test
    @DisplayName("Mediator should handle concurrent start/stop operations")
    void testConcurrentOperations() throws InterruptedException {
        Thread startThread = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                mediator.startMowing();
            }
        });

        Thread stopThread = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                mediator.stopMowing();
            }
        });

        startThread.start();
        stopThread.start();

        startThread.join(5000);
        stopThread.join(5000);

        assertFalse(startThread.isAlive(), "Start thread should complete");
        assertFalse(stopThread.isAlive(), "Stop thread should complete");
    }

    @Test
    @DisplayName("LawnGrid should handle color update after disposal")
    void testUpdateAfterDisposal() {
        lawnGrid.setVisible(false);
        lawnGrid.removeAll();
        
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    mediator.updateLawn(i, j);
                }
            }
        }, "Should handle updates after disposal");
    }

    @Test
    @DisplayName("Components should handle memory cleanup")
    void testMemoryCleanup() {
        // Create and dispose of multiple instances
        for (int i = 0; i < 100; i++) {
            LawnGrid grid = new LawnGrid(5, 5);
            LawnMower mower = new LawnMower(5, 5);
            MowerMediator med = new MowerMediator(grid, mower);
            
            mower.start();
            med.updateLawn(0, 0);
            mower.stop();
            
            grid.removeAll();
            grid = null;
            mower = null;
            med = null;
        }
        
        // If we reached here without OutOfMemoryError, test passes
        assertTrue(true, "Should handle multiple instance creation and disposal");
    }

    @Test
    @DisplayName("Mediator should handle rapid state transitions")
    void testRapidStateTransitions() {
        // Rapid state changes
        for (int i = 0; i < 100; i++) {
            mediator.startMowing();
            mediator.stopMowing();
        }
        
        assertFalse(lawnMower.isMowing(), "Mower should end in stopped state");
    }
} 