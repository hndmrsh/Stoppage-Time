package com.samuelhindmarsh.ld27.states;

import java.awt.Graphics;

public interface State {

	public void render(Graphics g, int displayWidth, int displayHeight);
	
	public void update();

	public void mouseMoved(int x, int y);
	
	public void mouseClicked(int x, int y);
}
