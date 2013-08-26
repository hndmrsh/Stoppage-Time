package com.samuelhindmarsh.ld27.game;

import java.awt.Color;
import java.awt.Graphics;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.managers.ImageManager;

public class Player {

	private int x, y;
	private int number;
	private String name;

	public Player(int x, int y, int number, String name) {
		this.x = x;
		this.y = y;
		this.number = number;
		this.name = name;
	}

	public void render(Graphics g, int displayWidth, int displayHeight, Color c, int offset){
		g.setColor(c);

		g.fillOval(offset + x - (Configuration.PLAYER_SIZE / 2), offset + y - (Configuration.PLAYER_SIZE / 2),
				Configuration.PLAYER_SIZE, Configuration.PLAYER_SIZE);


		g.setColor(Color.white);
		g.setFont(ImageManager.getFont(10f));
		String numberString = ""+number;
		g.drawString(numberString, offset + x - (numberString.length() * 4), offset + y + 5);

		g.setFont(ImageManager.getFont(8f));
		g.drawString(name, offset + x - (name.length() * 3), offset + y - Configuration.PLAYER_SIZE / 2);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}



}
