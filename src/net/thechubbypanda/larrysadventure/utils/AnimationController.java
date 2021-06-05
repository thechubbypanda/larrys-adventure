package net.thechubbypanda.larrysadventure.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import net.thechubbypanda.larrysadventure.utils.interfaces.Updatable;

public class AnimationController extends Constants implements Updatable, Disposable {

	private Animation<TextureRegion> animation;
	private TextureRegion currentFrame;
	private float stateTime = 0;

	public AnimationController(Texture texture, int cols, int rows, float frameDuration) {
		animation = createAnimation(texture, cols, rows, frameDuration);
		currentFrame = animation.getKeyFrame(stateTime, false);
	}

	public void update() {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, false);
	}

	public void render(SpriteBatch sb, Vector2i pos, Vector2i size) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(currentFrame, pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
		sb.end();
	}

	public boolean isFinished() {
		return animation.isAnimationFinished(stateTime);
	}

	public void dispose() {
		animation = null;
		currentFrame = null;
	}

	// Creates an animation from a spritesheet
	public static Animation<TextureRegion> createAnimation(Texture texture, int cols, int rows, float frameDuration) {
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);
		TextureRegion[] allFrames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				allFrames[index++] = tmp[i][j];
			}
		}
		return new Animation<TextureRegion>(frameDuration, allFrames);
	}
}
