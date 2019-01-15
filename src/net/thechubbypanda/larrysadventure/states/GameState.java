package net.thechubbypanda.larrysadventure.states;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.GameStateManager;

public abstract class GameState extends GameComponent {

	// State flags
	public static final short MENU = 0;
	public static final short PAUSE = 1;
	public static final short GAMEOVER = 2;
	public static final short MAZE = 3;
	public static final short HELP = 4;

	public static enum StateType {
		game, menu;
	}

	protected GameStateManager gsm;
	public StateType type;

	protected GameState(GameStateManager gsm, StateType type) {
		this.gsm = gsm;
		this.type = type;
	}
}
