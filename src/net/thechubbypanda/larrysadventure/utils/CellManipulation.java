package net.thechubbypanda.larrysadventure.utils;

import java.util.ArrayList;
import java.util.Collections;

import net.thechubbypanda.larrysadventure.entities.tiles.Tile.TileType;
import net.thechubbypanda.larrysadventure.misc.Cell;
import net.thechubbypanda.larrysadventure.utils.interfaces.Walkable;

public class CellManipulation {

	// Finds the best path from one cell to another (A* algorithm)
	public static ArrayList<Cell> findPath(Cell[][] cellMap, Cell start, Cell target, Walkable enemy) {
		// Contains cells that have been fully checked
		ArrayList<Cell> closedSet = new ArrayList<Cell>();

		// Contains cells that are being checked
		ArrayList<Cell> openSet = new ArrayList<Cell>();

		// Add the staring cell to the open set
		openSet.add(start);

		// While there are cells in the open set
		while (openSet.size() > 0) {
			Cell currentCell = openSet.get(0);

			// Find the cell with the lowest cost to the target
			for (int i = 1; i < openSet.size(); i++) {

				// If the fcost (total cost from start to goal) of the next cell is < the
				// current cell's
				if (openSet.get(i).fcost() < currentCell.fcost()) {
					// Change to the next cell
					currentCell = openSet.get(i);
				}

				// If the fcost of the next cell is equal to the current cell's fcost and
				// the next cell's hcost (heuristic from cell to target) is < the current cell's
				// hcost
				else if (openSet.get(i).fcost() == currentCell.fcost() && openSet.get(i).hcost < currentCell.hcost) {
					// Chageg to the next cell
					currentCell = openSet.get(i);
				}
			}

			// We will be done with the current cell after this so move it into the closed
			// set
			openSet.remove(currentCell);
			closedSet.add(currentCell);

			// If we have reached the target, calculate and return the path
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

			// Get the neighbours of the current cell and choose the cell with the lowest
			// cost of moving to (gscore)
			for (Cell neighbour : getNeighbours(cellMap, currentCell)) {
				// Skip it if we can't walk on it or we've disregarded it
				if (!enemy.isWalkable(neighbour) || closedSet.contains(neighbour)) {
					continue;
				}

				// The new total path cost if we go to the next cell
				int movementCost = currentCell.gcost + getDistanceBetween(currentCell, neighbour);

				// Discover this fast route or if we know of it and the movement cost is more
				// than the neighbour's gcost then leave it
				if (!openSet.contains(neighbour)) {
					openSet.add(neighbour);
				} else if (movementCost >= neighbour.gcost) {
					continue;
				}

				// Set the neighbours costs and parent for later use since this is the best
				// route
				neighbour.gcost = movementCost;
				neighbour.hcost = getDistanceBetween(neighbour, target);
				neighbour.parent = currentCell;
			}
		}

		// Something went wrong and a path could not be found
		return null;
	}

	// Returns the neighbours of a cell
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

	// Gets the distance between 2 cells
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

	// Checks if a cell is a dead end (has 3 walls around it)
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

	// Checks if there are unvisited cells in a map
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

	// Gets the unvisited neighbours of a cell
	public static ArrayList<Cell> getUnvisitedNeighbours(Cell[][] cellMap, Cell cell) {
		ArrayList<Cell> unvisitedNeighbours = new ArrayList<Cell>();

		for (Cell neighbour : getOddNeighbours(cellMap, cell)) {
			if (!neighbour.visited) {
				unvisitedNeighbours.add(neighbour);
			}
		}
		return unvisitedNeighbours;
	}

	// Gets only grass neighbours
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

	// Removes the wall between 2 cells
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
