package net.thechubbypanda.larrysadventure.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.utils.AnimationController;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Explosion extends Entity {

	private static Texture texture = new Texture("explosionSprite.png");
	private static Vector2i size = new Vector2i(32, 32);
	private static Sound explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

	private AnimationController animation;

	public Explosion(World world, Vector2i pos) {
		super(world, pos, Vector2.Zero);
		animation = new AnimationController(texture, 6, 6, 0.01f);
		explosion.play(0.6f);
	}

	public void update() {
		super.update();
		animation.update();
		if (animation.isFinished()) {
			this.toRemove = true;
		}
	}

	public void render() {
		animation.render(sb, pos, size);
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(pos.x / PPM, pos.y / PPM));
		bodyDef.type = BodyType.StaticBody;

		return world.createBody(bodyDef);
	}
}
