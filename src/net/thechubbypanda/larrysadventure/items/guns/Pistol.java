package net.thechubbypanda.larrysadventure.items.guns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.projectiles.Bullet;
import net.thechubbypanda.larrysadventure.entities.projectiles.Projectile;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Pistol extends Gun {

	public Item makeNew() {
		return new Pistol();
	}

	protected float getBulletVelocity() {
		return 6f;
	}

	protected Projectile getProjectile(World world, Vector2i startPos, Vector2 velocity) {
		return (Projectile) new Bullet(world, startPos, velocity);
	}

	protected long getShotDelayMillis() {
		return 100;
	}

	public int getMagSize() {
		return 20;
	}

	protected int getReloadTime() {
		return 1500;
	}

	public void dispose() {
	}

	protected Texture getTexture() {
		return null;
	}
}
