package net.thechubbypanda.larrysadventure.entities.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class GrassTile extends Tile {
	
	private static Texture texture = new Texture("tiles/grass.png");

	public GrassTile(World world, Vector2i pos) {
		super(world, pos);
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.StaticBody;
		bDef.position.set(new Vector2(pos.x / PPM, pos.y / PPM));

		Body b = world.createBody(bDef);

		return b;
	}

	protected Texture getTexture() {
		return texture;
	}

	protected TileType getType() {
		return TileType.grass;
	}
}
