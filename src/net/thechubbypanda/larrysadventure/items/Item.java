package net.thechubbypanda.larrysadventure.items;

import com.badlogic.gdx.graphics.Texture;

import net.thechubbypanda.larrysadventure.GameComponent;
import net.thechubbypanda.larrysadventure.utils.Vector2i;

public abstract class Item extends GameComponent {

	// True if this item is on the ground (not in an inventory)
	public boolean dropped = false;

	private Vector2i pos;

	public Item(Vector2i pos) {
		this.pos = pos;
	}

	public void update() {
	}

	// Only render if dropped
	public void render() {
		if (dropped) {
			sb.setProjectionMatrix(camera.combined);
			sb.begin();
			sb.draw(getTexture(), pos.x - getTexture().getWidth() / 2, pos.y - getTexture().getHeight() / 2);
			sb.end();
		}
	}

	// Gets the subclass to return a new instance of itself
	// public abstract Item makeNew();

	protected abstract Texture getTexture();

	public Vector2i getPos() {
		return pos;
	}

	public void dispose() {
	}
}
