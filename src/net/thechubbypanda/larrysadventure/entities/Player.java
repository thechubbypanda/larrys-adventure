package net.thechubbypanda.larrysadventure.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.items.guns.Gun;
import net.thechubbypanda.larrysadventure.items.guns.Pistol;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.misc.Inventory;
import net.thechubbypanda.larrysadventure.utils.AnimationController;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Player extends Entity implements InputProcessor {

	// The textures of the player (what is drawn to the screen)
	private static final Texture larryLeft = new Texture("entities/larry/left.png");
	private static final Texture larryRight = new Texture("entities/larry/right.png");

	// The maximum health value of the player
	public static final int maxHealth = 150;

	// The animations for the player and how long each frame lasts
	private static Animation<TextureRegion> left, right;
	private static float frameDuration = 0.09f;

	// The acceleration to apply to the player when it needs to move
	private static final float acceleration = 14f;

	// The current health of the player
	public static int health;

	// The current score of the player
	public static int score;

	// The player's inventory
	public static Inventory inventory = new Inventory();

	// > 0 if the player has recently been hit
	public static int justHit;

	// Sets the player back to full health, clears the inventory and sets the score
	// to 0
	public static void reset() {
		inventory = new Inventory();
		inventory.add(new Pistol());
		health = 0;
		score = 0;
		justHit = 0;
	}

	// Makes sure the player is in the correct state before play begins
	static {
		reset();
	}

	// The last animation played
	private Animation<TextureRegion> lastAnimation;

	// What frame the current animation is on
	private TextureRegion currentFrame;

	// Arbitrary animation value
	private float timeKeeper = 10000;

	// Reference to the current entity handler
	private EntityHandler entityHandler;

	// Reference to the Box2D world (for creating more entities)
	private World world;

	// The map of the current level
	private Cell[][] cellMap;

	// The players's starting position
	private Vector2i startPos;

	// The last cell that the player was on
	public volatile Cell lastCell;

	public Player(EntityHandler entityHandler, World world, Vector2i pos) {
		super(world, pos, new Vector2());

		this.entityHandler = entityHandler;
		this.world = world;

		// Sets this class to manage all the input from the framework
		Gdx.input.setInputProcessor(this);

		// Loading animations
		left = AnimationController.createAnimation(larryLeft, 3, 1, frameDuration);
		right = AnimationController.createAnimation(larryRight, 3, 1, frameDuration);

		currentFrame = right.getKeyFrame(0);
		lastAnimation = right;

		health = maxHealth;
		justHit = 0;

		startPos = pos;
	}

	public void setMap(Cell[][] cellMap) {
		this.cellMap = cellMap;
	}

	// Moves the player to the starting position
	public void setToStart() {
		body.setTransform(startPos.x / PPM, startPos.y / PPM, 0);
	}

	public void update() {
		inventory.update();
		applyInput();
		super.update();
		updateAnimation();
		if (justHit > 0) {
			justHit--;
		}
		lastCell = getClosestCell(cellMap);
	}

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(currentFrame, pos.x - 16, pos.y - 16, 32, 32);
		sb.end();
		inventory.render();
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 20f;

		Body b = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(15 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 5f;
		fixtureDef.friction = 0;
		b.createFixture(fixtureDef);

		return b;
	}

	// Checks if movement keys are pressed and applies forces accordingly
	private void applyInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
			body.applyForceToCenter(new Vector2(0, acceleration), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.applyForceToCenter(new Vector2(-acceleration, 0), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			body.applyForceToCenter(new Vector2(0, -acceleration), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.applyForceToCenter(new Vector2(acceleration, 0), true);
		}
	}

	// Finds the correct animation frame for the current circumstances
	private void updateAnimation() {
		timeKeeper += Gdx.graphics.getDeltaTime();

		// Check movement direction
		if (vel.x < 0) {

			// Get current frame and set to last
			currentFrame = left.getKeyFrame(timeKeeper, true);
			lastAnimation = left;

		} else if (vel.x > 0) {

			// Get current frame and set to last
			currentFrame = right.getKeyFrame(timeKeeper, true);
			lastAnimation = right;

		} else if (vel.y != 0) {

			// Update the last animation
			currentFrame = lastAnimation.getKeyFrame(timeKeeper, true);
		} else {

			// Set currentFrame to standing still frame
			currentFrame = lastAnimation.getKeyFrame(2, true);
		}
	}

	public void dispose() {
		super.dispose();
		left = null;
		right = null;
		currentFrame = null;
		inventory.dispose();
	}

	// Shoot on click
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (!GameStateManager.paused) {
			if (button == Buttons.LEFT && health > 0) {
				if (inventory.holding instanceof Gun) {
					((Gun) inventory.holding).shoot(entityHandler, world, pos, new Vector2(x, y));
				}
			}
		}
		return true;
	}

	// ----------------------------------------------------------------
	// ---------------------------- Unused ----------------------------
	// ----------------------------------------------------------------

	public boolean keyTyped(char arg0) {
		return false;
	}

	public boolean keyUp(int arg0) {
		return false;
	}

	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

	public boolean scrolled(int arg0) {
		return false;
	}

	public boolean touchDragged(int arg0, int arg1, int arg2) {
		return false;
	}

	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	public boolean keyDown(int keycode) {

		return false;
	}
}
