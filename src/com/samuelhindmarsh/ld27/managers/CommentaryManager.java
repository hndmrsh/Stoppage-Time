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
		"%player% scores! " + Configuration.PLAYER_NAME + " will surely win the league!",
		"Great goal by %player% for " + Configuration.PLAYER_NAME + "!"
	};
	
	private static String[] successfulPass = {
		"Good pass by %from%.",
		"The ball "
	};
	
	private static String[] cpuIntercepts = {
		"%cpu% clears the ball, and " + Configuration.CPU_NAME + " have surely won the league.",
		"The ball is intercepted, and that's surely it for " + Configuration.PLAYER_NAME,
		"Bad pass from %player%, and %cpu% clears"
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
	
	public static String getCpuInterceptsMessage(String fromPlayer, String cpuPlayer){
		String msg = cpuIntercepts[new Random().nextInt(cpuIntercepts.length)];
		return msg.replace("%player%", fromPlayer).replace("%cpu%", cpuPlayer);
	}
	
	public static String getFullTimeMessage(String fromPlayer, String cpuPlayer){
		return fullTime[new Random().nextInt(fullTime.length)];
	}
}
