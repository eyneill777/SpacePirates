package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import spacepirates.game.Collidable;
import spacepirates.game.Game;

public abstract class Tile implements Collidable{
	private int x;
	private int y;
	private TileMap tiles;
	private Fixture fixture;
	private boolean initialized = false;
	protected boolean hasFixture = false;
	
	
	protected void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setTiles(TileMap tiles){
		this.tiles = tiles;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public TileMap getTileMap(){
		return tiles;
	}

	public Game getGame(){
		return tiles.getGame();
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = new FixtureDef();
		
		fixtureDef.filter.categoryBits = Game.CAT_TILE;
		
		return fixtureDef;
	}
	
	public void init() {
		initialized = true;
		if(hasFixture){
			FixtureDef fixterDef = new FixtureDef();
			PolygonShape tileShape = tiles.getTileShape();
			
			tileShape.setAsBox(TileMap.TILE_SIZE/2, TileMap.TILE_SIZE/2, 
					new Vector2(x * TileMap.TILE_SIZE + TileMap.TILE_SIZE/2, y * TileMap.TILE_SIZE + TileMap.TILE_SIZE/2), 0);
			fixterDef.shape = tileShape;
			
			fixture = tiles.getBody().createFixture(fixterDef);
			fixture.setUserData(this);
		}
	}

	public void store() {
		initialized = false;
		if(hasFixture){
			tiles.getBody().destroyFixture(fixture);
		}
	}
}
