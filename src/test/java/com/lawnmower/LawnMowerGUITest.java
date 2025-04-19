package com.lawnmower;

import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LawnMowerGUITest {
    private static boolean isHeadless;

    @BeforeAll
    static void setUp() {
        isHeadless = GraphicsEnvironment.isHeadless();
        if (isHeadless) {
            System.out.println("Running in headless environment. GUI tests will be skipped.");
        }
    }

    @Test
    @DisplayName("GUI should initialize with correct components")
    void testGUIInitialization() {
        assumeFalse(isHeadless, "Skipping test in headless environment");
        
        LawnMowerGUI gui = new LawnMowerGUI();
        assertNotNull(gui.getLawnGrid(), "LawnGrid should be initialized");
    }

    @Test
    @DisplayName("Window operations should work correctly")
    void testWindowOperations() {
        assumeFalse(isHeadless, "Skipping test in headless environment");
        
        LawnMowerGUI gui = new LawnMowerGUI();
        assertFalse(gui.isVisible(), "Window should not be visible initially");
        gui.setVisible(true);
        assertTrue(gui.isVisible(), "Window should be visible after setVisible(true)");
        gui.setVisible(false);
        assertFalse(gui.isVisible(), "Window should not be visible after setVisible(false)");
    }

    @Test
    @DisplayName("GUI buttons should trigger mediator actions")
    void testButtonActions() {
        assumeFalse(isHeadless, "Skipping test in headless environment");
        
        LawnMowerGUI gui = new LawnMowerGUI();
        LawnMower mower = new LawnMower(10, 10);
        MowerMediator mediator = new MowerMediator(gui.getLawnGrid(), mower);
        
        assertFalse(mower.isMowing(), "Mower should not be mowing initially");
        mediator.startMowing();
        assertTrue(mower.isMowing(), "Mower should be mowing after start");
        mediator.stopMowing();
        assertFalse(mower.isMowing(), "Mower should not be mowing after stop");
    }
} 