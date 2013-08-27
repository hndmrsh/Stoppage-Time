package com.samuelhindmarsh.ld27.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.samuelhindmarsh.ld27.Configuration;
import com.samuelhindmarsh.ld27.managers.ImageManager;

public interface State {

	public void render(Graphics g, int displayWidth, int displayHeight);

	public void update();

	public void mouseMoved(int x, int y);

	public void mouseClicked(int x, int y, boolean rightClick);

	public class Button {
		private String name;
		private int x, y;
		private int width, height;
		private boolean hovering;
		private boolean active;

		public Button(String name, int x, int y) {
			this.name = name;

			BufferedImage img = ImageManager.getImage(name);
			width = img.getWidth();
			height = img.getHeight();

			this.x = x - (width/2);
			this.y = y - (height/2);


		}

		public boolean intersects(int x, int y){
			return (x > this.x && x <= this.x + width) && (y > this.y && y <= this.y + height);
		}

		public void render(Graphics g, int displayWidth, int displayHeight){
			BufferedImage img = null;
			if(hovering){
				img = ImageManager.getImage(name + "-hover");
			} else if(active){
				img = ImageManager.getImage(name + "-active");
			} else {
				img = ImageManager.getImage(name);
			}

			g.drawImage(img, x, y, width, height, null);
			if(Configuration.DEBUGGING){
				g.setColor(Color.green);
				g.drawRect(x, y, width, height);
			}
		}

		public void setHovering(boolean hovering) {
			this.hovering = hovering;
		}


		public void setActive(boolean active) {
			this.active = active;
		}

		public boolean isActive() {
			return active;
		}

		public String getName() {
			return name;
		}
	}
}
