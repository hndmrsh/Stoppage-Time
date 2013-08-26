package com.samuelhindmarsh.ld27.instructions;

import java.awt.Color;
import java.awt.Graphics;

import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Player;

public class Move extends Instruction {

	public Move(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void execute(Player p, Ball b) {
		p.moveTo(x, y, this);
	}
	
	@Override
	public void render(Graphics g, int offset, int fromX, int fromY) {
		g.setColor(Color.white);
		g.drawLine(fromX, fromY, x+offset, y+offset);
	}
	
	

}
