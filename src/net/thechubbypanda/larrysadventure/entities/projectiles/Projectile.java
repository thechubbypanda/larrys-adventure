package net.thechubbypanda.larrysadventure.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Entity;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Projectile extends Entity {

	private Texture texture;
	private int damage = 10;
	private boolean hitWallBefore = false;

	protected Projectile(World world, Vector2i pos, Vector2 vel) {
		super(world, pos, vel);

		texture = getTexture();
	}

	public void update() {
		super.update();
	}

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, pos.x - texture.getWidth() / 2, pos.y - texture.getHeight() / 2);
		sb.end();
	}

	public int hit() {
		toRemove = true;
		return damage;
	}

	public void hitWall() {
		if (hitWallBefore) {
			toRemove = true;
		} else {
			hitWallBefore = true;
		}
	}

	// Gets the texture from the subclass
	protected abstract Texture getTexture();
}
