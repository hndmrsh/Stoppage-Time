package com.samuelhindmarsh.ld27.managers;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.samuelhindmarsh.ld27.game.Ball;
import com.samuelhindmarsh.ld27.game.Goalkeeper;
import com.samuelhindmarsh.ld27.game.Player;
import com.samuelhindmarsh.ld27.states.GameState;

public class ScenarioManager {

	public static boolean parseScenario(File scenario, GameState state) throws FileNotFoundException {
		boolean hasGoalkeeper = false, hasCpu = false, hasPlayer = false, hasBall = false;

		Scanner s = new Scanner(scenario);
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.length() > 0 && !line.startsWith("#")){
				String[] tokens = line.split(",");

				if("keeper".equalsIgnoreCase(tokens[0])){
					state.getCpuTeam().add(parsePlayer(tokens, Color.green, true));
					hasGoalkeeper = true;
				} else if("player".equalsIgnoreCase(tokens[0])){
					state.getPlayerTeam().add(parsePlayer(tokens, Color.blue, false));
					hasPlayer = true;
				} else if("cpu".equalsIgnoreCase(tokens[0])){
					state.getCpuTeam().add(parsePlayer(tokens, Color.red, false));
					hasCpu = true;
				} else if("ball".equalsIgnoreCase(tokens[0])){
					state.setBall(parseBall(tokens));
					hasBall = true;
				}
			}
		}
		s.close();

		boolean error = false;
		String errorMsg = "The following errors occurred:\n";
		if(!hasGoalkeeper){
			error = true;
			errorMsg += "\n* No goalkeeper defined for CPU team.";
		}
		if(!hasCpu){
			error = true;
			errorMsg += "\n* No players defined for CPU team.";
		}
		if(!hasPlayer){
			error = true;
			errorMsg += "\n* No players defined for human team.";
		}
		if(!hasBall){
			error = true;
			errorMsg += "\n* No ball defined.";
		}

		if(error){
			JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
		}

		return !error;
	}


	private static Ball parseBall(String[] tokens) {
		int x = Integer.parseInt(tokens[1].trim());
		int y = Integer.parseInt(tokens[2].trim());
		return new Ball(x, y);
	}


	private static Player parsePlayer(String[] tokens, Color colour, boolean isKeeper){
		int number = Integer.parseInt(tokens[1].trim());
		String name = tokens[2].trim();
		int x = Integer.parseInt(tokens[3].trim());
		int y = Integer.parseInt(tokens[4].trim());

		if(isKeeper){
			return new Goalkeeper(x, y, number, name, colour);
		} else {
			return new Player(x, y, number, name, colour);
		}

	}

}
