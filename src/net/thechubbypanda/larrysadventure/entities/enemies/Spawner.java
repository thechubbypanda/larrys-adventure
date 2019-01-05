package net.thechubbypanda.larrysadventure.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Entity;
import net.thechubbypanda.larrysadventure.entities.EntityHandler;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.items.Key;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.Utils;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Spawner extends Entity {

	private static final Texture texture = new Texture("spawner.png");

	public boolean hasKey = false;

	private World world;
	private EntityHandler entityHandler;
	private Cell[][] cellMap;
	private Player player;
	private long lastTime = 0;
	private long timeBetweenSpawns = 0;
	private int health = 30;

	public Spawner(World world, EntityHandler entityHandler, Cell[][] cellMap, Player player, Vector2i pos, int level) {
		super(world, pos, new Vector2());

		this.world = world;
		this.entityHandler = entityHandler;
		this.cellMap = cellMap;
		this.player = player;
		int variability = 5000 * level;
		timeBetweenSpawns = 5000 + Utils.randomInt(0, variability);
		lastTime = System.currentTimeMillis() - 5000;
	}

	public void update() {
		super.update();

		// Spawn enemies
		if (System.currentTimeMillis() > lastTime + timeBetweenSpawns) {
			entityHandler.addEntity(new RobotChicken(world, cellMap, player, pos));
			lastTime = System.currentTimeMillis();
		}

		if (health <= 0) {
			toRemove = true;
			Player.score += 10;
			if (hasKey) {
				Key k = new Key(pos);
				k.dropped = true;
				entityHandler.addItem(k);
			}
		}
	}

	public void render() {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, pos.x - 16, pos.y - 16, 32, 32);
		sb.end();
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.type = BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 / PPM, 16 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;

		Body b = world.createBody(bodyDef);
		b.createFixture(fixtureDef);
		return b;
	}

	public Spawner hit(int damage) {
		health -= damage;
		return this;
	}

	public void dispose() {
		super.dispose();
	}
}
