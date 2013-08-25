package com.samuelhindmarsh.ld27.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;

public class StoppageTimeCanvas extends JPanel {

	private int fps;
	
	private StoppageTimeGame game;
	
	public StoppageTimeCanvas(StoppageTimeGame game) {
		this.game = game;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		game.render(g, getWidth(), getHeight());
		
		if(Configuration.DEBUGGING){
			g.setColor(Color.white);
			g.drawString("FPS: " + fps, 10, 20);
		}
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}
	

}
