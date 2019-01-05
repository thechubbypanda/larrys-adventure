package net.thechubbypanda.larrysadventure.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.utils.Button;
import net.thechubbypanda.larrysadventure.utils.SoundButton;
import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class Pause extends GameState {

	private static final Texture darken = new Texture("darken.png");

	private Button resume, exit;
	private SoundButton soundButton;

	public Pause(GameStateManager gsm) {
		super(gsm, StateType.menu);

		ButtonListener pause = new ButtonListener() {
			public void onClick() {
				gsm.togglePause();
			}
		};

		ButtonListener quit = new ButtonListener() {
			public void onClick() {
				Gdx.app.exit();
			}
		};

		ButtonListener music = new ButtonListener() {
			public void onClick() {
				sound = !sound;
			}
		};

		resume = new Button("Resume Game", font60, pause, color, hoverColor);
		exit = new Button("Quit", font60, quit, color, hoverColor);
		soundButton = new SoundButton(music);
	}

	public void update() {
		resume.update(100, Gdx.graphics.getHeight() - 300);
		exit.update(100, Gdx.graphics.getHeight() - 375);
		soundButton.update();
	}

	public void render() {
		sb.setProjectionMatrix(hudCamera.combined);
		sb.begin();
		sb.draw(darken, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		font120.setColor(color);
		font120.draw(sb, "Paused", 100, Gdx.graphics.getHeight() - 100);
		resume.draw(sb);
		exit.draw(sb);
		soundButton.draw(sb);
		sb.end();
	}

	public void dispose() {
	}
}
