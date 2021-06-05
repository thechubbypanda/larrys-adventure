package net.thechubbypanda.larrysadventure.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Entity;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Enemy extends Entity {

	// A reference to the player
	protected Player player;

	// The current level map
	protected Cell[][] cellMap;

	// the enemie's health
	public int health = 0;

	// The amount of damage this enemy does to the player per hit
	public int damage = 10;

	// Time between each hit from the enemy
	protected int hitInterval = 40;

	// Frames until the enemy can hit again
	private int hitTimer = 0;

	// True if the enemy is currently hitting the player
	public boolean hittingPlayer = false;

	// True if the enemy can actually hit the player
	protected boolean canHitPlayer = true;

	public Enemy(World world, Cell[][] cellMap, Player player, Vector2i pos) {
		super(world, pos, new Vector2());

		this.player = player;
		this.cellMap = cellMap;
	}

	public void update() {
		super.update();

		// Check health
		if (health <= 0) {
			this.toRemove = true;
			Player.score += 10;
		}

		// Damage the player
		if (hitTimer <= 0) {
			if (hittingPlayer) {
				if (canHitPlayer) {
					Player.health -= damage;
					hitTimer = hitInterval;
					Player.justHit = 10;
				}
			}
		} else {
			hitTimer--;
		}
	}

	// Called when this enemy is hit by something
	public abstract void hit(int damage);

	// Checks if the cell can be walked on by this enemy
	public abstract boolean isWalkable(Cell cell);
}
