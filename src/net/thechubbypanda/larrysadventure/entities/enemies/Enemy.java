package net.thechubbypanda.larrysadventure.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.thechubbypanda.larrysadventure.entities.Entity;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Enemy extends Entity {

	protected Player player;
	protected Cell[][] cellMap;

	public int health = 0;

	public int damage = 10;

	protected int hitInterval = 40;
	private int hitTimer = 0;
	public boolean hittingPlayer = false;
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

	public abstract void hit(int damage);

	// Checks if the cell can be walked on by this enemy
	public abstract boolean isWalkable(Cell cell);
}
