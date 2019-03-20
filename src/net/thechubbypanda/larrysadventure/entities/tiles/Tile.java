package net.thechubbypanda.larrysadventure.entities.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Entity;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Tile extends Entity {

	public static enum TileType {
		grass(), wall();
	}

	// Default tile size
	public static int SIZE = 64;
	public static Vector3 dimensions = new Vector3(SIZE, SIZE, 0);

	private final Texture texture = getTexture();
	public final TileType type = getType();

	protected Tile(World world, Vector2i pos) {
		super(world, pos, new Vector2());
	}

	protected abstract Texture getTexture();

	protected abstract TileType getType();

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE, SIZE);
		sb.end();
	}
}
