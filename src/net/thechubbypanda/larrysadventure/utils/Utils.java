package net.thechubbypanda.larrysadventure.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Utils extends Constants {

	/**
	 * Produces a random integer within a range, values are included
	 * 
	 * @param min Lower bound
	 * @param max Upper bound
	 * @return random integer
	 */
	public static int randomInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	// Gets the dimensions of drawable text
	public static Vector2i getTextDimensions(BitmapFont font, String text) {
		glyphLayout.setText(font, text);
		return new Vector2i((int) glyphLayout.width, (int) glyphLayout.height);
	}
}
