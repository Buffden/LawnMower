package com.lawnmower;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MowerMediatorTest {
    private LawnGrid lawnGrid;
    private LawnMower mower;
    private MowerMediator mediator;

    @BeforeEach
    void setUp() {
        lawnGrid = new LawnGrid(5, 5);
        mower = new LawnMower(5, 5);
        mediator = new MowerMediator(lawnGrid, mower);
    }

    @Test
    @DisplayName("MowerMediator should be created successfully")
    void testMediatorCreation() {
        assertNotNull(mediator, "Mediator should be created");
    }

    @Test
    @DisplayName("MowerMediator should start mowing")
    void testStartMowing() {
        assertDoesNotThrow(() -> mediator.startMowing(),
            "Should be able to start mowing");
    }

    @Test
    @DisplayName("MowerMediator should stop mowing")
    void testStopMowing() {
        assertDoesNotThrow(() -> mediator.stopMowing(),
            "Should be able to stop mowing");
    }

    @Test
    @DisplayName("MowerMediator should handle multiple start/stop cycles")
    void testMultipleStartStopCycles() {
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> mediator.startMowing(),
                "Should be able to start mowing multiple times");
            assertDoesNotThrow(() -> mediator.stopMowing(),
                "Should be able to stop mowing multiple times");
        }
    }
} 