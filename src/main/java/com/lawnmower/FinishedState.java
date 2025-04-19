package com.lawnmower;

public class FinishedState implements MowerState {
    private LawnMower mower;

    public FinishedState(LawnMower mower) {
        this.mower = mower;
    }

    @Override
    public void execute() {
        System.out.println("Mowing completed!");
    }
}
