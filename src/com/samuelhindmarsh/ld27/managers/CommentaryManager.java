package com.samuelhindmarsh.ld27.managers;

import java.util.Random;

import com.samuelhindmarsh.ld27.Configuration;

public class CommentaryManager {

	private static String[] startMessages = {
		"Only seconds remain, " + Configuration.PLAYER_NAME + " must score%n%soon if they want to win the league.",
		"These two teams are tied in the last seconds of%n%the final game of the season.",
		"Nearly full time, it looks like " + Configuration.PLAYER_NAME + "%n%are going to fall at the last hurdle.",
		"The referee looks at his watch. Can%n%" + Configuration.CPU_NAME + " hang on to win the league?",
	};

	private static String[] playerScores = {
		"%player% scores! " + Configuration.PLAYER_NAME + " will surely%n%win the league!",
		"Great goal by %player% for " + Configuration.PLAYER_NAME + "!"
	};

	private static String[] successfulPass = {
		"Good pass by %from%.",
		"The ball "
	};

	private static String[] cpuIntercepts = {
		"%cpu% clears the ball, and " + Configuration.CPU_NAME + "%n%have surely won the league.",
		"The ball is intercepted, and that's surely%n%it for " + Configuration.PLAYER_NAME,
		"That's a bad pass, and %cpu% will clear"
	};
	
	private static String[] keeperSaves = {
		"%cpu% collects the ball. " + Configuration.CPU_NAME + " have%n%surely won it now!",
		"Great save by %cpu% for " + Configuration.CPU_NAME
	};
	
	private static String[] cpuTackles = {
		"Great tackle by %cpu% on %player%,%n%and that's surely the game.",
		"That's a big tackle by %cpu%. " + Configuration.CPU_NAME + "%n%must hold on now!"
	};

	private static String[] ballOut = {
		"The ball goes out of play.",
		"That could be the last chance for " + Configuration.PLAYER_NAME + "."
	};

	private static String[] fullTime = {
		"The referee blows the whistle for full time!",
		"That's it! %team% have won the league!",
		"It's all over here at " + Configuration.STADIUM_NAME + "!"
	};



	public static String getStartMessage(){
		return startMessages[new Random().nextInt(startMessages.length)];
	}

	public static String getPlayerScoresMessage(String playerName){
		String msg = playerScores[new Random().nextInt(playerScores.length)];
		return msg.replace("%player%", playerName);
	}

	public static String getSuccessfulPassMessage(String fromPlayer, String toPlayer){
		String msg = successfulPass[new Random().nextInt(successfulPass.length)];
		return msg.replace("%from%", fromPlayer).replace("%to%", toPlayer);
	}

	public static String getCpuInterceptsMessage(String cpuPlayer, String lastKicked){
		String msg = cpuIntercepts[new Random().nextInt(cpuIntercepts.length)];
		return msg.replace("%cpu%", cpuPlayer).replace("%player%", lastKicked);
	}
	
	public static String getKeeperSavesMessage(String keeper){
		String msg = keeperSaves[new Random().nextInt(keeperSaves.length)];
		return msg.replace("%cpu%", keeper);
	}

	public static String getFullTimeMessage(String winningTeam){
		String msg = fullTime[new Random().nextInt(fullTime.length)];
		return msg.replace("%team%", winningTeam);
	}

	public static String getBallOutMessage(){
		return ballOut[new Random().nextInt(ballOut.length)];
	}

	public static String getCpuTacklesMessage(String name, String playerWithBall) {
		String msg = cpuTackles[new Random().nextInt(cpuTackles.length)];
		return msg.replace("%player%", playerWithBall).replace("%cpu%", name);
	}
}
