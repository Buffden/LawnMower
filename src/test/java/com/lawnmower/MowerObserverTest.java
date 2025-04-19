package com.lawnmower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MowerObserverTest {
    private MowerObserver observer;

    @BeforeEach
    void setUp() {
        observer = new MowerObserver() {
            @Override
            public void onCellMowed(int row, int col) {
                // Mock implementation
            }
        };
    }

    @Test
    @DisplayName("MowerObserver should handle cell updates")
    void testOnCellMowed() {
        assertDoesNotThrow(() -> observer.onCellMowed(0, 0),
            "Should be able to handle cell updates");
    }

    @Test
    @DisplayName("MowerObserver should handle multiple cell updates")
    void testMultipleCellUpdates() {
        for (int i = 0; i < 3; i++) {
            final int row = i;
            final int col = i;
            assertDoesNotThrow(() -> observer.onCellMowed(row, col),
                "Should be able to handle multiple cell updates");
        }
    }

    @Test
    @DisplayName("MowerObserver should handle invalid coordinates")
    void testInvalidCoordinates() {
        assertDoesNotThrow(() -> observer.onCellMowed(-1, -1),
            "Should handle invalid coordinates gracefully");
        assertDoesNotThrow(() -> observer.onCellMowed(5, 5),
            "Should handle out-of-bounds coordinates gracefully");
    }
} 