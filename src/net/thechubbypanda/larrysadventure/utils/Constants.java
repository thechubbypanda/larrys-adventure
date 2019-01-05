package net.thechubbypanda.larrysadventure.utils;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Constants {

	public static final String TITLE = "Larry's Adventure";

	public static final float PPM = 100;

	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;

	public static SpriteBatch sb;

	public static final OrthographicCamera camera = new OrthographicCamera();
	public static final OrthographicCamera hudCamera = new OrthographicCamera();

	public static final Viewport viewport = new FillViewport(WIDTH, HEIGHT, camera);
	public static final Viewport hudViewport = new ScreenViewport(hudCamera);

	public static Box2DDebugRenderer debugRenderer;

	public static final Color color = new Color(1, 1, 1, 1);
	public static final Color hoverColor = new Color(0.8f, 0, 0, 1);

	public static final Random random = new Random();

	public static BitmapFont font120, font60, font40, font20;

	public static final GlyphLayout glyphLayout = new GlyphLayout();

	public static Cursor crosshair;

	public static boolean sound = true;
}
