package com.lawnmower;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;


public class LawnGrid extends JPanel {
    private int rows;
    private int cols;
    private Color[][] gridColors;

    // Constructor
    public LawnGrid(int rows, int cols) {
        // Validate input dimensions
        if (rows <= 0 || cols <= 0) {
            // Set minimum valid dimensions if invalid values are provided
            this.rows = Math.max(1, rows);
            this.cols = Math.max(1, cols);
        } else {
            this.rows = rows;
            this.cols = cols;
        }
        
        gridColors = new Color[this.rows][this.cols];

        // Initialize the grid with dark green (uncut grass)
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                gridColors[i][j] = Color.RED;
            }
        }

        // Set the layout to grid
        setLayout(new GridLayout(this.rows, this.cols));
        setPreferredSize(new Dimension(500, 500)); // Example size
    }

    // Method to update a cell's color
    public void updateCell(int row, int col, Color color) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            gridColors[row][col] = color;
            repaint(); // Trigger re-painting
        }
    }

    // Override paintComponent to draw the grid
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(gridColors[i][j]);
                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.BLACK); // Grid line color
                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }
    }
}
