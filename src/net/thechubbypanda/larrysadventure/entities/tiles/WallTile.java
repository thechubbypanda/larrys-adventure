package net.thechubbypanda.larrysadventure.entities.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class WallTile extends Tile {

	private static Texture texture = new Texture("tiles/wall.png");
	private static ChainShape leftSide, topSide, rightSide, bottomSide;

	public WallTile(World world, Cell cell, Cell[][] map) {
		super(world, cell.getWorldPos());

		// Setup the sides of the wall for the first wall
		if (leftSide == null) {
			Vector2[] points = new Vector2[2];
			leftSide = new ChainShape();
			rightSide = new ChainShape();
			topSide = new ChainShape();
			bottomSide = new ChainShape();

			points[0] = new Vector2(-SIZE / 2 / PPM, -SIZE / 2 / PPM);
			points[1] = new Vector2(-SIZE / 2 / PPM, SIZE / 2 / PPM);
			leftSide.createChain(points);

			points[0] = new Vector2(-SIZE / 2 / PPM, SIZE / 2 / PPM);
			points[1] = new Vector2(SIZE / 2 / PPM, SIZE / 2 / PPM);
			topSide.createChain(points);

			points[0] = new Vector2(SIZE / 2 / PPM, SIZE / 2 / PPM);
			points[1] = new Vector2(SIZE / 2 / PPM, -SIZE / 2 / PPM);
			rightSide.createChain(points);

			points[0] = new Vector2(-SIZE / 2 / PPM, -SIZE / 2 / PPM);
			points[1] = new Vector2(SIZE / 2 / PPM, -SIZE / 2 / PPM);
			bottomSide.createChain(points);
		}

		FixtureDef fDef = new FixtureDef();
		fDef.friction = 1f;

		try {
			if (map[cell.pos.y][cell.pos.x - 1].type != TileType.wall) {
				fDef.shape = leftSide;
				body.createFixture(fDef);
			}
		} catch (Exception e) {

		}
		try {
			if (map[cell.pos.y + 1][cell.pos.x].type != TileType.wall) {
				fDef.shape = topSide;
				body.createFixture(fDef);
			}
		} catch (Exception e) {

		} 
		try {
			if (map[cell.pos.y][cell.pos.x + 1].type != TileType.wall) {
				fDef.shape = rightSide;
				body.createFixture(fDef);
			}
		} catch (Exception e) {

		}
		try {
			if (map[cell.pos.y - 1][cell.pos.x].type != TileType.wall) {
				fDef.shape = bottomSide;
				body.createFixture(fDef);
			}
		} catch (Exception e) {

		}
	}

	public WallTile(World world, Vector2i pos, boolean left, boolean top, boolean right, boolean bottom) {
		super(world, pos);

		FixtureDef fDef = new FixtureDef();
		fDef.friction = 1f;

		if (left) {
			fDef.shape = leftSide;
			body.createFixture(fDef);
		}
		if (top) {
			fDef.shape = topSide;
			body.createFixture(fDef);
		}
		if (right) {
			fDef.shape = rightSide;
			body.createFixture(fDef);
		}
		if (bottom) {
			fDef.shape = bottomSide;
			body.createFixture(fDef);
		}
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
		return TileType.wall;
	}
}
