package net.thechubbypanda.larrysadventure.states.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.entities.EntityHandler;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.states.GameState;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Level extends GameState implements ContactListener {

	protected World world;
	protected Player player;
	protected EntityHandler entityHandler;
	protected Tile[][] map;

	protected Level(GameStateManager gsm) {
		super(gsm, StateType.game);

		world = new World(new Vector2(), true);
		world.setContactListener(this);

		entityHandler = new EntityHandler(gsm, world);
	}

	public void update() {
		world.step(Gdx.graphics.getDeltaTime(), 8, 4);
		world.clearForces();
		entityHandler.update();
		if (player != null) {
			camera.position.set(player.getPos().toVector2(), 0);
		}
	}

	public void render() {
		// Draw the tiles currently visible by the player
		for (Tile[] tiles : map) {
			for (Tile tile : tiles) {
				if (camera.frustum.boundsInFrustum(new Vector3(tile.getPos().toVector2(), 0), Tile.dimensions)) {
					tile.render();
				}
			}
		}

		entityHandler.render();
	}

	// Adds a new player at the specified position
	protected void createPlayer(int x, int y) {
		player = new Player(entityHandler, world, new Vector2i(x, y));
		entityHandler.setPlayer(player);
	}

	// Called by Box2D when a 2 fixtures start to collide
	public void beginContact(Contact contact) {
		entityHandler.beginContact(contact);
	}

	// Called by Box2D when a 2 fixtures finish colliding
	public void endContact(Contact contact) {
		entityHandler.endContact(contact);
	}

	public void dispose() {
		world.dispose();
		world = null;
		if (player != null) {
			player.dispose();
			player = null;
		}
		entityHandler.dispose();
		entityHandler = null;
	}

	// ----------------------------------------------------------------
	// ---------------------------- Unused ----------------------------
	// ----------------------------------------------------------------

	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
