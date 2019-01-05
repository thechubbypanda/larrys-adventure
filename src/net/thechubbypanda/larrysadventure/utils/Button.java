package net.thechubbypanda.larrysadventure.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class Button extends Utils {

	private int x = 0, y = 0, width, height;
	private String text;
	private BitmapFont font;
	private ButtonListener listener;
	private Color color, hoverColor;
	private boolean hovering = false;

	public boolean clickable = true;

	public Button(String text, BitmapFont font, ButtonListener listener, Color color, Color hoverColor) {
		this.text = text;
		this.font = font;
		this.listener = listener;
		this.color = color;
		this.hoverColor = hoverColor;

		Vector2i temp = getTextDimensions(font, text);
		width = temp.x;
		height = temp.y;
	}

	public void update(int x, int y) {
		this.x = x;
		this.y = y;

		int mX = Gdx.input.getX();
		int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

		if (mX > x && mX < width + x) {
			if (mY > y && mY < height + y) {
				if (Gdx.input.isTouched()) {
					if (clickable) {
						listener.onClick();
					}
				}
				hovering = true;
			} else {
				hovering = false;
			}
		} else {
			hovering = false;
		}
	}

	public void draw(Batch batch) {
		if (!hovering) {
			font.setColor(color);
		} else {
			font.setColor(hoverColor);
		}
		font.draw(batch, text, x, y + height);
	}
}
