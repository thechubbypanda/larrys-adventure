package net.thechubbypanda.larrysadventure.items.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.EntityHandler;
import net.thechubbypanda.larrysadventure.entities.projectiles.Projectile;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.utils.Utils;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Gun extends Item {

	private long lastShot;
	private int reloading = 0;
	public int mag = getMagSize();
	private int width;

	private Sound shoot;

	protected Gun() {
		super(null);

		lastShot = System.currentTimeMillis();
		width = Utils.getTextDimensions(font20, "Reloading").x;

		shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
	}

	public void update() {
		if (mag <= 0 || (mag != getMagSize() && Gdx.input.isKeyPressed(Input.Keys.R))) {
			reload();
		}
		if (reloading > 0) {
			reloading--;
		}
	}

	public void render() {
		if (reloading > 0) {
			sb.setProjectionMatrix(hudCamera.combined);
			sb.begin();
			font20.setColor(1, 0, 0, 0.8f);
			font20.draw(sb, "Reloading", Gdx.graphics.getWidth() / 2 - width / 2, Gdx.graphics.getHeight() / 2 + 75);
			sb.end();
		}
	}

	/**
	 * Creates a bullet at a position and shoots it in the correct direction
	 *
	 * @param entityHandler
	 * @param world
	 * @param startPos
	 * @param direction
	 */
	public void shoot(EntityHandler entityHandler, World world, Vector2i startPos, Vector2 direction) {
		if (System.currentTimeMillis() > lastShot + getShotDelayMillis() && reloading == 0 && mag > 0) {
			float bulletVelocity = getBulletVelocity();
			float diffX = direction.x - Gdx.graphics.getWidth() / 2;
			float diffY = direction.y - Gdx.graphics.getHeight() / 2;
			float angle = (float) Math.atan2(diffY, diffX);
			float velX = (float) (bulletVelocity * Math.cos(angle));
			float velY = (float) (bulletVelocity * Math.sin(angle));
			Vector2 velocity = new Vector2(velX, -velY);
			entityHandler.addEntity(getProjectile(world, startPos, velocity));
			lastShot = System.currentTimeMillis();
			mag--;
			shoot.play(0.3f);
		}
	}

	public void reload() {
		reloading = (int) (getReloadTime() * 0.06f);
		mag = getMagSize();
	}

	protected abstract float getBulletVelocity();

	protected abstract long getShotDelayMillis();

	public abstract int getMagSize();

	protected abstract int getReloadTime();

	protected abstract Projectile getProjectile(World world, Vector2i startPos, Vector2 velocity);
}
