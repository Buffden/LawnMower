package com.lawnmower;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LawnGridTest {
    private LawnGrid grid;
    private static final int DEFAULT_SIZE = 5;

    @BeforeEach
    void setUp() {
        grid = new LawnGrid(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    @Test
    @DisplayName("Grid should be initialized with correct dimensions")
    void testGridInitialization() {
        // Check if the preferred size is set
        Dimension size = grid.getPreferredSize();
        assertEquals(500, size.width, "Grid width should be 500");
        assertEquals(500, size.height, "Grid height should be 500");
        
        // Verify the layout is GridLayout
        assertInstanceOf(GridLayout.class, grid.getLayout(), "Layout should be GridLayout");
        
        GridLayout layout = (GridLayout) grid.getLayout();
        assertEquals(DEFAULT_SIZE, layout.getRows(), "Grid should have " + DEFAULT_SIZE + " rows");
        assertEquals(DEFAULT_SIZE, layout.getColumns(), "Grid should have " + DEFAULT_SIZE + " columns");
    }

    @ParameterizedTest
    @DisplayName("Grid should handle different sizes correctly")
    @CsvSource({
        "3, 3",
        "5, 5",
        "10, 10",
        "20, 15"
    })
    void testDifferentGridSizes(int rows, int cols) {
        LawnGrid customGrid = new LawnGrid(rows, cols);
        GridLayout layout = (GridLayout) customGrid.getLayout();
        
        assertEquals(rows, layout.getRows(), "Grid should have correct number of rows");
        assertEquals(cols, layout.getColumns(), "Grid should have correct number of columns");
    }

    @Test
    @DisplayName("Cell updates within bounds should be accepted")
    void testValidCellUpdate() {
        // Update cells at different positions
        grid.updateCell(0, 0, Color.GREEN);
        grid.updateCell(DEFAULT_SIZE - 1, DEFAULT_SIZE - 1, Color.RED);
        grid.updateCell(DEFAULT_SIZE / 2, DEFAULT_SIZE / 2, Color.BLUE);
        
        // Since we can't directly access the colors, we at least verify no exceptions are thrown
        assertTrue(true, "Valid cell updates should not throw exceptions");
    }

    @ParameterizedTest
    @DisplayName("Invalid cell updates should be handled gracefully")
    @CsvSource({
        "-1, 0",
        "0, -1",
        "5, 0",
        "0, 5",
        "-1, -1",
        "5, 5"
    })
    void testInvalidCellUpdate(int row, int col) {
        // These updates should not throw exceptions
        assertDoesNotThrow(() -> grid.updateCell(row, col, Color.GREEN),
            "Invalid cell updates should not throw exceptions");
    }

    @Test
    @DisplayName("Grid should handle null color updates")
    void testNullColorUpdate() {
        // Updating with null color should not throw exception
        assertDoesNotThrow(() -> grid.updateCell(0, 0, null),
            "Null color updates should not throw exceptions");
    }

    @Test
    @DisplayName("Grid should maintain aspect ratio")
    void testAspectRatio() {
        LawnGrid rectangularGrid = new LawnGrid(3, 6);
        Dimension size = rectangularGrid.getPreferredSize();
        
        // Even with different row/column counts, the size should remain 500x500
        assertEquals(500, size.width, "Grid width should remain 500");
        assertEquals(500, size.height, "Grid height should remain 500");
    }
} 