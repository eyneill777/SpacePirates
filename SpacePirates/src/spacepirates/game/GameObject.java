package spacepirates.game;

public interface GameObject {
	void init();
	void store();
	void update(float delta);
	boolean isInitialized();
	Game getGame();
}
