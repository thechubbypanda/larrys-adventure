package net.thechubbypanda.larrysadventure.misc;

import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Cell {
	
	// Variables for maze generation
	public TileType type;
	public boolean visited = false;
	// This position is the array position
	public Vector2i pos;
	public boolean isEnd = false;
	
	// Variables for pathfinding
	public int gcost;
	public int hcost;
	public int fcost() {
		return hcost + gcost;
	}
	public Cell parent;
	
	public Cell(int x, int y, TileType type) {
		pos = new Vector2i(x, y);
		this.type = type;
	}
	
	// Returns the position of the cell in the pixel world
	public Vector2i getWorldPos() {
		return new Vector2i(pos.x * Tile.SIZE, pos.y * Tile.SIZE);
	}
}
