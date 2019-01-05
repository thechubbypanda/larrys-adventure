package net.thechubbypanda.larrysadventure.entities.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.CellManipulation;
import net.thechubbypanda.larrysadventure.utils.Vector2i;
import net.thechubbypanda.larrysadventure.utils.interfaces.Walkable;

public class Farmer extends Enemy implements Walkable, Runnable {

	private static Texture texture = new Texture("entities/enemies/farmer/farmer.png");

	private float acceleration = 20f;
	private volatile int stunned = 0;
	private final int stunTime = 120;
	private int startWait = 60;

	private Thread thread = new Thread(this, "Pathfinding 2");
	private boolean running = false;

	private volatile ArrayList<Cell> path = new ArrayList<Cell>();
	private volatile int pathPosition = 0;
	private volatile Cell lastCell;
	private volatile Vector2i target;
	private volatile boolean pathLocked = false;

	public Farmer(World world, Cell[][] cellMap, Player player, Vector2i pos) {
		super(world, cellMap, player, pos);

		target = pos;
		hitInterval = 30;
		damage = 10;
		start();
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread.start();
	}

	public void update() {
		lastCell = getClosestCell(cellMap);
		if (startWait <= 0) {
			if (stunned <= 0) {
				canHitPlayer = true;

				// Move towards the player by the shortest path
				if (path != null && path.size() > 0 && path.size() > pathPosition + 1 && lastCell == path.get(pathPosition)) {
					pathLocked = true;
					pathPosition++;
					while (pathPosition >= path.size()) {
						pathPosition--;
					}
					target = path.get(pathPosition).getWorldPos();
					pathLocked = false;
				}
				moveTowards(target.x, target.y, acceleration);

			} else {
				stunned--;
				canHitPlayer = false;
			}

			health = Integer.MAX_VALUE;

			super.update();

			if (distanceTo(player) < 60) {
				hittingPlayer = true;
			} else {
				hittingPlayer = false;
			}
		} else {
			startWait--;
		}
	}

	public void run() {
		while (running) {
			if (player.lastCell != null && lastCell != null && !CellManipulation.getNeighbours(cellMap, player.lastCell).contains(lastCell)) {
				if (!pathLocked) {
					if (path.size() == 0 || path.indexOf(player.lastCell) > pathPosition) {
						path = calculatePath();
					}
					if (lastCell == path.get(pathPosition)) {
						pathPosition++;
						if (pathPosition >= path.size()) {
							path = calculatePath();
						}
					}
					while (pathPosition >= path.size()) {
						pathPosition--;
					}
					target = path.get(pathPosition).getWorldPos();
				}
			} else {
				target = player.getPos();
			}
		}
	}

	private ArrayList<Cell> calculatePath() {
		ArrayList<Cell> neighbours = CellManipulation.getNeighbours(cellMap, player.lastCell);
		boolean nextToPlayer;

		try {
			nextToPlayer = neighbours.contains(lastCell);
		} catch (ArrayIndexOutOfBoundsException e) {
			nextToPlayer = false;
		}

		if (nextToPlayer) {
			moveTowards(player, acceleration);
		} else {
			ArrayList<Cell> bestPath = new ArrayList<Cell>();
			int bestDistanceToPlayer = Integer.MAX_VALUE;

			for (Cell cell : neighbours) {
				ArrayList<Cell> path = CellManipulation.findPath(cellMap, lastCell, cell, this);
				if (path == null || path.size() == 0) {
					continue;
				}
				int distance = player.distanceTo(path.get(path.size() - 1).getWorldPos());
				if (distance < bestDistanceToPlayer) {
					bestDistanceToPlayer = distance;
					bestPath = path;
				}
			}
			pathPosition = 0;
			return bestPath;
		}
		return path;
	}

	public void hit(int damage) {
		stunned = stunTime;
	}

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, pos.x - 32, pos.y - 32, 64, 64);
		sb.end();
	}

	public boolean isWalkable(Cell cell) {
		return cell.type == TileType.wall;
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 20f;

		Body b = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(26 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 2f;
		fixtureDef.friction = 0;
		b.createFixture(fixtureDef);

		return b;
	}

	public void dispose() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.dispose();
	}
}
