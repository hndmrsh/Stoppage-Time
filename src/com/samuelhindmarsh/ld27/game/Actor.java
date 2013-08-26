package com.samuelhindmarsh.ld27.game;

import java.awt.Graphics;

import com.samuelhindmarsh.ld27.instructions.Instruction;

public abstract class Actor {

	protected Instruction instructionCallback;
	protected double speed;
	protected double deceleration;
	
	public abstract void render(Graphics g, int displayWidth, int displayHeight, int offset);

	public abstract void update();

	protected double angle;
	protected double x;
	protected double y;
	protected int radius;

	public Actor() {
		super();
	}

	public boolean intersects(Actor other){
		double dist = Math.hypot(this.x - other.x, this.y - other.y);
		return dist < Math.abs(this.radius + other.radius);
	}

}