package net.thechubbypanda.larrysadventure.states;

import com.badlogic.gdx.Gdx;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.utils.Button;
import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class Help extends GameState {

	private Button back;
	private int wait = 20;

	public Help(GameStateManager gsm) {
		super(gsm, StateType.menu);

		ButtonListener backClick = new ButtonListener() {
			public void onClick() {
				gsm.setState(GameState.MENU);
			}
		};

		back = new Button("Back", font60, backClick, color, hoverColor);
		back.clickable = false;
	}

	public void update() {
		if (wait <= 0) {
			back.clickable = true;
		} else {
			wait--;
		}
		back.update(100, Gdx.graphics.getHeight() - 740);
	}

	public void render() {
		sb.setProjectionMatrix(hudCamera.combined);
		sb.begin();
		font120.setColor(color);
		font120.draw(sb, "How To Play", 100, Gdx.graphics.getHeight() - 100);
		font40.setColor(color);
		font40.draw(sb, "WASD to move.", 100, Gdx.graphics.getHeight() - 300);
		font40.draw(sb, "Point the mouse to aim.", 100, Gdx.graphics.getHeight() - 350);
		font40.draw(sb, "Left click to shoot.", 100, Gdx.graphics.getHeight() - 400);
		font40.draw(sb, "Find 2 keys per level.", 100, Gdx.graphics.getHeight() - 450);
		font40.draw(sb, "Keys are found in spawners.", 100, Gdx.graphics.getHeight() - 500);
		font40.draw(sb, "Get to the teleporter!", 100, Gdx.graphics.getHeight() - 550);
		back.draw(sb);
		sb.end();
	}

	public void dispose() {
	}
}
