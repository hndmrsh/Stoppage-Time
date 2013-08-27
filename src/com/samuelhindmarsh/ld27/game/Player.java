package com.samuelhindmarsh.ld27.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.instructions.Instruction;
import com.samuelhindmarsh.ld27.instructions.Move;
import com.samuelhindmarsh.ld27.instructions.Pass;
import com.samuelhindmarsh.ld27.instructions.Shoot;
import com.samuelhindmarsh.ld27.instructions.Wait;
import com.samuelhindmarsh.ld27.managers.ImageManager;

public class Player extends Actor {

	private boolean playing = false;

	private int number;
	private String name;
	private Color colour;

	protected Ball ball;

	private boolean hasPossession;
	private Instruction currentInstruction;

	private boolean selected;

	private boolean moving;

	private int wait = 0;

	private double toX, toY;


	Queue<Instruction> instructions = new LinkedList<Instruction>();
	Queue<Instruction> doneInstructions = new LinkedList<Instruction>();


	public Player(int x, int y, int number, String name, Color c) {
		super(x, y);
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
			doneInstructions.add(currentInstruction);
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
				if(currentInstruction != null){
					currentInstruction.setComplete(true);
				}
			}
		}

		if((currentInstruction instanceof Pass || currentInstruction instanceof Shoot) && !currentInstruction.isComplete()){
			currentInstruction.execute(this, ball);
		}
	}

	public boolean clickedOn(int x, int y) {
		double dist = Math.hypot(this.x - x, this.y - y);
		if(Configuration.DEBUGGING){
			System.out.println(dist + "");
		}
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

	public void moveTo(double x, double y, double speed) {
		this.toX = x;
		this.toY = y;
		this.speed = speed;
		angle = Math.toDegrees(Math.atan2(this.toY - this.y, this.toX - this.x));
		deceleration = 0.0;
		moving = true;
	}

	public Queue<Instruction> getInstructions() {
		return instructions;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}

	@Override
	public void reset() {
		while(!instructions.isEmpty()){
			doneInstructions.offer(instructions.poll());
		}
		while(!doneInstructions.isEmpty()){
			Instruction i = doneInstructions.poll();
			i.reset();
			queue(i);
		}

		this.hasPossession = false;
		this.currentInstruction = null;
		this.moving = false;
		this.toX = 0.0;
		this.toY = 0.0;
		this.x = initialX;
		this.y = initialY;
		this.playing = false;
	}


	public void setMoving(boolean moving) {
		this.moving = moving;
	}

}
