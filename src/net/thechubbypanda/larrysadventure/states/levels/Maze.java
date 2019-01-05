package net.thechubbypanda.larrysadventure.states.levels;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.entities.LevelExit;
import net.thechubbypanda.larrysadventure.entities.enemies.Farmer;
import net.thechubbypanda.larrysadventure.entities.enemies.Spawner;
import net.thechubbypanda.larrysadventure.entities.tiles.GrassTile;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile;
import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.entities.tiles.WallTile;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.CellManipulation;
import net.thechubbypanda.larrysadventure.utils.Utils;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Maze extends Level {

	private Cell[][] cellMap;
	private int level = 0;
	private boolean done = false;

	public Maze(GameStateManager gsm, int level) {
		super(gsm);

		this.level = level;
		int size = 9 + (4 * level);

		createPlayer(Tile.SIZE, Tile.SIZE);

		Vector2i startPos = new Vector2i(1, 1);

		if (size % 2 == 0) {
			System.err.println("Map size must be odd!");
			Gdx.app.exit();
		}

		cellMap = generateBlankCellMap(size);
		cellMap = generateMaze(cellMap, startPos);

		map = createTileMap(cellMap, startPos);
		player.setMap(cellMap);
		entityHandler.start();
		done = true;
	}

	public void update() {
		if (done) {
			super.update();
		}
	}

	private static Cell[][] generateBlankCellMap(int size) {
		Cell[][] cellMap = new Cell[size][size];

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (x % 2 != 0 && y % 2 != 0) {
					cellMap[y][x] = new Cell(x, y, TileType.grass);
				} else {
					cellMap[y][x] = new Cell(x, y, TileType.wall);
				}
			}
		}
		return cellMap;
	}

	// Randomly generates a maze
	private static Cell[][] generateMaze(Cell[][] cellMap, Vector2i startPos) {
		Stack<Cell> stack = new Stack<Cell>();
		Cell cell = cellMap[startPos.y][startPos.x];
		int largestStack = Integer.MIN_VALUE;
		Cell furthestCell = null;

		do {
			// Mark the cell as visited
			cell.visited = true;

			// Check if the cellMap still has unvisited cells
			if (CellManipulation.containsUnvisited(cellMap)) {

				// Get the unvisited neighbours of the current cell
				ArrayList<Cell> unvisitedNeighbours = CellManipulation.getUnvisitedNeighbours(cellMap, cell);

				// Check if the cell has unvisited neighbours
				if (!unvisitedNeighbours.isEmpty()) {

					// Choose a random cell to go to next and go to next
					Cell nextCell = unvisitedNeighbours.get(Utils.randomInt(0, unvisitedNeighbours.size() - 1));
					stack.push(nextCell);
					CellManipulation.removeWallBetween(cellMap, cell, nextCell);
					cell = nextCell;

				} else if (!stack.empty()) {

					// Find the furthest cell from the start
					if (CellManipulation.isDeadEnd(cellMap, cell)) {
						if (largestStack < stack.size()) {
							largestStack = stack.size();
							furthestCell = cell;
						}
					}

					// Backtrack
					cell = stack.pop();
				}
			}

		} while (!stack.empty());

		furthestCell.isEnd = true;

		return cellMap;
	}

	// Creates a Tile map from a Cell map. Also adds enemies, etc.
	private Tile[][] createTileMap(Cell[][] cellMap, Vector2i startPos) {
		ArrayList<Cell> spawnerLocations = new ArrayList<Cell>();

		Tile[][] tileMap = new Tile[cellMap.length][cellMap[0].length];
		Cell endCell = null;

		for (Cell[] cells : cellMap) {
			for (Cell cell : cells) {
				if (cell.type == TileType.grass) {
					tileMap[cell.pos.y][cell.pos.x] = new GrassTile(world, cell.getWorldPos());

					if (CellManipulation.isDeadEnd(cellMap, cell) && cell != cellMap[startPos.x][startPos.y] && !cell.isEnd) {
						spawnerLocations.add(cell);
					}

					if (cell.isEnd) {
						entityHandler.addEntity(new LevelExit(world, cell.getWorldPos()));
						endCell = cell;
					}
				} else {
					tileMap[cell.pos.y][cell.pos.x] = new WallTile(world, cell, cellMap);
				}
			}
		}

		ArrayList<Cell> neighbours = CellManipulation.getNeighbours(cellMap, endCell);
		Cell spawn;

		do {
			spawn = neighbours.get(Utils.randomInt(0, neighbours.size() - 1));
		} while (spawn.type != TileType.wall);

		if (GameStateManager.level >= 5) {
			entityHandler.addEntity(new Farmer(world, cellMap, player, spawn.getWorldPos()));
		}

		ArrayList<Spawner> spawners = new ArrayList<Spawner>();
		while (!spawnerLocations.isEmpty()) {
			Cell cell = spawnerLocations.get(random.nextInt(spawnerLocations.size()));
			spawnerLocations.remove(cell);
			Spawner temp = new Spawner(world, entityHandler, cellMap, player, cell.getWorldPos(), level);
			entityHandler.addEntity(temp);
			spawners.add(temp);
		}

		int keysGiven = 0;
		if (spawners.size() > 0) {
			while (keysGiven < 2) {
				Spawner s = spawners.get(Utils.randomInt(0, spawners.size() - 1));
				if (!s.hasKey) {
					s.hasKey = true;
					keysGiven++;
				}
			}
		}

		return tileMap;
	}

	public void dispose() {
		map = null;
		super.dispose();
	}
}
