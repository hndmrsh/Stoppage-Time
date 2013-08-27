package com.samuelhindmarsh.ld27.game;

import java.awt.Graphics;

import com.samuelhindmarsh.ld27.instructions.Instruction;

public abstract class Actor {

	protected double speed;
	protected double deceleration;

	public abstract void render(Graphics g, int displayWidth, int displayHeight, int offset);

	public abstract void update();

	protected double angle;
	protected double x, y;
	protected int radius;

	protected double initialX, initialY;


	public Actor(double x, double y) {
		this.x = x;
		this.y = y;

		this.initialX = this.x;
		this.initialY = this.y;

	}

	public boolean intersects(Actor other){
		double dist = Math.hypot(this.x - other.x, this.y - other.y);
		return dist < Math.abs(this.radius + other.radius);
	}

	public abstract void reset();

	public double distTo(Actor other){
		return Math.hypot(this.x - other.x, this.y - other.y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
}