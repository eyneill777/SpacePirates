package spacepirates.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
	void init();
	void store();
	void update(float delta);
    void render(SpriteBatch batch);
	boolean isInitialized();
	float getX();
	float getY();
	Game getGame();
}
