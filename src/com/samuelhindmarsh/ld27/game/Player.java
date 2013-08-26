package com.samuelhindmarsh.ld27.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.instructions.Instruction;
import com.samuelhindmarsh.ld27.instructions.Move;
import com.samuelhindmarsh.ld27.instructions.Pass;
import com.samuelhindmarsh.ld27.instructions.Wait;
import com.samuelhindmarsh.ld27.managers.ImageManager;

public class Player extends Actor {

	private boolean playing = false;

	private int number;
	private String name;
	private Color colour;

	private Ball ball;

	private boolean hasPossession;
	private Instruction currentInstruction;

	private boolean selected;

	private boolean moving;

	private int wait = 0;
	
	private double toX, toY;

	Queue<Instruction> instructions = new LinkedList<Instruction>();

	public Player(int x, int y, int number, String name, Color c) {
		this.x = x;
		this.y = y;
		this.colour = c;
		this.radius = 10;
		this.number = number;
		this.name = name;
	}

	public void render(Graphics g, int displayWidth, int displayHeight,
			int offset) {
		g.setColor(colour);

		g.fillOval((int) (offset + x - (radius)),
				(int) (offset + y - (radius)), 2 * radius, 2 * radius);
		if (selected) {
			g.setColor(Color.yellow);
			g.drawOval((int) (offset + x - (radius)),
					(int) (offset + y - (radius)), 2 * radius, 2 * radius);
		} else if (hasPossession) {
			g.setColor(Color.white);
			g.drawOval((int) (offset + x - (radius)),
					(int) (offset + y - (radius)), 2 * radius, 2 * radius);
		}

		g.setColor(Color.white);
		g.setFont(ImageManager.getFont(10f));
		String numberString = "" + number;
		g.drawString(numberString,
				(int) (offset + x - (numberString.length() * 4)), (int) (offset
						+ y + 5));

		g.setFont(ImageManager.getFont(8f));
		g.drawString(name, (int) (offset + x - (name.length() * 3)),
				(int) (offset + y - radius));

		if(!playing){
			renderInstructions(g, offset);
		}
	}

	private void renderInstructions(Graphics g, int offset) {
		int fromX = (int) x+offset;
		int fromY = (int) y+offset;
		for (Instruction i : instructions) {
			i.render(g, offset, fromX, fromY);
			if(i instanceof Move){
				fromX = (int) i.getX()+offset;
				fromY = (int) i.getY()+offset;
			}
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public void setHasPossession(boolean hasPossession) {
		this.hasPossession = hasPossession;
	}

	public boolean hasPossession() {
		return hasPossession;
	}

	public void update() {
		playing = true;

		if (instructions.size() > 0 && (currentInstruction == null || currentInstruction.isComplete())) {
			currentInstruction = instructions.poll();
			currentInstruction.execute(this, ball);
		}

		if (wait > 0){
			wait = (int) Math.max(wait - (1000.0 / Configuration.FRAMERATE), 0);
		} else if(currentInstruction instanceof Wait){
			currentInstruction.setComplete(true);
		}
		
		
		if (moving) {
			double dX = Math.cos(Math.toRadians(angle)) * speed;
			double dY = Math.sin(Math.toRadians(angle)) * speed;
			x += dX;
			y += dY;
			if(hasPossession){
				ball.x += dX;
				ball.y += dY;
			}
			if (Math.abs(toX - x) < speed && Math.abs(toY - y) < speed) {
				x = toX;
				y = toY;
				moving = false;
				currentInstruction.setComplete(true);
			}
		}

		if(currentInstruction instanceof Pass && !currentInstruction.isComplete()){
			currentInstruction.execute(this, ball);
		}
	}

	public boolean clickedOn(int x, int y) {
		double dist = Math.hypot(this.x - x, this.y - y);
		System.out.println(dist + "");
		return dist < radius;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public void queue(Instruction i) {
		instructions.offer(i);
	}

	public void moveTo(double x, double y, Instruction instructionCallback) {
		this.toX = x;
		this.toY = y;
		angle = Math.toDegrees(Math.atan2(this.toY - this.y, this.toX - this.x));
		speed = 1.0;
		deceleration = 0.0;
		this.instructionCallback = instructionCallback;
		moving = true;
	}

	public Queue<Instruction> getInstructions() {
		return instructions;
	}
	
	public void setWait(int wait) {
		this.wait = wait;
	}
}
