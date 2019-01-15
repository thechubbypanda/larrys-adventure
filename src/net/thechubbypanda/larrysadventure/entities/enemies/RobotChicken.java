package net.thechubbypanda.larrysadventure.entities.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.AnimationController;
import net.thechubbypanda.larrysadventure.utils.CellManipulation;
import net.thechubbypanda.larrysadventure.utils.Vector2i;
import net.thechubbypanda.larrysadventure.utils.interfaces.Walkable;

public class RobotChicken extends Enemy implements Walkable {

	// Animations for the enemy
	private Animation<TextureRegion> left, right;

	// The last animation used
	private Animation<TextureRegion> lastAnimation;

	// The current animation's current frame
	private TextureRegion currentFrame;

	// Arbitrary animation counter
	private float timeKeeper = 10000;

	// Duration off each frame
	private float frameDuration = 0.11f;

	// True if in the view of the camera
	private boolean inFrustrum = false;

	// Acceleration applied to enemy when it needs to move
	private static final float acceleration = 12f;

	// Current best path to player
	private volatile ArrayList<Cell> path = new ArrayList<Cell>();

	// Index of the enemy on the current path
	private volatile int pathPosition = 0;

	// The last cell that this enemy was on
	private volatile Cell lastCell;

	// The target cell's location (usually the player's cell)
	private volatile Vector2i target;

	// True if the path is in the process of being changed
	private volatile boolean pathLocked = false;

	public RobotChicken(World world, Cell[][] cellMap, Player player, Vector2i pos) {
		super(world, cellMap, player, pos);

		this.target = pos;

		health = 10;

		Texture texture;

		texture = new Texture("entities/enemies/robotChicken/left.png");
		left = AnimationController.createAnimation(texture, 3, 1, frameDuration);

		texture = new Texture("entities/enemies/robotChicken/right.png");
		right = AnimationController.createAnimation(texture, 3, 1, frameDuration);

		currentFrame = right.getKeyFrame(0);
		lastAnimation = right;
	}

	protected Body getBody(World world, Vector2i pos, Vector2 vel) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x / PPM, pos.y / PPM);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 20f;

		Body b = world.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(15 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 5f;
		fixtureDef.friction = 0;
		b.createFixture(fixtureDef);

		return b;
	}

	public void update() {
		super.update();
		lastCell = getClosestCell(cellMap);
		if (path != null && path.size() > 0 && path.size() > pathPosition + 1 && lastCell == path.get(pathPosition)) {
			pathLocked = true;
			pathPosition++;
			if (pathPosition >= path.size()) {
				pathPosition--;
			}
			target = path.get(pathPosition).getWorldPos();
			pathLocked = false;
		}
		moveTowards(target.x, target.y, acceleration);

		// Checking if in the view of the camera
		inFrustrum = camera.frustum.boundsInFrustum(new Vector3(pos.x, pos.y, 0), new Vector3(32, 32, 0));
		if (inFrustrum) {
			updateAnimation();
		}
	}

	// Called by EntityHandler secondary "Pathfinding" thread to calculate the best
	// path to the player
	public void setTarget() {
		if (lastCell != player.lastCell) {
			if (!pathLocked) {
				if (path.size() == 0 || path.indexOf(player.lastCell) > pathPosition) {
					path = calculatePath();
				}
				if (lastCell == path.get(pathPosition)) {
					pathPosition++;
					if (pathPosition >= path.size()) {
						path = calculatePath();
					}
				}
				target = path.get(pathPosition).getWorldPos();
			}
		} else {
			target = player.getPos();
		}
	}

	// Null checks
	private ArrayList<Cell> calculatePath() {
		ArrayList<Cell> temp = CellManipulation.findPath(cellMap, lastCell, player.lastCell, this);
		if (temp != null) {
			pathPosition = 0;
			return temp;
		}
		return path;
	}

	// Only rendered if the user can see it
	public void render() {
		if (inFrustrum) {
			sb.setProjectionMatrix(camera.combined);
			sb.begin();
			sb.draw(currentFrame, pos.x - 16, pos.y - 16, 32, 32);
			sb.end();
		}
	}

	public boolean isWalkable(Cell cell) {
		return cell.type == TileType.grass;
	}

	// Changes the displayed image according to velocity
	public void updateAnimation() {
		Animation<TextureRegion> currentLeftAnimation = left;
		Animation<TextureRegion> currentRightAnimation = right;
		timeKeeper += Gdx.graphics.getDeltaTime();

		// Check movement direction
		if (vel.x < 0) {

			// Get current frame and set to last
			currentFrame = currentLeftAnimation.getKeyFrame(timeKeeper, true);
			lastAnimation = currentLeftAnimation;

		} else if (vel.x > 0) {

			// Get current frame and set to last
			currentFrame = currentRightAnimation.getKeyFrame(timeKeeper, true);
			lastAnimation = currentRightAnimation;

		} else if (vel.y != 0) {

			// Update the last animation
			currentFrame = lastAnimation.getKeyFrame(timeKeeper, true);
		} else {

			// Set currentFrame to standing still frame
			currentFrame = lastAnimation.getKeyFrame(2, true);
		}
	}

	public void hit(int damage) {
		health -= damage;
	}

	public synchronized void dispose() {
		super.dispose();
	}
}
