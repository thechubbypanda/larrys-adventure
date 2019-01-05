package net.thechubbypanda.larrysadventure.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import net.thechubbypanda.larrysadventure.GameStateManager;
import net.thechubbypanda.larrysadventure.entities.Player;
import net.thechubbypanda.larrysadventure.items.Item;
import net.thechubbypanda.larrysadventure.items.guns.Gun;
import net.thechubbypanda.larrysadventure.utils.Constants;
import net.thechubbypanda.larrysadventure.utils.interfaces.Renderable;

public class HUDRenderer extends Constants implements Renderable {

	public static final Texture green = new Texture("green.png");
	public static final Texture red = new Texture("red.png");
	public static final Texture hit = new Texture("hit.png");

	// Renders the Heads Up Display
	public void render() {
		sb.setProjectionMatrix(hudCamera.combined);
		sb.begin();
		if (Player.justHit > 0) {
			sb.draw(hit, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		sb.draw(red, Gdx.graphics.getWidth() - 50 - 200, Gdx.graphics.getHeight() - 50 - 25, 200, 25);
		sb.draw(green, Gdx.graphics.getWidth() - 50 - 200, Gdx.graphics.getHeight() - 50 - 25, (float)(200) * ((float)Player.health / (float)Player.maxHealth), 25);
		font20.setColor(1, 1, 1, 1);
		font20.draw(sb, "Score: " + Player.score, Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 100);
		Item held = Player.inventory.holding;
		if (held instanceof Gun) {
			Gun gun = (Gun) held;
			font20.draw(sb, "Ammo: " + gun.mag + " / " + gun.getMagSize(), Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 130);
		}
		font20.draw(sb, "Keys: " + Player.inventory.numberOfKeys() + " / 2", Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 160);
		font60.draw(sb, "Level: " + GameStateManager.level, 50, Gdx.graphics.getHeight() - 50);
		sb.end();
	}
}
