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
import com.samuelhindmarsh.ld27.game.Goalkeeper;
import com.samuelhindmarsh.ld27.game.Player;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;
import com.samuelhindmarsh.ld27.managers.CommentaryManager;
import com.samuelhindmarsh.ld27.managers.ImageManager;
import com.samuelhindmarsh.ld27.managers.ScenarioManager;

public class GameState implements State {

	private boolean scenarioLoaded = false;
	
	private HashSet<Player> playerTeam;
	private HashSet<Player> cpuTeam;
	private Goalkeeper keeper;
	
	private boolean playerScored = false;
	private String commentary;
	private int score;
	
	public GameState(StoppageTimeGame game, File scenario) {
		playerTeam = new HashSet<Player>();
		cpuTeam = new HashSet<Player>();
	
		boolean success = false;
		try {
			success = ScenarioManager.parseScenario(scenario, playerTeam, cpuTeam);
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
		int offset = 25;
		
		g.setColor(Color.gray);
		
		// render pitch
		BufferedImage pitch = ImageManager.getImage("pitch");
		g.fillRect(offset-5, offset-5, pitch.getWidth() + 10, pitch.getHeight() + 10);
		g.drawImage(pitch, offset, offset, pitch.getWidth(), pitch.getHeight(), null);
		
		// render players
		for (Player p : cpuTeam) {
			g.setColor(Color.red);
			g.fillOval(offset + p.getX() - (Configuration.PLAYER_SIZE / 2), offset + p.getY() - (Configuration.PLAYER_SIZE / 2), 
					Configuration.PLAYER_SIZE, Configuration.PLAYER_SIZE);
			g.setColor(Color.white);
			g.setFont(ImageManager.getFont(10f));
			g.drawString(""+p.getNumber(), offset + p.getX() - Configuration.PLAYER_SIZE / 3, offset + p.getY());
			g.setFont(ImageManager.getFont(8f));
			g.drawString(""+p.getName(), offset + p.getX() - Configuration.PLAYER_SIZE, offset + p.getY() - Configuration.PLAYER_SIZE / 2);
		}
		
		for (Player p : playerTeam) {
			g.setColor(Color.blue);
			g.fillOval(offset + p.getX() - (Configuration.PLAYER_SIZE / 2), offset + p.getY() - (Configuration.PLAYER_SIZE / 2), 
					Configuration.PLAYER_SIZE, Configuration.PLAYER_SIZE);
			g.setColor(Color.white);
			g.setFont(ImageManager.getFont(10f));
			g.drawString(""+p.getNumber(), offset + p.getX() - Configuration.PLAYER_SIZE / 3, offset + p.getY());
			g.setFont(ImageManager.getFont(8f));
			g.drawString(""+p.getName(), offset + p.getX() - Configuration.PLAYER_SIZE, offset + p.getY() - Configuration.PLAYER_SIZE / 2);
		}
		
		g.setColor(Color.black);
		g.fillOval(offset + keeper.getX() - (Configuration.PLAYER_SIZE / 2), offset + keeper.getY() - (Configuration.PLAYER_SIZE / 2), 
				Configuration.PLAYER_SIZE, Configuration.PLAYER_SIZE);
		g.setColor(Color.white);
		g.setFont(ImageManager.getFont(10f));
		g.drawString(""+keeper.getNumber(), offset + keeper.getX() - Configuration.PLAYER_SIZE / 3, offset + keeper.getY());
		g.setFont(ImageManager.getFont(8f));
		g.drawString(""+keeper.getName(), offset + keeper.getX() - Configuration.PLAYER_SIZE, offset + keeper.getY() - Configuration.PLAYER_SIZE / 2);
		
		// render scoreboard
		g.setColor(Color.gray);
		g.fillRect(577, 25, 422, 50);
		g.setColor(Color.white);
		g.setFont(ImageManager.getFont(52f));
		String scores = Configuration.PLAYER_ABBREVIATION + " " + (playerScored ? score + 1 : score) + " : " + score + " " + Configuration.CPU_ABBREVIATION;
		g.drawString(scores, 620, 68);
		
		// render commentary
		g.setFont(ImageManager.getFont(16f));
		g.setColor(Color.gray);
		g.fillRect(577, 525, 422, 50);
		g.setColor(Color.white);
		String[] commentaryLines = commentary.split("%n%");
		for (int i = 0; i < commentaryLines.length; i++) {
			String c = commentaryLines[i];
			g.drawString(c, 582, 540 + i*20);
		}
				
		g.setFont(ImageManager.getFont(8f));
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int x, int y) {
		// TODO Auto-generated method stub

	}
	
	public boolean getScenarioLoaded() {
		return scenarioLoaded;
	}

}
