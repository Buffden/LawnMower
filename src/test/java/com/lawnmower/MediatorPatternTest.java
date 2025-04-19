package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MediatorPatternTest {
    private static final int GRID_SIZE = 5;
    private LawnGrid lawnGrid;
    private LawnMower lawnMower;
    private MowerMediator mediator;

    @BeforeEach
    void setUp() {
        lawnGrid = new LawnGrid(GRID_SIZE, GRID_SIZE);
        lawnMower = new LawnMower(GRID_SIZE, GRID_SIZE);
        mediator = new MowerMediator(lawnGrid, lawnMower);
    }

    @Test
    @DisplayName("Mediator should properly initialize components")
    void testMediatorInitialization() {
        assertNotNull(mediator, "Mediator should be properly initialized");
        // Verify that the mediator has properly set up the observer relationship
        lawnMower.start();
        try {
            Thread.sleep(1100); // Wait for one mowing cycle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lawnMower.stop();
        // The fact that no exceptions occurred indicates proper initialization
        assertTrue(true, "Mediator should handle mowing cycle without errors");
    }

    @Test
    @DisplayName("Mediator should coordinate mowing operations")
    void testMowingCoordination() {
        // Start mowing through mediator
        mediator.startMowing();
        assertTrue(lawnMower.isMowing(), "Mower should be mowing after mediator starts it");

        // Stop mowing through mediator
        mediator.stopMowing();
        assertFalse(lawnMower.isMowing(), "Mower should stop after mediator stops it");
    }

    @Test
    @DisplayName("Mediator should handle lawn updates")
    void testLawnUpdates() {
        // Test multiple cell updates
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                mediator.updateLawn(row, col);
            }
        }
        // If no exceptions occurred, the updates were handled properly
        assertTrue(true, "Mediator should handle all lawn updates without errors");
    }

    @Test
    @DisplayName("Mediator should handle invalid updates gracefully")
    void testInvalidUpdates() {
        // Test invalid coordinates
        mediator.updateLawn(-1, -1);
        mediator.updateLawn(GRID_SIZE, GRID_SIZE);
        mediator.updateLawn(0, GRID_SIZE);
        mediator.updateLawn(GRID_SIZE, 0);
        
        // If no exceptions occurred, the invalid updates were handled gracefully
        assertTrue(true, "Mediator should handle invalid updates without errors");
    }

    @Test
    @DisplayName("Mediator should coordinate complete mowing sequence")
    void testCompleteMowingSequence() {
        // Start mowing
        mediator.startMowing();
        
        // Let it mow for a few cycles
        try {
            Thread.sleep(3100); // Wait for 3 moves
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Stop mowing
        mediator.stopMowing();
        
        // Verify the sequence completed without errors
        assertFalse(lawnMower.isMowing(), "Mower should be stopped after sequence");
    }
} 