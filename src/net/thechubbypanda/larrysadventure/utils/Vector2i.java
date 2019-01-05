package net.thechubbypanda.larrysadventure.utils;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.misc.Cell;

public class Vector2i implements Serializable {

	private static final long serialVersionUID = -9088285923330753957L;

	public int x, y;

	public Vector2i() {
		this(0, 0);
	}

	public Vector2i(Vector2 v) {
		this(Math.round(v.x), Math.round(v.y));
	}

	public Vector2i(Vector2i v) {
		this(v.x, v.y);
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 toVector2() {
		return new Vector2(x, y);
	}

	public boolean equals(Vector2i v) {
		return equals(v.x, v.y);
	}

	public boolean equals(int x, int y) {
		return x == this.x && y == this.y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	// Returns the cell that this object is the closest to on the given map
	public Cell getClosestCell(Cell[][] cellMap) {
		int roundedX = Math.round((float) x / Tile.SIZE);
		int roundedY = Math.round((float) y / Tile.SIZE);
		try {
			return cellMap[roundedY][roundedX];
		} catch (ArrayIndexOutOfBoundsException e) {
			return cellMap[0][0];
		}
	}
}
