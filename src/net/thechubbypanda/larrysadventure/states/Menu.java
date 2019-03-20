package net.thechubbypanda.larrysadventure.states;

import com.badlogic.gdx.Gdx;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.utils.Button;
import net.thechubbypanda.larrysadventure.utils.SoundButton;
import net.thechubbypanda.larrysadventure.utils.Utils;
import net.thechubbypanda.larrysadventure.utils.interfaces.ButtonListener;

public class Menu extends GameState {

	private Button play, exit, help;
	private SoundButton soundButton;
	private int wait = 20;

	public Menu(GameStateManager gsm) {
		super(gsm, StateType.menu);

		ButtonListener quit = new ButtonListener() {
			public void onClick() {
				Gdx.app.exit();
			}
		};

		ButtonListener helpClick = new ButtonListener() {
			public void onClick() {
				gsm.setState(GameState.HELP);
			}
		};

		ButtonListener start = new ButtonListener() {

			public void onClick() {
				gsm.setState(GameState.MAZE);
			}
		};

		ButtonListener music = new ButtonListener() {
			public void onClick() {
				sound = !sound;
			}
		};

		exit = new Button("Quit", font60, quit, color, hoverColor);
		play = new Button("Play", font60, start, color, hoverColor);
		help = new Button("How to Play", font60, helpClick, color, hoverColor);
		soundButton = new SoundButton(music);

		exit.clickable = false;
		play.clickable = false;
		help.clickable = false;
		soundButton.clickable = false;
	}

	public void update() {
		if (wait <= 0) {
			exit.clickable = true;
			play.clickable = true;
			help.clickable = true;
			soundButton.clickable = true;
		} else {
			wait--;
		}
		play.update(100, Gdx.graphics.getHeight() - 300);
		exit.update(100, Gdx.graphics.getHeight() - 450);
		help.update(100, Gdx.graphics.getHeight() - 375);
		soundButton.update();
	}

	public void render() {
		sb.setProjectionMatrix(hudCamera.combined);
		sb.begin();
		font120.setColor(color);
		font120.draw(sb, "Larry's Adventure", 100, Gdx.graphics.getHeight() - 100);
		font20.setColor(color);
		font20.draw(sb, "Music and sounds by Monplaisir & Rhodesmas", Gdx.graphics.getWidth() - Utils.getTextDimensions(font20, "Music and sounds by Monplaisir & Rhodesmas").x - 5, 45);
		font20.draw(sb, "Created by TheChubbyPanda", Gdx.graphics.getWidth() - Utils.getTextDimensions(font20, "Created by TheChubbyPanda").x - 5, 20);
		play.draw(sb);
		exit.draw(sb);
		help.draw(sb);
		soundButton.draw(sb);
		sb.end();
	}

	public void dispose() {
	}
}
