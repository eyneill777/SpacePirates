package gken.rustyice.game.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Fixture;

public class FloorTile extends Tile{

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(getGame().getResources().box, 
				getTileMap().getOffsetX() + getX() * TileMap.TILE_SIZE, 
				getTileMap().getOffsetY() + getY() * TileMap.TILE_SIZE,
				TileMap.TILE_SIZE, TileMap.TILE_SIZE);
	}

	@Override
	public void init(){
		super.init();
	}

	@Override
	public boolean isConnected(Tile other) {
		return other instanceof FloorTile;
	}
}
