package com.lawnmower;

public class IdleState implements MowerState {
    private LawnMower mower;

    public IdleState(LawnMower mower) {
        this.mower = mower;
    }

    @Override
    public void execute() {
        System.out.println("Mower is idle. Press Start to begin mowing.");
    }
}

// Add MowingState, PausedState, FinishedState similarly.