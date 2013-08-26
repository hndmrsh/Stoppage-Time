package com.samuelhindmarsh.ld27.game;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

	private static final double DECELERATION = -0.02;

	private	double speed;
	private double angle;

	private double x, y;
	private int toX, toY;

	private boolean isOut, isGoal;

	public Ball(int startX, int startY) {
		this.x = startX;
		this.y = startY;
	}


	public void moveTo(int x, int y){
		double x2 = Math.max(this.x, x);
		double x1 = Math.min(this.x, x);
		double y2 = Math.max(this.y, y);
		double y1 = Math.min(this.y, y);

		this.speed = Math.hypot(x2 - x1, y2 - y1) / 50.0;
		this.angle = Math.toDegrees(Math.atan2(y - this.y, x - this.x));
		this.toX = x;
		this.toY = y;
	}

	public void update(){
		if(speed > 0.0){
			x += Math.cos(Math.toRadians(angle)) * speed;
			y += Math.sin(Math.toRadians(angle)) * speed;
			speed = Math.max(0.0, speed + DECELERATION);
		}

		if(shouldStop()){
			speed  = 0.0;
		}

		checkIfGoal();
		checkIfOut();
	}


	private void checkIfGoal() {
		if(!isGoal && !isOut){
			isGoal = (x - 6 >= 196 && x + 6 <= 314) && (y - 6 > 10 && y + 6 < 55);
		}
	}


	private void checkIfOut() {
		if(!isGoal && !isOut){
			isOut = (x+6 < 20 || x-6 > 492 || (y+6 < 55 || y-6 > 500));
		}

	}


	private boolean shouldStop() {
		if(x - 6 < 0 || y - 6 < 0 || x + 6 > 512 || y + 6> 550){
			return true;
		} else if(isGoal && (y - 6 > 10 && y + 6 < 55) && (x - 6 < 196 || x + 6 > 314)){
			return true;
		} else if(isOut && (y + 6 > 10 && y - 6 < 55) && (x + 6 > 196 && x - 6 < 314)){
			return true;
		} else if((x - 6 >= 196 && x + 6 <= 314) && (y - 6 < 10)){
			return true;
		}
		return false;
	}


	public void render(Graphics g, int displayWidth, int displayHeight, int offset){
		g.setColor(Color.white);
		g.fillOval((int)x-6+offset, (int)y-6+offset, 12, 12);
		g.setColor(Color.black);
		g.drawOval((int)x-6+offset, (int)y-6+offset, 12, 12);

		if(speed > 0.0){
			g.setColor(Color.white);
			g.drawLine((int)x+offset,(int) y+offset, (int)toX + offset, (int)toY + offset);
		}
	}

	public boolean isGoal() {
		return isGoal;
	}

	public boolean isOut() {
		return isOut;
	}

}
