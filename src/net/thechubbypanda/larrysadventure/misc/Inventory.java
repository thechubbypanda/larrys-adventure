package net.thechubbypanda.larrysadventure.misc;

import java.util.ArrayList;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.items.Key;

public class Inventory extends GameComponent {

	public Item holding;

	// A place for storing items
	private ArrayList<Item> items = new ArrayList<Item>();

	public void update() {
		for (Item item : items) {
			item.update();
		}
	}

	public void render() {
		for (Item item : items) {
			item.render();
		}
	}

	public void add(Item item) {
		if (!items.contains(item)) {
			items.add(item);
			if (holding == null) {
				holding = item;
			}
		}
	}

	public void removeKeys() {
		items.removeIf(item -> item instanceof Key);
	}

	public boolean hasKeys() {
		return numberOfKeys() == 2;
	}

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
