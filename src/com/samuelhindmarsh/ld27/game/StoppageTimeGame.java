package com.samuelhindmarsh.ld27.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.managers.ImageManager;
import com.samuelhindmarsh.ld27.states.GameState;
import com.samuelhindmarsh.ld27.states.MenuState;
import com.samuelhindmarsh.ld27.states.State;

public class StoppageTimeGame {

	private State state;
	
	public StoppageTimeGame() {		
		state = new MenuState(this);
	}
	
	public void render(Graphics g, int displayWidth, int displayHeight){
		g.drawImage(ImageManager.getImage("bg"), 0, 0, displayWidth, displayHeight, null);
		state.render(g, displayWidth, displayHeight);
	}
	
	public void update(){
		state.update();
	}
	
	public void newGame(){
		JFileChooser fileChooser = new JFileChooser("scenarios");
		fileChooser.setDialogTitle("Choose a scenario to play");
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Scenario files (" + Configuration.SCENARIO_FILE_EXTENSION + ")";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(Configuration.SCENARIO_FILE_EXTENSION);
			}
		});
		int result = fileChooser.showDialog(null, "Play");
		if(result == JFileChooser.APPROVE_OPTION){
			state = new GameState(fileChooser.getSelectedFile());
		} else if (result == JFileChooser.ERROR_OPTION){
			JOptionPane.showMessageDialog(null, "There was an error loading this scenario", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void exit(){
		System.exit(0);
	}
	
	public void mouseMoved(int x, int y){
		state.mouseMoved(x, y);
	}

	public void mouseClicked(int x, int y) {
		state.mouseClicked(x, y);
	}
}
