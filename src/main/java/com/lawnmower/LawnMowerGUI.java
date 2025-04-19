package com.lawnmower;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LawnMowerGUI extends JFrame {
    private LawnGrid lawnGrid;

    public LawnMowerGUI() {
        setTitle("Lawn Mower Emulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        lawnGrid = new LawnGrid(10, 10);
        LawnMower mower = new LawnMower(10, 10);
        MowerMediator mediator = new MowerMediator(lawnGrid, mower);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(e -> mediator.startMowing());
        stopButton.addActionListener(e -> mediator.stopMowing());

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        setLayout(new BorderLayout());
        add(lawnGrid, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public LawnGrid getLawnGrid() {
        return lawnGrid;
    }
}
