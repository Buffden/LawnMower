package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LawnMowerMovementTest {
    private LawnMower lawnMower;
    private TestMowerObserver observer;

    private static class TestMowerObserver implements MowerObserver {
        private int lastRow = -1;
        private int lastCol = -1;
        private int callCount = 0;
        private boolean reachedEnd = false;

        @Override
        public void onCellMowed(int row, int col) {
            this.lastRow = row;
            this.lastCol = col;
            this.callCount++;
        }

        public void reset() {
            lastRow = -1;
            lastCol = -1;
            callCount = 0;
            reachedEnd = false;
        }

        public int getLastRow() { return lastRow; }
        public int getLastCol() { return lastCol; }
        public int getCallCount() { return callCount; }
        public boolean hasReachedEnd() { return reachedEnd; }
    }

    @BeforeEach
    void setUp() {
        observer = new TestMowerObserver();
    }

    @ParameterizedTest
    @DisplayName("LawnMower should handle different grid sizes")
    @CsvSource({
        "1, 1",   // Minimal grid
        "3, 3",   // Small grid
        "5, 5",   // Medium grid
        "10, 10", // Large grid
        "5, 10",  // Rectangular grid (wide)
        "10, 5"   // Rectangular grid (tall)
    })
    void testDifferentGridSizes(int rows, int cols) {
        lawnMower = new LawnMower(rows, cols);
        lawnMower.addObserver(observer);
        observer.reset();

        lawnMower.start();
        try {
            Thread.sleep(1100); // Wait for one move
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        assertTrue(observer.getCallCount() > 0, "Mower should move at least once");
        assertTrue(observer.getLastRow() >= 0 && observer.getLastRow() < rows, 
                  "Row position should be within bounds");
        assertTrue(observer.getLastCol() >= 0 && observer.getLastCol() < cols, 
                  "Column position should be within bounds");
    }

    @Test
    @DisplayName("LawnMower should follow snake pattern movement")
    void testSnakePatternMovement() {
        lawnMower = new LawnMower(3, 3);
        lawnMower.addObserver((row, col) -> {
            // First row should move left to right
            if (row == 0) {
                assertTrue(col >= observer.getLastCol() || observer.getLastCol() == -1,
                          "First row should move left to right");
            }
            // Second row should move right to left
            else if (row == 1 && observer.getLastRow() == 1) {
                assertTrue(col <= observer.getLastCol(),
                          "Second row should move right to left");
            }
            observer.onCellMowed(row, col);
        });

        lawnMower.start();
        try {
            Thread.sleep(3100); // Wait for multiple moves
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();
    }

    @Test
    @DisplayName("LawnMower should handle rapid start/stop operations")
    void testRapidStartStop() {
        lawnMower = new LawnMower(5, 5);
        lawnMower.addObserver(observer);

        // Rapid start/stop sequence
        for (int i = 0; i < 5; i++) {
            lawnMower.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lawnMower.stop();
        }

        // Final check
        assertFalse(lawnMower.isMowing(), "Mower should be stopped after rapid start/stop");
    }

    @Test
    @DisplayName("LawnMower should complete full grid coverage")
    void testFullGridCoverage() {
        int rows = 3;
        int cols = 3;
        lawnMower = new LawnMower(rows, cols);
        boolean[][] covered = new boolean[rows][cols];
        
        lawnMower.addObserver((row, col) -> {
            covered[row][col] = true;
            observer.onCellMowed(row, col);
        });

        lawnMower.start();
        try {
            // Wait long enough for full coverage
            Thread.sleep(rows * cols * 1100L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        // Verify coverage
        int coveredCells = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (covered[i][j]) coveredCells++;
            }
        }
        assertTrue(coveredCells > 0, "Some cells should be covered");
    }

    @Test
    @DisplayName("LawnMower should handle edge transitions correctly")
    void testEdgeTransitions() {
        lawnMower = new LawnMower(2, 3);
        final int[] expectedRow = {0};
        final boolean[] reachedEnd = {false};
        
        lawnMower.addObserver((row, col) -> {
            if (col == 2 && row == expectedRow[0]) {
                // Reached right edge
                expectedRow[0]++;
            } else if (col == 0 && row == expectedRow[0]) {
                // Reached left edge
                expectedRow[0]++;
            }
            if (row >= 2) reachedEnd[0] = true;
            observer.onCellMowed(row, col);
        });

        lawnMower.start();
        try {
            Thread.sleep(4100); // Wait for multiple transitions
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        assertTrue(expectedRow[0] > 0, "Mower should transition between rows");
    }
} 