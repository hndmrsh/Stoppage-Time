package com.samuelhindmarsh.ld27.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.game.GameLoop;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;
import com.samuelhindmarsh.ld27.managers.ImageManager;

public class MenuState implements State {

	private StoppageTimeGame game;
	private int mouseX = 0, mouseY = 0;
	private Button play;
	private Button exit;

	public MenuState(StoppageTimeGame game) {
		this.game = game;
		play = new Button("play", Configuration.WIDTH / 2, (Configuration.HEIGHT / 20) * 12);
		exit = new Button("exit", Configuration.WIDTH / 2, (Configuration.HEIGHT / 20) * 16);
	}

	@Override
	public void render(Graphics g, int displayWidth, int displayHeight) {
		BufferedImage title = ImageManager.getImage("title");
		g.drawImage(title, (displayWidth - title.getWidth()) / 2, displayHeight / 20, title.getWidth(), title.getHeight(), null); 

		play.render(g, displayWidth, displayHeight);
		exit.render(g, displayWidth, displayHeight);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}
	
	public void mouseClicked(int x, int y){
		if(play.intersects(x, y)){
			game.newGame();
		} else if(exit.intersects(x, y)){
			game.exit();
		}
	}

	private class Button {
		private String name;
		private int x, y;
		private int width, height;

		public Button(String name, int x, int y) {
			this.name = name;

			BufferedImage img = ImageManager.getImage(name);
			width = img.getWidth();
			height = img.getHeight();

			this.x = x - (width/2);
			this.y = y - (height/2);


		}

		public boolean intersects(int x, int y){
			return (x > this.x && x <= this.x + width) && (y > this.y && y <= this.y + height);
		}

		public void render(Graphics g, int displayWidth, int displayHeight){
			BufferedImage img = null;
			if(intersects(MenuState.this.mouseX, MenuState.this.mouseY)){
				img = ImageManager.getImage(name + "-hover");
			} else {
				img = ImageManager.getImage(name);
			}

			g.drawImage(img, x, y, width, height, null);
			if(Configuration.DEBUGGING){
				g.setColor(Color.green);
				g.drawRect(x, y, width, height);
			}
		}

	}

}
