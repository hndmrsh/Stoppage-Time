package com.samuelhindmarsh.ld27.game;

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
