package net.thechubbypanda.larrysadventure.states;

import com.badlogic.gdx.Gdx;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.utils.Button;
import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class GameOver extends GameState {

	private Button menu, exit;
	private int finalScore;

	public GameOver(GameStateManager gsm) {
		super(gsm, StateType.menu);

		finalScore = Player.score;

		ButtonListener reset = new ButtonListener() {
			public void onClick() {
				gsm.reset();
			}
		};

		ButtonListener quit = new ButtonListener() {
			public void onClick() {
				Gdx.app.exit();
			}
		};

		menu = new Button("Menu", font60, reset, color, hoverColor);
		exit = new Button("Quit", font60, quit, color, hoverColor);
	}

	public void update() {
		menu.update(100, Gdx.graphics.getHeight() - 400);
		exit.update(100, Gdx.graphics.getHeight() - 475);
	}

	public void render() {
		sb.setProjectionMatrix(hudCamera.combined);
		sb.begin();
		font120.setColor(color);
		font120.draw(sb, "Game Over", 100, Gdx.graphics.getHeight() - 100);
		font60.setColor(color);
		font60.draw(sb, "Score: " + finalScore, 100, Gdx.graphics.getHeight() - 250);
		menu.draw(sb);
		exit.draw(sb);
		sb.end();
	}

	public void dispose() {
	}
}
