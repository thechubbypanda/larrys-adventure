package net.thechubbypanda.larrysadventure.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class LevelExit extends Entity {

	private static Texture texture = new Texture("levelExit.png");

	public LevelExit(World world, Vector2i pos) {
		super(world, pos, new Vector2());
	}

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, pos.x - Tile.SIZE / 2, pos.y - Tile.SIZE / 2, Tile.SIZE, Tile.SIZE);
		sb.end();
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.fixedRotation = true;
		bodyDef.type = BodyType.DynamicBody;
		
		Body b = world.createBody(bodyDef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(1 / PPM);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		
		b.createFixture(fixtureDef);
		return b;
	}

}
