package com.lawnmower;

public class PausedState implements MowerState {
    private LawnMower mower;

    public PausedState(LawnMower mower) {
        this.mower = mower;
    }

    @Override
    public void execute() {
        System.out.println("Mower is paused. Press resume to continue.");
    }
}