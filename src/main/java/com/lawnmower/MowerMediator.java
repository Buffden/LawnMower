package com.lawnmower;

import java.awt.Color;

public class MowerMediator implements Mediator {
    private LawnGrid lawnGrid;
    private LawnMower lawnMower;

    public MowerMediator(LawnGrid lawnGrid, LawnMower lawnMower) {
        this.lawnGrid = lawnGrid;
        this.lawnMower = lawnMower;

        // Add LawnGrid as an observer
        lawnMower.addObserver((row, col) -> updateLawn(row, col));
    }

    @Override
    public void startMowing() {
        lawnMower.start();
    }

    @Override
    public void stopMowing() {
        lawnMower.stop();
    }

    @Override
    public void updateLawn(int row, int col) {
        lawnGrid.updateCell(row, col, Color.green);
    }
}
