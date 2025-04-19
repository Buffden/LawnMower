package com.lawnmower;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StateImplementationTest {
    private LawnMower lawnMower;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        lawnMower = new LawnMower(5, 5);
        // Set up output capture
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("IdleState should prompt to press Start")
    void testIdleState() {
        // Create and execute idle state
        MowerState idleState = new IdleState(lawnMower);
        idleState.execute();
        
        assertEquals("Mower is idle. Press Start to begin mowing.\n", 
                    outputStream.toString(),
                    "IdleState should display correct message");
    }

    @Test
    @DisplayName("MowingState should indicate mower is moving")
    void testMowingState() {
        MowerState mowingState = new MowingState(lawnMower);
        mowingState.execute();
        
        assertEquals("Mower is moving. Press stop to stop.\n", 
                    outputStream.toString(),
                    "MowingState should display correct message");
    }

    @Test
    @DisplayName("PausedState should prompt to resume")
    void testPausedState() {
        MowerState pausedState = new PausedState(lawnMower);
        pausedState.execute();
        
        assertEquals("Mower is paused. Press resume to continue.\n", 
                    outputStream.toString(),
                    "PausedState should display correct message");
    }

    @Test
    @DisplayName("FinishedState should indicate completion")
    void testFinishedState() {
        MowerState finishedState = new FinishedState(lawnMower);
        finishedState.execute();
        
        assertEquals("Mowing completed!\n", 
                    outputStream.toString(),
                    "FinishedState should display correct message");
    }

    @Test
    @DisplayName("States should maintain reference to LawnMower")
    void testStateContext() {
        MowerState[] states = {
            new IdleState(lawnMower),
            new MowingState(lawnMower),
            new PausedState(lawnMower),
            new FinishedState(lawnMower)
        };
        
        for (MowerState state : states) {
            assertNotNull(state, "State should be properly instantiated");
            state.execute(); // Should not throw any exceptions
        }
    }
} 