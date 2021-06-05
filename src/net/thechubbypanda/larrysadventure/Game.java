package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import net.thechubbypanda.larrysadventure.states.GameState;
import net.thechubbypanda.larrysadventure.utils.Constants;

public class Game extends Constants implements ApplicationListener {

	private GameStateManager gsm;

	// Called by framework once on application start
	public void create() {

		// Set constants that cannot be set while in static context
		sb = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("mavenPro.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 120;
		font120 = generator.generateFont(parameter);
		parameter.size = 60;
		font60 = generator.generateFont(parameter);
		parameter.size = 40;
		font40 = generator.generateFont(parameter);
		parameter.size = 20;
		font20 = generator.generateFont(parameter);
		generator.dispose();
		crosshair = Gdx.graphics.newCursor(new Pixmap(Gdx.files.local("crosshair.png")), 8, 8);

		// Set camera zoom
		camera.zoom = 0.3f;

		// Initialize game state manager with starting state of menu
		gsm = new GameStateManager(GameState.MENU);
	}

	// Called every 1/60th of a second by the framework if the computer is capable
	public void render() {

		// Update the game state manager
		gsm.update();

		// Clear the screen
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render the game state manager
		gsm.render();
		// System.out.println(Gdx.graphics.getFramesPerSecond());
	}

	// Stops the image squeezing or otherwise contorting itself
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport.apply();
		hudViewport.update(width, height);
		hudViewport.apply(true);
	}

	public void pause() {

	}

	public void resume() {

	}

	public void dispose() {
		sb.dispose();
		gsm.dispose();
	}

	// Application entry point, sets up the framework application
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setMaximized(true);
		config.setTitle(TITLE);
		config.setWindowPosition(-1, -1);
		config.setWindowIcon("icon.png");

		new Lwjgl3Application(new Game(), config);
	}
}
