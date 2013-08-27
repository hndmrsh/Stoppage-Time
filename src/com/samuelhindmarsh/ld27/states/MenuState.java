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
		play.setHovering(play.intersects(x, y));
		exit.setHovering(exit.intersects(x, y));
	}

	public void mouseClicked(int x, int y, boolean rightClick){
		if(play.intersects(x, y)){
			play.setActive(true);
			game.newGame();
		} else if(exit.intersects(x, y)){
			exit.setActive(true);
			game.exit();
		}
	}



}
