package net.thechubbypanda.larrysadventure.misc;

import java.util.ArrayList;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.items.Key;

public class Inventory extends GameComponent {

	public Item holding;

	// A place for storing items
	private ArrayList<Item> items = new ArrayList<Item>();

	// Updates all the items
	public void update() {
		for (Item item : items) {
			item.update();
		}
	}

	// Renders all the items
	public void render() {
		for (Item item : items) {
			item.render();
		}
	}

	// Adds an item to the inventory
	public void add(Item item) {
		if (!items.contains(item)) {
			items.add(item);
			if (holding == null) {
				holding = item;
			}
		}
	}

	// Removes all the keys from the inventory
	public void removeKeys() {
		items.removeIf(item -> item instanceof Key);
	}

	// Returns true if the inventory contains 2 keys
	public boolean hasKeys() {
		return numberOfKeys() == 2;
	}

	// Returns the number of keys that the inventory contains
	public int numberOfKeys() {
		int keys = 0;
		for (Item item : items) {
			if (item instanceof Key) {
				keys++;
			}
		}
		return keys;
	}

	public void dispose() {
		for (Item item : items) {
			item.dispose();
		}
	}
}
