package com.samuelhindmarsh.ld27.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

import com.samuelhindmarsh.ld27.game.Goalkeeper;
import com.samuelhindmarsh.ld27.game.Player;

public class ScenarioManager {

	public static boolean parseScenario(File scenario, Set<Player> playerTeam, Set<Player> cpuTeam) throws FileNotFoundException {
		boolean hasGoalkeeper = false, hasCpu = false, hasPlayer = false;
		
		Scanner s = new Scanner(scenario);
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.length() > 0 && !line.startsWith("#")){
				String[] tokens = line.split(",");
				
				if("keeper".equalsIgnoreCase(tokens[0])){
					cpuTeam.add(parsePlayer(tokens, true));
					hasGoalkeeper = true;
				} else if("player".equalsIgnoreCase(tokens[0])){
					playerTeam.add(parsePlayer(tokens, false));
					hasPlayer = true;
				} else if("cpu".equalsIgnoreCase(tokens[0])){
					cpuTeam.add(parsePlayer(tokens, false));
					hasCpu = true;
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
		
		if(error){
			JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return !error;
	}
	
	
	private static Player parsePlayer(String[] tokens, boolean isKeeper){
		int number = Integer.parseInt(tokens[1].trim());
		String name = tokens[2].trim();
		int x = Integer.parseInt(tokens[3].trim());
		int y = Integer.parseInt(tokens[4].trim());
		
		if(isKeeper){
			return new Goalkeeper(x, y, number, name);
		} else {
			return new Player(x, y, number, name);
		}
		
	}
	
}
