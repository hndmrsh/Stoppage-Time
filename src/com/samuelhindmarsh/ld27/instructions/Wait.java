package com.samuelhindmarsh.ld27.instructions;

import java.awt.Color;
import java.awt.Graphics;

import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Player;

public class Wait extends Instruction {

	private int wait;
	
	public Wait(int wait) {
		super(0,0);
		this.wait = wait * 1000;
	}
	
	@Override
	public void execute(Player p, Ball b) {
		p.setWait(wait);
	}
	
	@Override
	public void render(Graphics g, int offset, int fromX, int fromY) {
		g.setColor(Color.green);
		g.drawString((wait / 1000) + "", fromX - 10, fromY + 20);
	}
	
	

}
