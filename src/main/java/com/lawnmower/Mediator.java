package com.lawnmower;

public interface Mediator {
    void startMowing();
    void stopMowing();
    void updateLawn(int row, int col);
}
