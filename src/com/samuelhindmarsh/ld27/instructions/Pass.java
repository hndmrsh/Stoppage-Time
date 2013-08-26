package com.samuelhindmarsh.ld27.instructions;

import java.awt.Color;
import java.awt.Graphics;

import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Player;

public class Pass extends Instruction {

	public Pass(int x, int y) {
		super(x, y);
	}

	@Override
	public void execute(Player p, Ball b) {
		if(p.hasPossession()){
			p.setHasPossession(false);
			b.setLastKicked(p);
			b.setPlayerWithBall(null);
			b.moveTo(x, y, false);
			complete = true;
		}
	}

	@Override
	public void render(Graphics g, int offset, int fromX, int fromY) {
		g.setColor(Color.yellow);
		g.drawLine(fromX, fromY, x+offset, y+offset);
	}

}
