package com.samuelhindmarsh.ld27.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Goalkeeper;
import com.samuelhindmarsh.ld27.game.Player;
import com.samuelhindmarsh.ld27.game.StoppageTimeGame;
import com.samuelhindmarsh.ld27.instructions.Move;
import com.samuelhindmarsh.ld27.instructions.Pass;
import com.samuelhindmarsh.ld27.instructions.Shoot;
import com.samuelhindmarsh.ld27.instructions.Wait;
import com.samuelhindmarsh.ld27.managers.CommentaryManager;
import com.samuelhindmarsh.ld27.managers.ImageManager;
import com.samuelhindmarsh.ld27.managers.ScenarioManager;

public class GameState implements State {

	private StoppageTimeGame game;

	private int offset = 25;

	private boolean scenarioLoaded = false;

	private Button[] buttons = {
			new Button("play", 680, 150),
			new Button("clear", 880, 150),
			new Button("wait", 680, 450),
			new Button("move", 680, 300),
			new Button("pass", 880, 300),
			new Button("shoot", 880, 450)
	};

	private HashSet<Player> playerTeam;
	private HashSet<Player> cpuTeam;
	private Goalkeeper keeper;
	private Ball ball;
	private Player selectedPlayer;

	private boolean playerScored = false;
	private String commentary;
	private int score;
	private int secondsElapsed = 0;
	private long startMillis = 0;

	private boolean playing = false;
	private boolean gameOver = false;
	private boolean finished = false;
	private boolean done = false;

	private String activeMode;

	public GameState(StoppageTimeGame game, File scenario) {
		this.game = game;

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

		for(Player p : playerTeam){
			p.setBall(ball);
		}
		for(Player p : cpuTeam){
			p.setBall(ball);
		}
		keeper.setBall(ball);

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
			p.render(g, displayWidth, displayHeight, offset);

		}

		for (Player p : playerTeam) {
			p.render(g, displayWidth, displayHeight, offset);
		}

		keeper.render(g, displayWidth, displayHeight, offset);

		// render ball
		ball.render(g, displayWidth, displayHeight, offset);


		// render instruction buttons
		buttons[0].render(g, displayWidth, displayHeight);
		if(selectedPlayer != null){
			for(int i = 1; i < buttons.length; i++){
				buttons[i].render(g, displayWidth, displayHeight);
			}
		}

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
		if(secondsElapsed < 0){
			g.drawString("FT", 905, 64);
		} else {
			g.drawString("92:" + (50 + secondsElapsed), 870, 64);
		}

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

		checkButtons();

		if(playing && !finished){
			secondsElapsed = (int) ((System.currentTimeMillis() - startMillis) / 1000);

			if(secondsElapsed >= 10){
				gameOver = true;
				finished = true;
				done();
			}

			ball.update();
			playerScored = ball.isGoal();

			if(!gameOver && ball.isOut()){
				commentary = CommentaryManager.getBallOutMessage();
				gameOver = true;
			} else if(!gameOver && ball.isGoal()){
				Player scorer = ball.getLastKicked();
				if(scorer == null){
					scorer = ball.getPlayerWithBall();
				}
				commentary = CommentaryManager.getPlayerScoresMessage(scorer.getName());
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

						done();
					}

				}).start();
			}

			if(!gameOver){
				Player playerWithBall = ball.getPlayerWithBall();
				Player lastKicked = ball.getLastKicked();

				applyAi();

				// OK, now check which player is in possession
				for(Player p : playerTeam){
					checkForPossession(p);
					p.update();
				}

				// check if CPU tackles
				for(Player p : cpuTeam){
					if(checkForPossession(p)){
						cpuHasBall(p, playerWithBall, lastKicked, false);
					}
					p.update();
				}

				// check if keeper saves
				if(checkForPossession(keeper)){
					cpuHasBall(keeper, playerWithBall, lastKicked, true);
				}
				keeper.update();
			}
		}
	}

	private void applyAi() {
		// first, clear existing movements
		for(Player p : cpuTeam){
			p.setMoving(false);
		}

		if(ball.getPlayerWithBall() == null){
			keeper.moveTo(Math.min(304, Math.max(206, ball.getToX())), keeper.getY(), 1.4);
		} else {
			keeper.moveTo(Math.min(304, Math.max(206, ball.getX())), keeper.getY(), 1.4);
		}

		// determine free players
		Set<Player> freeTacklers = new HashSet<Player>(cpuTeam);

		// find the closest player to ball and move to it
		Player closestToBall = null;
		for(Player p : cpuTeam){
			if(closestToBall == null || p.distTo(ball) < closestToBall.distTo(ball)){
				closestToBall = p;
			}
		}

		closestToBall.moveTo(ball.getX(), ball.getY(), 0.9);
		freeTacklers.remove(closestToBall);

		// mark all human players not in possession
		for(Player p : playerTeam){
			if(!p.hasPossession()){
				// find closest CPU player to mark this player
				Player closestToPlayer = null;
				for(Player cpu : freeTacklers){
					if(closestToPlayer == null || cpu.distTo(p) < closestToPlayer.distTo(p)){
						closestToPlayer = cpu;
					}
				}

				if(closestToPlayer != null){ // could be null if not enough CPU players to mark the human players
					closestToPlayer.moveTo(p.getX() - 3, p.getY() - 3, 0.9);
					freeTacklers.remove(closestToPlayer);
				}
			}
		}

		//		if(!moving){
		//			// we are not tracking down a loose ball, so mark the closest player
		//			for(Player p : playerTeam){
		//				if(!p.isMarked){
		//					// this player
		//					for(Player p : cpuTeam){
		//						closest &= distToBall <= p.distToBall();
		//						if(!closest){
		//							break;
		//						}
		//					}
		//				}
		//			}
		//		}
	}

	private void checkButtons() {
		activeMode = "";

		if(!playing && buttons[0].isActive()){
			// playing
			playing = true;
			startMillis = System.currentTimeMillis();
			for(Player p : playerTeam){
				p.setSelected(false);
				selectedPlayer = null;
			}
		} else if(buttons[1].isActive()){
			// clear
			if(selectedPlayer != null){
				selectedPlayer.getInstructions().clear();
			}
			buttons[1].setActive(false);
		} else if(buttons[2].isActive()){
			int res = -1;
			do{
				try{
					String ans = JOptionPane.showInputDialog(null, "How many seconds to wait for?", 1);
					if(ans == null || ans.trim().length() < 0 || "".equals(ans.trim())){
						break;
					}
					res = Integer.parseInt(ans.trim());
				} catch(NumberFormatException e){
					// doesn't matter
				}
			} while(res < 0);
			selectedPlayer.queue(new Wait(res));
			buttons[2].setActive(false);
		}

		for(int i = 3; i < buttons.length; i++ ){
			if(buttons[i].isActive()){
				activeMode = buttons[i].getName();
			}
		}
	}

	private void cpuHasBall(Player p, Player playerWithBall, Player lastKicked, boolean keeper){
		if(keeper){
			commentary = CommentaryManager.getKeeperSavesMessage(p.getName());
		} else if(playerWithBall == null){
			// intercepted a loose ball
			commentary = CommentaryManager.getCpuInterceptsMessage(p.getName(), lastKicked.getName());
		} else {
			commentary = CommentaryManager.getCpuTacklesMessage(p.getName(), playerWithBall.getName());
		}
		gameOver = true;
	}

	private boolean checkForPossession(Player p){
		if(ball.getPlayerWithBall() != p && ball.getLastKicked() != p && p.intersects(ball)){
			ball.setPlayerWithBall(p);
			ball.setLastKicked(null);
			p.setHasPossession(true);
			return true;
		}
		return false;
	}

	private void done() {
		commentary = CommentaryManager.getFullTimeMessage((playerScored ? Configuration.PLAYER_NAME : Configuration.CPU_NAME));
		secondsElapsed = -1;
		done = true;
	}

	@Override
	public void mouseMoved(int x, int y) {
		for(Button b : buttons){
			b.setHovering(b.intersects(x, y));
		}
	}

	@Override
	public void mouseClicked(int x, int y, boolean rightClick) {
		if(Configuration.DEBUGGING){
			System.out.println((x-offset)+", " + (y-offset));
		}

		if(done){
			if(playerScored){
				game.reset();
			} else {
				this.reset();
			}
		}

		// clear everything first
		if(!rightClick && (x > offset && x < offset + 512 && y > offset && y < offset + 550)){
			if(!playing && "".equals(activeMode)){
				for(Player p : playerTeam){
					p.setSelected(false);
				}
				selectedPlayer = null;
			} else if("move".equals(activeMode)){
				selectedPlayer.queue(new Move(x-offset, y-offset));
			} else if("pass".equals(activeMode)){
				selectedPlayer.queue(new Pass(x-offset, y-offset));
			} else if("shoot".equals(activeMode)){
				selectedPlayer.queue(new Shoot(x-offset, y-offset));
			}
		} else {
			for(Button b : buttons){
				b.setActive(playing && b.getName().equals("play"));
			}
		}

		if(!playing){
			if(!rightClick && (x > offset && x < offset + 512 && y > offset && y < offset + 550)){
				if("".equals(activeMode)){
					for(Player p : playerTeam){
						if(p.clickedOn(x-offset, y-offset)){
							p.setSelected(true);
							selectedPlayer = p;
						}
					}
				}
			} else if(!rightClick){
				for(Button b : buttons){
					if(b.intersects(x, y)){
						b.setActive(true);
					}
				}
			}
		}
	}


	public void reset() {
		for (Player p : playerTeam) {
			p.reset();
		}
		for (Player p : cpuTeam) {
			p.reset();
		}
		keeper.reset();

		ball.reset();

		for (Button b : buttons) {
			b.setActive(false);
		}

		this.activeMode = "";
		this.commentary = CommentaryManager.getStartMessage();
		this.done = false;
		this.finished = false;
		this.gameOver = false;
		this.playerScored = false;
		this.playing = false;
		this.secondsElapsed = 0;
		this.selectedPlayer = null;
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
