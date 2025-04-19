package com.lawnmower;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LawnMowerGUITest {
    
    @Test
    @DisplayName("GUI should initialize with correct components")
    void testGUIInitialization() throws InterruptedException, InvocationTargetException {
        final JFrame[] frame = new JFrame[1];
        final CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeAndWait(() -> {
            frame[0] = new JFrame("Lawn Mower Emulator");
            int rows = 10;
            int cols = 10;

            LawnGrid lawnGrid = new LawnGrid(rows, cols);
            LawnMower mower = new LawnMower(rows, cols);
            MowerMediator mediator = new MowerMediator(lawnGrid, mower);

            JPanel controlPanel = new JPanel();
            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");

            startButton.addActionListener(e -> mediator.startMowing());
            stopButton.addActionListener(e -> mediator.stopMowing());

            controlPanel.add(startButton);
            controlPanel.add(stopButton);

            frame[0].setLayout(new BorderLayout());
            frame[0].add(lawnGrid, BorderLayout.CENTER);
            frame[0].add(controlPanel, BorderLayout.SOUTH);

            frame[0].setSize(600, 600);
            frame[0].pack();
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "GUI initialization timed out");
        
        SwingUtilities.invokeAndWait(() -> {
            // Test frame properties
            assertEquals("Lawn Mower Emulator", frame[0].getTitle(), "Frame title should match");
            assertTrue(frame[0].getSize().width > 0, "Frame should have positive width");
            assertTrue(frame[0].getSize().height > 0, "Frame should have positive height");
            
            // Test components
            Container contentPane = frame[0].getContentPane();
            assertEquals(BorderLayout.class, contentPane.getLayout().getClass(), "Layout should be BorderLayout");
            
            // Test control panel
            Component southComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(contentPane, BorderLayout.SOUTH);
            assertNotNull(southComponent, "Control panel should exist");
            assertTrue(southComponent instanceof JPanel, "South component should be JPanel");
            
            JPanel controlPanel = (JPanel) southComponent;
            Component[] buttons = controlPanel.getComponents();
            assertEquals(2, buttons.length, "Control panel should have 2 buttons");
            
            assertTrue(buttons[0] instanceof JButton, "First component should be JButton");
            assertTrue(buttons[1] instanceof JButton, "Second component should be JButton");
            assertEquals("Start", ((JButton) buttons[0]).getText(), "First button should be Start");
            assertEquals("Stop", ((JButton) buttons[1]).getText(), "Second button should be Stop");
            
            // Test lawn grid
            Component centerComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(contentPane, BorderLayout.CENTER);
            assertNotNull(centerComponent, "Lawn grid should exist");
            assertTrue(centerComponent instanceof LawnGrid, "Center component should be LawnGrid");
            
            frame[0].dispose();
        });
    }

    @Test
    @DisplayName("GUI buttons should trigger mediator actions")
    void testButtonActions() throws InterruptedException, InvocationTargetException {
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] mowingState = new boolean[1];
        
        SwingUtilities.invokeAndWait(() -> {
            int rows = 10;
            int cols = 10;

            LawnGrid lawnGrid = new LawnGrid(rows, cols);
            LawnMower mower = new LawnMower(rows, cols);
            MowerMediator mediator = new MowerMediator(lawnGrid, mower);

            JPanel controlPanel = new JPanel();
            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");

            startButton.addActionListener(e -> {
                mediator.startMowing();
                mowingState[0] = mower.isMowing();
            });
            
            stopButton.addActionListener(e -> {
                mediator.stopMowing();
                mowingState[0] = mower.isMowing();
            });

            // Test start button
            startButton.doClick();
            assertTrue(mowingState[0], "Mower should be mowing after start button click");

            // Test stop button
            stopButton.doClick();
            assertFalse(mowingState[0], "Mower should not be mowing after stop button click");

            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Button action test timed out");
    }

    @Test
    @DisplayName("GUI should handle window operations")
    void testWindowOperations() throws InterruptedException, InvocationTargetException {
        final JFrame[] frame = new JFrame[1];
        final CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeAndWait(() -> {
            frame[0] = new JFrame("Lawn Mower Emulator");
            frame[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame[0].setSize(600, 600);
            
            // Test frame state
            assertFalse(frame[0].isVisible(), "Frame should not be visible initially");
            frame[0].setVisible(true);
            assertTrue(frame[0].isVisible(), "Frame should be visible after setVisible(true)");
            
            assertEquals(600, frame[0].getSize().width, "Frame width should be 600");
            assertEquals(600, frame[0].getSize().height, "Frame height should be 600");
            
            frame[0].dispose();
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Window operations test timed out");
    }
} 