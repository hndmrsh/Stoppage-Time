package com.samuelhindmarsh.ld27.game;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.ui.StoppageTimeCanvas;

public class GameLoop implements Runnable {

	private boolean running;
	
	private StoppageTimeGame game;
	private StoppageTimeCanvas canvas;
	
	public GameLoop(StoppageTimeGame game, StoppageTimeCanvas canvas) {
		this.game = game;
		this.canvas = canvas;
	}
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		long nanosPerFrame = 1000000000 / Configuration.FRAMERATE;
		int frameCount = 0;
		long secondCount = 0;
		while(running){
			long start = System.nanoTime();
			
			game.update();
			canvas.repaint();
			
			long end = System.nanoTime();
			long diff = (nanosPerFrame - (end - start));
			if(diff > 0){
				try {
					Thread.sleep(diff / 1000000, (int) (diff % 1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			frameCount++;
			secondCount += (end - start) + diff;

			if(secondCount >= 999999900){
				canvas.setFps(frameCount);
				
				secondCount = 0;
				frameCount = 0;
			}
		}
	}

}
