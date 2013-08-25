package com.samuelhindmarsh.ld27.managers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.samuelhindmarsh.ld27.game.StoppageTimeGame;

public class InputManager implements MouseListener, MouseMotionListener, KeyListener {

	private StoppageTimeGame game;
	
	public InputManager(StoppageTimeGame game) {
		this.game = game;
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			game.exit();
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		game.mouseClicked(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		game.mouseMoved(e.getX(), e.getY());
	}
	
	
	
	
	
	/*
	 * UNUSED METHODS
	 */
	
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}


	@Override
	public void mouseDragged(MouseEvent arg0) {}


	
	

}
