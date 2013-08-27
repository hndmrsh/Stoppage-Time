package com.samuelhindmarsh.ld27.managers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageManager {

	private static Font font;

	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	private static HashMap<Float, Font> fonts = new HashMap<Float, Font>();

	public static BufferedImage getImage(String imageName){
		BufferedImage cachedImg = images.get(imageName);
		if(images.containsKey(imageName)){
			return cachedImg;
		}

		String fileName = "img" + File.separator + imageName + ".png";
		File f = new File(fileName);

		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.err.println("Couldn't read image with name: " + fileName);
			e.printStackTrace();
		}

		images.put(imageName, img);
		return img;
	}

	public static Font getFont(float size){
		if(font == null){
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, new File("QUARTZMS.TTF"));
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
		}

		if(font != null && !fonts.containsKey(size)){
			Font scaledFont = font.deriveFont(size);
			fonts.put(size, scaledFont);
		}

		return fonts.get(size);
	}

}
