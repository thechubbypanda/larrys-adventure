package net.thechubbypanda.larrysadventure.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class SoundButton extends Constants {

	private static final Texture on = new Texture("soundOn.png");
	private static final Texture off = new Texture("soundOff.png");

	private int x = 0, y = 0, width = 50, height = 50;
	private ButtonListener listener;

	public boolean clickable = true;

	private long lastClick;

	public SoundButton(ButtonListener listener) {
		this.listener = listener;
		lastClick = System.currentTimeMillis();
	}

	public void update() {
		x = Gdx.graphics.getWidth() - width - 20;
		y = Gdx.graphics.getHeight() - height - 20;

		if (clickable) {
			if (true) ;
		}

		int mX = Gdx.input.getX();
		int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

		if (lastClick + 200 < System.currentTimeMillis()) {
			if (Gdx.input.isTouched() && clickable) {
				if (mX > x && mX < width + x) {
					if (mY > y && mY < height + y) {
						listener.onClick();
						lastClick = System.currentTimeMillis();
					}
				}
			}
		}
	}

	public void draw(SpriteBatch sb) {
		if (sound) {
			sb.draw(on, x, y, width, height);
		} else {
			sb.draw(off, x, y, width, height);
		}
	}
}
