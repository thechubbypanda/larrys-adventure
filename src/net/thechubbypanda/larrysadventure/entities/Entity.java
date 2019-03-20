package net.thechubbypanda.larrysadventure.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Entity extends GameComponent {

	// True if the entity handler should remove this entity
	public boolean toRemove = false;

	protected Vector2i pos;
	protected Vector2 vel;

	// Box2D physics body that pertains to this entity
	protected Body body;

	protected Entity(World world, Vector2i pos, Vector2 vel) {

		this.pos = new Vector2i(pos);
		this.vel = new Vector2(vel);

		body = getBody(world, pos, vel);
		body.setUserData(this);
	}

	// Allows subclasses to define their own body types
	protected abstract Body getBody(World world, Vector2i pos, Vector2 vel);

	public int distanceTo(Entity e) {
		return distanceTo(e.getPos().x, e.getPos().y);
	}

	public int distanceTo(Vector2i v) {
		return distanceTo(v.x, v.y);
	}

	// Finds the distance from this entity to a point
	public int distanceTo(int x, int y) {
		float xDifference, yDifference;
		if (pos.x > x) {
			xDifference = pos.x - x;
		} else {
			xDifference = x - pos.x;
		}
		if (pos.y > y) {
			yDifference = pos.y - y;
		} else {
			yDifference = y - pos.y;
		}
		return (int) Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
	}

	// Returns the cell that this object is the closest to on the given map
	public Cell getClosestCell(Cell[][] cellMap) {
		int roundedX = Math.round((float) pos.x / Tile.SIZE);
		int roundedY = Math.round((float) pos.y / Tile.SIZE);
		try {
			return cellMap[roundedY][roundedX];
		} catch (ArrayIndexOutOfBoundsException e) {
			return cellMap[0][0];
		}
	}

	protected void moveTowards(Entity e, float force) {
		moveTowards(e.getPos().x, e.getPos().y, force);
	}

	// Applies a force towards a given point
	protected void moveTowards(float x, float y, float force) {
		float angle = (float) Math.atan2(y - pos.y, x - pos.x);
		float forceX = (float) (force * Math.cos(angle));
		float forceY = (float) (force * Math.sin(angle));
		body.applyForceToCenter(new Vector2(forceX, forceY), true);
	}

	public void update() {
		// Set all values for use by subclasses
		pos.x = (int) (body.getPosition().x * PPM);
		pos.y = (int) (body.getPosition().y * PPM);
		vel = body.getLinearVelocity();

		// Round speed for simpler calculations
		if (vel.x > -0.1f && vel.x < 0.1f) {
			vel.x = 0;
		}
		if (vel.y > -0.1f && vel.y < 0.1f) {
			vel.y = 0;
		}
	}

	public Vector2i getPos() {
		return pos;
	}

	public Vector2 getVel() {
		return vel;
	}

	public Body getBody() {
		return body;
	}

	public void dispose() {
		body = null;
		pos = null;
		vel = null;
	}
}
