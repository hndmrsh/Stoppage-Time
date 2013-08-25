package com.samuelhindmarsh.ld27.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.game.GameLoop;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;
import com.samuelhindmarsh.ld27.managers.InputManager;

public class StoppageTimeWindow extends JFrame {

	private StoppageTimeCanvas canvas;
	
	public StoppageTimeWindow() {
		StoppageTimeGame game = new StoppageTimeGame();
		canvas = new StoppageTimeCanvas(game);
		canvas.setPreferredSize(new Dimension(Configuration.WIDTH, Configuration.HEIGHT));
		add(canvas, BorderLayout.CENTER);
		
		new GameLoop(game, canvas).start();
		
		InputManager im = new InputManager(game);
		addKeyListener(im);
		canvas.addMouseListener(im);
		canvas.addMouseMotionListener(im);
		
		pack();
		
		setTitle("Stoppage Time");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
	}
	
	public static void main(String[] args) {
		new StoppageTimeWindow();
	}
}
