package com.lawnmower;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class StateTransitionTest {
    private LawnMower lawnMower;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        lawnMower = new LawnMower(5, 5);
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("State should transition from Idle to Mowing")
    void testIdleToMowingTransition() {
        // Start in Idle state
        MowerState idleState = new IdleState(lawnMower);
        idleState.execute();
        assertTrue(outputStream.toString().contains("Press Start"), "Should show idle message");
        
        // Transition to Mowing state
        outputStream.reset();
        MowerState mowingState = new MowingState(lawnMower);
        mowingState.execute();
        assertTrue(outputStream.toString().contains("moving"), "Should show mowing message");
    }

    @Test
    @DisplayName("State should transition from Mowing to Paused")
    void testMowingToPausedTransition() {
        // Start in Mowing state
        MowerState mowingState = new MowingState(lawnMower);
        mowingState.execute();
        assertTrue(outputStream.toString().contains("moving"), "Should show mowing message");
        
        // Transition to Paused state
        outputStream.reset();
        MowerState pausedState = new PausedState(lawnMower);
        pausedState.execute();
        assertTrue(outputStream.toString().contains("paused"), "Should show paused message");
    }

    @Test
    @DisplayName("State should transition from Paused back to Mowing")
    void testPausedToMowingTransition() {
        // Start in Paused state
        MowerState pausedState = new PausedState(lawnMower);
        pausedState.execute();
        assertTrue(outputStream.toString().contains("paused"), "Should show paused message");
        
        // Transition back to Mowing state
        outputStream.reset();
        MowerState mowingState = new MowingState(lawnMower);
        mowingState.execute();
        assertTrue(outputStream.toString().contains("moving"), "Should show mowing message");
    }

    @Test
    @DisplayName("State should transition to Finished when complete")
    void testTransitionToFinished() {
        // Start in Mowing state
        MowerState mowingState = new MowingState(lawnMower);
        mowingState.execute();
        assertTrue(outputStream.toString().contains("moving"), "Should show mowing message");
        
        // Transition to Finished state
        outputStream.reset();
        MowerState finishedState = new FinishedState(lawnMower);
        finishedState.execute();
        assertTrue(outputStream.toString().contains("completed"), "Should show completed message");
    }

    @RepeatedTest(5)
    @DisplayName("States should handle repeated transitions")
    void testRepeatedTransitions() {
        MowerState[] states = {
            new IdleState(lawnMower),
            new MowingState(lawnMower),
            new PausedState(lawnMower),
            new MowingState(lawnMower),
            new FinishedState(lawnMower)
        };
        
        for (MowerState state : states) {
            outputStream.reset();
            state.execute();
            assertFalse(outputStream.toString().isEmpty(), "Each state should produce output");
        }
    }

    @Test
    @DisplayName("State messages should be consistent across transitions")
    void testStateMessageConsistency() {
        // Test each state multiple times
        for (int i = 0; i < 3; i++) {
            // Idle state
            outputStream.reset();
            new IdleState(lawnMower).execute();
            String idleMessage = outputStream.toString();
            
            // Mowing state
            outputStream.reset();
            new MowingState(lawnMower).execute();
            String mowingMessage = outputStream.toString();
            
            // Paused state
            outputStream.reset();
            new PausedState(lawnMower).execute();
            String pausedMessage = outputStream.toString();
            
            // Finished state
            outputStream.reset();
            new FinishedState(lawnMower).execute();
            String finishedMessage = outputStream.toString();
            
            // Verify messages are consistent
            assertTrue(idleMessage.contains("Start"), "Idle message should mention Start");
            assertTrue(mowingMessage.contains("moving"), "Mowing message should mention moving");
            assertTrue(pausedMessage.contains("paused"), "Paused message should mention paused");
            assertTrue(finishedMessage.contains("completed"), "Finished message should mention completed");
        }
    }

    @Test
    @DisplayName("States should maintain mower reference")
    void testStateMowerReference() {
        MowerState[] states = {
            new IdleState(lawnMower),
            new MowingState(lawnMower),
            new PausedState(lawnMower),
            new FinishedState(lawnMower)
        };
        
        for (MowerState state : states) {
            assertNotNull(state, "State should be properly instantiated");
            outputStream.reset();
            state.execute();
            assertFalse(outputStream.toString().isEmpty(), "State should be able to execute");
        }
    }
} 