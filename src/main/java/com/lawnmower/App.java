package com.lawnmower;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class App {
    public static void main(String[] args) {
        // Check if running in headless mode
        if (Boolean.getBoolean("java.awt.headless")) {
            System.out.println("Running in headless mode");
            return;
        }

        JFrame frame = new JFrame("Hello Swing");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel("Swing is working!"));
        frame.setVisible(true);
    }
}
