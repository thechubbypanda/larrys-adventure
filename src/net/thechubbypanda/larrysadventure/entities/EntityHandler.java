package net.thechubbypanda.larrysadventure.entities;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.entities.enemies.Enemy;
import net.thechubbypanda.larrysadventure.entities.enemies.RobotChicken;
import net.thechubbypanda.larrysadventure.entities.enemies.Spawner;
import net.thechubbypanda.larrysadventure.entities.projectiles.Projectile;
import net.thechubbypanda.larrysadventure.entities.tiles.WallTile;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.states.GameState;

public class EntityHandler extends GameComponent implements ContactListener, Runnable {

	// List of entities currently in the world
	private volatile ArrayList<Entity> entities = new ArrayList<Entity>();

	// List of items currently in the world
	private ArrayList<Item> items = new ArrayList<Item>();

	// A reference to the player
	private Player player;

	// The Box2D physics world
	private World world;

	// A reference to the game state manager to allow state switching
	private GameStateManager gsm;

	// True if the game should change state on the next update cycle
	private boolean toChangeState = false;

	// Thread for running the A* algorithm for the Robot Chickens
	private Thread thread = new Thread(this, "Pathfinding");

	// True if the above thread is currently running
	private boolean running = false;

	public EntityHandler(GameStateManager gsm, World world) {
		this.world = world;
		this.gsm = gsm;
	}

	// Sets the player reference for this handler
	public void setPlayer(Entity player) {
		this.player = (Player) player;

		// Ensure the player isn't being updated and rendered twice
		if (entities.contains(player)) {
			entities.remove(player);
		}
	}

	// Adds an entity to the list of currently active entities
	public void addEntity(Entity entity) {
		if (!entities.contains(entity)) {
			entities.add(entity);
		}
	}

	// Removes an entity to the list of currently active entities and disposes of it
	public void removeEntity(Entity entity) {
		world.destroyBody(entity.getBody());
		entity.dispose();
		entities.remove(entity);
	}

	// Adds an item to the lit of currently active items
	public void addItem(Item item) {
		items.add(item);
	}

	// Starts the chicken pathfinding thread
	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread.start();
	}

	public void update() {
		// State control
		if (toChangeState) {
			if (Player.inventory.hasKeys()) {
				Player.score += 100;
			} else {
				Player.score -= 100;
				GameStateManager.level--;
			}
			Player.inventory.removeKeys();
			gsm.setState(GameState.MAZE);
		} else if (Player.health <= 0) {
			// Ending the game if the player has no health left
			gsm.setState(GameState.GAMEOVER);
		} else {

			// Update all the entities and items
			if (player != null) {
				player.update();
			}

			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).update();
			}

			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				item.update();
				if (player.distanceTo(item.getPos()) < 16) {
					item.dropped = false;
					Player.inventory.add(item);
				}
			}

			// Remove entities and instantiate Explosions in their place if needed
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if (e.toRemove) {
					if (e instanceof Enemy || e instanceof Spawner) {
						addEntity(new Explosion(world, e.pos));
					}
					removeEntity(e);
				}
			}
		}
	}

	// The method run by the pathfinding thread
	// This runs the "setTarget()" method on each robot chicken that is currently
	// active
	public void run() {
		while (running) {
			for (int i = 0; i < entities.size(); i++) {
				try {
					if (!entities.get(i).toRemove) {
						if (entities.get(i) instanceof RobotChicken) {
							((RobotChicken) entities.get(i)).setTarget();

						}
					}
				} catch (ClassCastException | NullPointerException | IndexOutOfBoundsException e) {
					// Inevitable concurrency exceptions
				}
			}
		}
	}

	// Render all the things
	public void render() {
		for (Entity entity : entities) {
			entity.render();
		}

		for (Item item : items) {
			item.render();
		}

		if (player != null) {
			player.render();
		}
	}

	// Called by Box2D when 2 bodies collide
	public void beginContact(Contact contact) {

		// Get each fixture's relevant Entity
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

		if (entityA != null && entityB != null) {

			// Print collision
			/*
			 * System.out.println("Collision began between:");
			 * System.out.println("Entity A: " + entityA.toString().substring(44));
			 * System.out.println("Entity B: " + entityB.toString().substring(44));
			 * System.out.println();
			 */

			// Check what class each of the colliding entities is and run the relevant
			// functions for each event
			// e.g. If the first entity is the player and the second is an enemy, the enemy
			// is hitting the player so the variable is set to true

			if (entityA == player) {
				if (entityB instanceof Enemy) {
					((Enemy) entityB).hittingPlayer = true;
				}

				if (entityB instanceof LevelExit) {
					toChangeState = true;
				}
			}

			if (entityA instanceof Projectile) {
				if (entityB instanceof Enemy) {
					((Enemy) entityB).hit(((Projectile) entityA).hit());
				}

				if (entityB instanceof Spawner) {
					((Spawner) entityB).hit(((Projectile) entityA).hit());
				}

				if (entityB instanceof WallTile) {
					((Projectile) entityA).hitWall();
				}
			}

			if (entityA instanceof Enemy) {
				if (entityB instanceof Projectile) {
					((Enemy) entityA).hit(((Projectile) entityB).hit());
				}

				if (entityB == player) {
					((Enemy) entityA).hittingPlayer = true;
				}
			}

			if (entityA instanceof Spawner) {
				if (entityB instanceof Projectile) {
					((Spawner) entityA).hit(((Projectile) entityB).hit());
				}
			}

			if (entityA instanceof WallTile) {
				if (entityB instanceof Projectile) {
					((Projectile) entityB).hitWall();
				}
			}
		}
	}

	// Called by Box2D when 2 bodies sto colliding
	public void endContact(Contact contact) {

		// Get each fixture's relevant Entity
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

		if (entityA != null && entityB != null) {

			// Print collision
			/*
			 * System.out.println("Collision ended between:");
			 * System.out.println("Entity A: " + entityA.toString().substring(44));
			 * System.out.println("Entity B: " + entityB.toString().substring(44));
			 * System.out.println();
			 */

			// Check what class each of the entities is and run the relevant functions for
			// each event

			if (entityA instanceof Enemy) {
				if (entityB == player) {
					((Enemy) entityA).hittingPlayer = false;
				}
			}
		}

		if (entityA == player) {

			if (entityB instanceof Enemy) {
				((Enemy) entityB).hittingPlayer = false;
			}
		}
	}

	public void dispose() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Entity entity : entities) {
			entity.dispose();
		}
	}

	public void postSolve(Contact arg0, ContactImpulse arg1) {

	}

	public void preSolve(Contact arg0, Manifold arg1) {

	}
}
