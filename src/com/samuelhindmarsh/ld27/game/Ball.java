package com.samuelhindmarsh.ld27.game;

import java.awt.Color;
import java.awt.Graphics;

import com.samuelhindmarsh.ld27.instructions.Instruction;
import com.samuelhindmarsh.ld27.instructions.Move;

public class Ball extends Actor {

	private Player playerWithBall;
	private Player lastKicked;

	private boolean isOut, isGoal;

	private double toX;

	public Ball(int startX, int startY) {
		super(startX, startY);
		this.radius = 6;
	}


	@Override
	public void update(){
		if(shouldStop()){
			speed = 0.0;
		}

		if(speed > 0.0){
			x += Math.cos(Math.toRadians(angle)) * speed;
			y += Math.sin(Math.toRadians(angle)) * speed;
			speed = Math.max(0.0, speed - deceleration);
		}

		checkIfGoal();
		checkIfOut();
	}


	private void checkIfGoal() {
		if(!isGoal && !isOut){
			isGoal = (x - radius >= 196 && x + radius <= 314) && (y - radius > 10 && y + radius < 55);
		}
	}


	private void checkIfOut() {
		if(!isGoal && !isOut){
			isOut = (x+radius < 20 || x-radius > 492 || (y+radius < 55 || y-radius > 500));
		}

	}


	private boolean shouldStop() {
		if(playerWithBall != null){
			return true;
		} else if(x - radius < 0 || y - radius < 0 || x + radius > 512 || y + radius> 550){
			return true;
		} else if(isGoal && (y - radius > 10 && y + radius < 55) && (x - radius < 196 || x + radius > 314)){
			return true;
		} else if(isOut && (y + radius > 10 && y - radius < 55) && (x + radius > 196 && x - radius < 314)){
			return true;
		} else if((x - radius >= 196 && x + radius <= 314) && (y - radius < 10)){
			return true;
		}
		return false;
	}


	@Override
	public void render(Graphics g, int displayWidth, int displayHeight, int offset){
		g.setColor(Color.white);
		g.fillOval((int)x-radius+offset, (int)y-radius+offset, 2*radius, 2*radius);
		g.setColor(Color.black);
		g.drawOval((int)x-radius+offset, (int)y-radius+offset, 2*radius, 2*radius);
	}

	public boolean isGoal() {
		return isGoal;
	}

	public boolean isOut() {
		return isOut;
	}

	public Player getPlayerWithBall() {
		return playerWithBall;
	}

	public void setPlayerWithBall(Player playerWithBall) {
		this.playerWithBall = playerWithBall;
	}

	public Player getLastKicked() {
		return lastKicked;
	}

	public void setLastKicked(Player lastKicked) {
		this.lastKicked = lastKicked;
	}

	public void moveTo(int x, int y, boolean shot) {
		this.speed = Math.min(7.0, Math.hypot(this.x - x, this.y - y) / (shot ? 20.0 : 60.0)); // cap ball speed at 7
		this.toX = x;
		this.deceleration = speed / 180.0;
		this.angle = Math.toDegrees(Math.atan2(y - this.y, x - this.x));
	}

	@Override
	public void reset() {
		this.x = this.initialX;
		this.y = this.initialY;
		this.angle = 0.0;
		this.deceleration = 0.0;
		this.isGoal = false;
		this.isOut = false;
		this.lastKicked = null;
		this.playerWithBall = null;
	}

	public double getToX() {
		return toX;
	}
}
