package com.samuelhindmarsh.ld27.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Goalkeeper;
import com.samuelhindmarsh.ld27.game.Player;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;
import com.samuelhindmarsh.ld27.managers.CommentaryManager;
import com.samuelhindmarsh.ld27.managers.ImageManager;
import com.samuelhindmarsh.ld27.managers.ScenarioManager;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny;

public class GameState implements State {

	private int offset = 25;

	private boolean scenarioLoaded = false;

	private HashSet<Player> playerTeam;
	private HashSet<Player> cpuTeam;
	private Goalkeeper keeper;
	private Ball ball;

	private boolean playerScored = false;
	private String commentary;
	private int score;
	private int secondsElapsed = 0;

	private boolean gameOver = false;
	private boolean finished = false;

	public GameState(StoppageTimeGame game, File scenario) {
		playerTeam = new HashSet<Player>();
		cpuTeam = new HashSet<Player>();

		boolean success = false;
		try {
			success = ScenarioManager.parseScenario(scenario, this);
			for(Player p : cpuTeam){
				if(p instanceof Goalkeeper){
					keeper = (Goalkeeper) p;
				}
			}
			cpuTeam.remove(keeper);
		} catch (FileNotFoundException e) {
			// should never happen
			e.printStackTrace();
		}

		scenarioLoaded = success;

		commentary = CommentaryManager.getStartMessage();
		score = new Random().nextInt(4);
	}

	@Override
	public void render(Graphics g, int displayWidth, int displayHeight) {
		g.setColor(Color.gray);

		// render pitch
		BufferedImage pitch = ImageManager.getImage("pitch");
		BufferedImage goal = ImageManager.getImage("goal");
		g.fillRect(offset-5, offset-5, pitch.getWidth() + 10, pitch.getHeight() + 10);
		g.drawImage(pitch, offset, offset, pitch.getWidth(), pitch.getHeight(), null);

		if(Configuration.DEBUGGING){ // pitch hitbox
			g.drawRect(20+offset, 50+offset, 472, 500);
		}
		// render goal
		g.drawImage(goal, offset, offset, goal.getWidth(), goal.getHeight(), null);
		if(Configuration.DEBUGGING){ // goal hitbox
			g.drawRect(196+offset, 10+offset, 118, 45);
		}

		// render players
		for (Player p : cpuTeam) {
			p.render(g, displayWidth, displayHeight, Color.red, offset);
		}

		for (Player p : playerTeam) {
			p.render(g, displayWidth, displayHeight, Color.blue, offset);
		}

		keeper.render(g, displayWidth, displayHeight, Color.green, offset);

		// render ball
		ball.render(g, displayWidth, displayHeight, offset);

		// render scoreboard
		g.setColor(Color.gray);
		g.fillRect(577, 25, 261, 50);
		g.fillRect(863, 25, 138, 50);

		g.setFont(ImageManager.getFont(44f));
		g.setColor(Color.blue);
		g.drawString(Configuration.PLAYER_ABBREVIATION, 580, 64);
		g.setColor(Color.white);
		String scores = (playerScored ? score + 1 : score) + ":" + score;
		g.drawString(scores, 670, 64);
		g.setColor(Color.red);
		g.drawString(Configuration.CPU_ABBREVIATION, 750, 64);

		g.setColor(Color.white);
		g.drawString("92:" + (50 + secondsElapsed), 870, 64);

		// render commentary
		g.setFont(ImageManager.getFont(16f));
		g.setColor(Color.gray);
		g.fillRect(577, 525, 422, 50);
		g.setColor(Color.white);
		String[] commentaryLines = commentary.split("%n%");
		for (int i = 0; i < commentaryLines.length; i++) {
			String c = commentaryLines[i];
			g.drawString(c, 582, 540 + i*20 + 5);
		}

		g.setFont(ImageManager.getFont(8f));
	}

	@Override
	public void update() {

		ball.update();
		playerScored = ball.isGoal();

		if(!gameOver && ball.isOut()){
			commentary = CommentaryManager.getBallOutMessage();
			gameOver = true;
		} else if(!gameOver && ball.isGoal()){
			commentary = CommentaryManager.getPlayerScoresMessage("FAKENAME");
			gameOver = true;
		}

		if(!finished && gameOver){
			finished = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// should never happen
						e.printStackTrace();
					}

					commentary = CommentaryManager.getFullTimeMessage((playerScored ? Configuration.PLAYER_NAME : Configuration.CPU_NAME));

					if(playerScored){
						win();
					} else {
						lose();
					}
				}

			}).start();
		}
	}

	private void lose() {
		// TODO Auto-generated method stub
		System.out.println("lose");
	}

	private void win() {
		// TODO Auto-generated method stub
		System.out.println("win");
	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int x, int y) {
		if(x > offset && x < offset + 512 && y > offset && y < offset + 550){
			ball.moveTo(x - offset, y - offset);
		}

	}

	public boolean getScenarioLoaded() {
		return scenarioLoaded;
	}

	public HashSet<Player> getCpuTeam() {
		return cpuTeam;
	}

	public HashSet<Player> getPlayerTeam() {
		return playerTeam;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

}
