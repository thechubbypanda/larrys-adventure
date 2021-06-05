package net.thechubbypanda.larrysadventure.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Bullet extends Projectile {

	public Bullet(World world, Vector2i pos, Vector2 vel) {
		super(world, pos, vel);
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.linearVelocity.set(vel);
		bodyDef.fixedRotation = true;
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.bullet = true;

		Body b = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(4 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;

		b.createFixture(fixtureDef);
		return b;
	}

	protected Texture getTexture() {
		return new Texture("entities/projectiles/bullet.png");
	}
}
