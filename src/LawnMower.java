import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LawnMower {
    private int rows;
    private int cols;
    private int currentRow;
    private int currentCol;
    private boolean mowing;
    private List<MowerObserver> observers = new ArrayList<>();
    private Timer timer;

    // Constructor
    public LawnMower(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.currentRow = 0;
        this.currentCol = 0;
        this.mowing = false;
        this.timer = new Timer();
    }

    // Add an observer
    public void addObserver(MowerObserver observer) {
        observers.add(observer);
    }

    // Notify all observers
    private void notifyObservers(int row, int col) {
        for (MowerObserver observer : observers) {
            observer.onCellMowed(row, col);
        }
    }

    // Start mowing
    public void start() {
        if (!mowing) {
            mowing = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mow();
                }
            }, 0, 1000); // Mows every second
        }
    }

    // Stop mowing
    public void stop() {
        if (mowing) {
            mowing = false;
            timer.cancel();
            timer = new Timer(); // Reset the timer for future use
        }
    }

    // Mow the current cell and move to the next
    private void mow() {
        if (!mowing) return;

        // Notify observers to update the grid
        notifyObservers(currentRow, currentCol);

        // Move to the next cell
        if (currentRow % 2 == 0) { // Even row: move left to right
            currentCol++;
            if (currentCol >= cols) { // Reached the right edge
                currentCol = cols - 1;
                moveToNextRow();
            }
        } else { // Odd row: move right to left
            currentCol--;
            if (currentCol < 0) { // Reached the left edge
                currentCol = 0;
                moveToNextRow();
            }
        }
    }

    // Move to the next row or stop if finished
    private void moveToNextRow() {
        currentRow++;
        if (currentRow >= rows) {
            stop(); // Finished mowing
        }
    }
}
