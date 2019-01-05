package net.thechubbypanda.larrysadventure;

import com.badlogic.gdx.utils.Disposable;

import net.thechubbypanda.larrysadventure.utils.Constants;
import net.thechubbypanda.larrysadventure.utils.interfaces.Renderable;
import net.thechubbypanda.larrysadventure.utils.interfaces.Updatable;

public abstract class GameComponent extends Constants implements Updatable, Renderable, Disposable {
	// Cleaner way to extend and implement the same thing over and over
}
