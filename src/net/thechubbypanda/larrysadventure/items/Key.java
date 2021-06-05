package net.thechubbypanda.larrysadventure.items;

import com.badlogic.gdx.graphics.Texture;

import net.thechubbypanda.larrysadventure.utils.Vector2i;

public class Key extends Item {
	
	private static Texture texture = new Texture("key.png");

	public Key(Vector2i pos) {
		super(pos);
	}

	protected Texture getTexture() {
		return texture;
	}
}
