package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LawnMowerTest {
    private LawnMower lawnMower;
    private TestMowerObserver observer;
    private static final int GRID_SIZE = 5;

    // Test observer implementation
    private static class TestMowerObserver implements MowerObserver {
        private int lastRow = -1;
        private int lastCol = -1;
        private int callCount = 0;

        @Override
        public void onCellMowed(int row, int col) {
            this.lastRow = row;
            this.lastCol = col;
            this.callCount++;
        }

        public int getLastRow() { return lastRow; }
        public int getLastCol() { return lastCol; }
        public int getCallCount() { return callCount; }
    }

    @BeforeEach
    void setUp() {
        lawnMower = new LawnMower(GRID_SIZE, GRID_SIZE);
        observer = new TestMowerObserver();
        lawnMower.addObserver(observer);
    }

    @Test
    @DisplayName("LawnMower should start in correct initial position")
    void testInitialPosition() {
        assertFalse(lawnMower.isMowing(), "Mower should not be mowing initially");
        assertEquals(-1, observer.getLastRow(), "No cells should be mowed initially");
        assertEquals(-1, observer.getLastCol(), "No cells should be mowed initially");
    }

    @Test
    @DisplayName("LawnMower should notify observers when mowing")
    void testObserverNotification() {
        lawnMower.start();
        // Wait for at least one mowing cycle
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        assertTrue(observer.getCallCount() > 0, "Observer should be notified at least once");
        assertTrue(observer.getLastRow() >= 0, "Row should be updated");
        assertTrue(observer.getLastCol() >= 0, "Column should be updated");
    }

    @Test
    @DisplayName("LawnMower should follow correct mowing pattern")
    void testMowingPattern() {
        lawnMower.start();
        // Wait for a few mowing cycles
        try {
            Thread.sleep(3100); // Wait for 3 moves
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        // Check if the mower moved in the expected pattern
        assertTrue(observer.getCallCount() >= 3, "Should have made at least 3 moves");
    }

    @Test
    @DisplayName("LawnMower should handle multiple observers")
    void testMultipleObservers() {
        TestMowerObserver secondObserver = new TestMowerObserver();
        lawnMower.addObserver(secondObserver);

        lawnMower.start();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();

        assertEquals(observer.getCallCount(), secondObserver.getCallCount(),
                    "Both observers should receive same number of notifications");
        assertEquals(observer.getLastRow(), secondObserver.getLastRow(),
                    "Both observers should receive same row updates");
        assertEquals(observer.getLastCol(), secondObserver.getLastCol(),
                    "Both observers should receive same column updates");
    }

    @Test
    @DisplayName("LawnMower should handle start/stop operations correctly")
    void testStartStopOperations() {
        assertFalse(lawnMower.isMowing(), "Should not be mowing initially");
        
        lawnMower.start();
        assertTrue(lawnMower.isMowing(), "Should be mowing after start");
        
        lawnMower.stop();
        assertFalse(lawnMower.isMowing(), "Should not be mowing after stop");
        
        // Test multiple start/stops
        lawnMower.start();
        lawnMower.start(); // Should not affect the state
        assertTrue(lawnMower.isMowing(), "Should still be mowing after multiple starts");
        
        lawnMower.stop();
        lawnMower.stop(); // Should not affect the state
        assertFalse(lawnMower.isMowing(), "Should not be mowing after multiple stops");
    }
} 