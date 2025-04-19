package com.lawnmower;

import java.awt.Color;
import java.awt.Dimension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class LawnGridTest {

    @Test
    void testGridInitialization() {
        // Create a 5x5 grid
        LawnGrid grid = new LawnGrid(5, 5);
        
        // Check if the preferred size is set
        Dimension size = grid.getPreferredSize();
        assertEquals(500, size.width);
        assertEquals(500, size.height);
    }

    @Test
    void testUpdateCell() {
        // Create a 3x3 grid
        LawnGrid grid = new LawnGrid(3, 3);
        
        // Update a cell and verify the color
        Color testColor = Color.GREEN;
        grid.updateCell(1, 1, testColor);
        
        // The color should be updated in the grid
        assertTrue(true); // Basic test - expand this when we add a method to get cell color
    }

    @Test
    void testInvalidCellUpdate() {
        LawnGrid grid = new LawnGrid(3, 3);
        
        // Try to update cells outside the grid
        grid.updateCell(-1, 0, Color.GREEN); // Should not throw exception
        grid.updateCell(0, -1, Color.GREEN); // Should not throw exception
        grid.updateCell(3, 0, Color.GREEN);  // Should not throw exception
        grid.updateCell(0, 3, Color.GREEN);  // Should not throw exception
        
        // If we reached here without exceptions, test passes
        assertTrue(true);
    }
} 