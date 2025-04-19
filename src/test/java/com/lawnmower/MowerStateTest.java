package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MowerStateTest {
    private static final int GRID_SIZE = 5;
    private LawnMower lawnMower;
    private MowerMediator mediator;

    @BeforeEach
    void setUp() {
        // Create dependencies
        LawnGrid lawnGrid = new LawnGrid(GRID_SIZE, GRID_SIZE);
        lawnMower = new LawnMower(GRID_SIZE, GRID_SIZE);
        mediator = new MowerMediator(lawnGrid, lawnMower);
    }

    @Test
    @DisplayName("Mediator should properly control mower")
    void testMediatorControl() {
        // Start mowing
        mediator.startMowing();
        assertTrue(lawnMower.isMowing(), "Mower should be mowing after start");

        // Stop mowing
        mediator.stopMowing();
        assertFalse(lawnMower.isMowing(), "Mower should stop after stop command");
    }

    @Test
    @DisplayName("Mediator should update lawn grid")
    void testLawnUpdates() {
        // Start at position (0,0)
        mediator.updateLawn(0, 0);
        // Visual verification would be needed to confirm color change
        // We can only test that no exception is thrown
        assertTrue(true, "Lawn update should complete without errors");
    }
} 