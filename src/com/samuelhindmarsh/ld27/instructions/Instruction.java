package com.samuelhindmarsh.ld27.instructions;

import java.awt.Graphics;

import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Player;

public abstract class Instruction {

	protected int x, y; 
	protected boolean complete;

	public Instruction(int x, int y) {
		this.x = x;
		this.y = y;
		complete = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public abstract void execute(Player p, Ball b);

	public abstract void render(Graphics g, int offset, int fromX, int fromY);

}
