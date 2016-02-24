package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class WallTile extends Tile{

	public WallTile() {
		 hasFixture = true;
	}
	
	@Override
	public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void init(){
		super.init();
	}
	
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(Color.CYAN);
		batch.draw(getGame().getResources().box, 
				getTileMap().getOffsetX() + getX() * TileMap.TILE_SIZE, 
				getTileMap().getOffsetY() + getY() * TileMap.TILE_SIZE,
				TileMap.TILE_SIZE, TileMap.TILE_SIZE);
		batch.setColor(Color.WHITE);
	}
}
