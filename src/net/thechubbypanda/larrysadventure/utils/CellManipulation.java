package net.thechubbypanda.larrysadventure.utils;

import java.util.ArrayList;
import java.util.Collections;

import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.interfaces.Walkable;

public class CellManipulation {

	public static ArrayList<Cell> findPath(Cell[][] cellMap, Cell start, Cell target, Walkable enemy) {
		ArrayList<Cell> closedSet = new ArrayList<Cell>();
		ArrayList<Cell> openSet = new ArrayList<Cell>();
		openSet.add(start);

		while (openSet.size() > 0) {
			Cell currentCell = openSet.get(0);
			for (int i = 1; i < openSet.size(); i++) {
				if (openSet.get(i).fcost() < currentCell.fcost()) {
					currentCell = openSet.get(i);
				}
				if (openSet.get(i).fcost() == currentCell.fcost() && openSet.get(i).hcost < currentCell.hcost) {
					currentCell = openSet.get(i);
				}
			}

			openSet.remove(currentCell);
			closedSet.add(currentCell);

			if (currentCell == target) {
				ArrayList<Cell> path = new ArrayList<Cell>();
				currentCell = target;

				while (currentCell != start) {
					path.add(currentCell);
					currentCell = currentCell.parent;
				}

				Collections.reverse(path);
				return path;
			}

			for (Cell cell : getNeighbours(cellMap, currentCell)) {
				if (!enemy.isWalkable(cell) || closedSet.contains(cell)) {
					continue;
				}
				int movementCost = currentCell.gcost + getDistanceBetween(currentCell, cell);
				if (movementCost < cell.gcost || !openSet.contains(cell)) {
					cell.gcost = movementCost;
					cell.hcost = getDistanceBetween(cell, target);
					cell.parent = currentCell;

					if (!openSet.contains(cell)) {
						openSet.add(cell);
					}
				}
			}
		}
		return null;
	}
	
	public static ArrayList<Cell> getNeighbours(Cell[][] cellMap, Cell cell) {
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		int x = cell.pos.x;
		int y = cell.pos.y;

		if (x - 1 >= 0) {
			neighbours.add(cellMap[y][x - 1]);
		}
		if (x + 1 < cellMap[0].length) {
			neighbours.add(cellMap[y][x + 1]);
		}
		if (y - 1 >= 0) {
			neighbours.add(cellMap[y - 1][x]);
		}
		if (y + 1 < cellMap.length) {
			neighbours.add(cellMap[y + 1][x]);
		}

		return neighbours;
	}

	public static int getDistanceBetween(Cell cell1, Cell cell2) {
		int differenceX, differenceY;

		if (cell1.pos.x > cell2.pos.x) {
			differenceX = cell1.pos.x - cell2.pos.x;
		} else {
			differenceX = cell2.pos.x - cell1.pos.x;
		}

		if (cell1.pos.y > cell2.pos.y) {
			differenceY = cell1.pos.y - cell2.pos.y;
		} else {
			differenceY = cell2.pos.y - cell1.pos.y;
		}

		return differenceX + differenceY;
	}
	
	public static boolean isDeadEnd(Cell[][] map, Cell cell) {
		int surroundingWalls = 0;

		if (cell.type == TileType.grass) {
			if (map[cell.pos.y + 1][cell.pos.x].type == TileType.wall) {
				surroundingWalls++;
			}
			if (map[cell.pos.y - 1][cell.pos.x].type == TileType.wall) {
				surroundingWalls++;
			}
			if (map[cell.pos.y][cell.pos.x + 1].type == TileType.wall) {
				surroundingWalls++;
			}
			if (map[cell.pos.y][cell.pos.x - 1].type == TileType.wall) {
				surroundingWalls++;
			}
		}

		return surroundingWalls == 3;
	}
	
	public static boolean containsUnvisited(Cell[][] cellMap) {
		for (Cell[] cells : cellMap) {
			for (Cell cell : cells) {
				if (!cell.visited) {
					return true;
				}
			}
		}
		return false;
	}

	public static ArrayList<Cell> getUnvisitedNeighbours(Cell[][] cellMap, Cell cell) {
		ArrayList<Cell> unvisitedNeighbours = new ArrayList<Cell>();

		for (Cell neighbour : getOddNeighbours(cellMap, cell)) {
			if (!neighbour.visited) {
				unvisitedNeighbours.add(neighbour);
			}
		}
		return unvisitedNeighbours;
	}

	public static ArrayList<Cell> getOddNeighbours(Cell[][] cellMap, Cell cell) {
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		int x = cell.pos.x;
		int y = cell.pos.y;

		if (x - 2 >= 0) {
			neighbours.add(cellMap[y][x - 2]);
		}
		if (x + 2 < cellMap[0].length) {
			neighbours.add(cellMap[y][x + 2]);
		}
		if (y - 2 >= 0) {
			neighbours.add(cellMap[y - 2][x]);
		}
		if (y + 2 < cellMap.length) {
			neighbours.add(cellMap[y + 2][x]);
		}

		return neighbours;
	}
	
	public static void removeWallBetween(Cell[][] cellMap, Cell cell1, Cell cell2) {
		Cell toChange = null;
		if (cell1.pos.x > cell2.pos.x) {
			toChange = cellMap[cell1.pos.y][cell1.pos.x - 1];
		} else if (cell1.pos.x < cell2.pos.x) {
			toChange = cellMap[cell1.pos.y][cell1.pos.x + 1];
		} else if (cell1.pos.y > cell2.pos.y) {
			toChange = cellMap[cell1.pos.y - 1][cell1.pos.x];
		} else if (cell1.pos.y < cell2.pos.y) {
			toChange = cellMap[cell1.pos.y + 1][cell1.pos.x];
		}
		toChange.type = TileType.grass;
	}
}