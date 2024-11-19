public class MowingState implements MowerState {
    private LawnMower mower;

    public MowingState(LawnMower mower) {
        this.mower = mower;
    }

    @Override
    public void execute() {
        System.out.println("Mower is moving. Press stop to stop.");
    }
}