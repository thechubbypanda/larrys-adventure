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

	private volatile ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private Player player;

	private World world;
	private GameStateManager gsm;

	private boolean toChangeState = false;

	private Thread thread = new Thread(this, "Pathfinding");
	private boolean running = false;

	public EntityHandler(GameStateManager gsm, World world) {
		this.world = world;
		this.gsm = gsm;
	}

	// Sets the player for this handler
	public void setPlayer(Entity player) {
		this.player = (Player) player;
		if (entities.contains(player)) {
			entities.remove(player);
		}
	}

	public void addEntity(Entity entity) {
		if (!entities.contains(entity)) {
			entities.add(entity);
		}
	}

	public void removeEntity(Entity entity) {
		world.destroyBody(entity.getBody());
		entity.dispose();
		entities.remove(entity);
	}

	public void addItem(Item item) {
		items.add(item);
	}

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
			gsm.setState(GameState.GAMEOVER);
		} else {

			// Update all the entities
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

			// Remove entities and play
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

			// Do stuff according to what type the colliding entities are

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

			// Do stuff according to what type the colliding entities are

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

	// ----------------------------------------------------------------
	// ---------------------------- Unused ----------------------------
	// ----------------------------------------------------------------

	public void postSolve(Contact arg0, ContactImpulse arg1) {

	}

	public void preSolve(Contact arg0, Manifold arg1) {

	}
}
