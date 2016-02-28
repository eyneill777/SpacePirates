package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import spacepirates.game.Collidable;
import spacepirates.game.Game;
import spacepirates.game.GameObject;
import spacepirates.game.level.Direction;

public abstract class Tile implements GameObject, Collidable{
	private int x;
	private int y;
	private TileMap tiles;
	private Fixture[] fixtures;
	private boolean initialized = false;
	private Sprite sprite;
	private boolean solid = false;
	private boolean sensor = false;
	
	protected void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	protected void setSolid(boolean solid){
		this.solid = solid;
	}
	
	public boolean isSolid(){
		return solid;
	}
	
	public void setSprite(Sprite sprite){
		this.sprite = sprite;
		sprite.setBounds(getX() * TileMap.TILE_SIZE + getTileMap().getOffsetX(),
				getY() * TileMap.TILE_SIZE + getTileMap().getOffsetY(),
				TileMap.TILE_SIZE, TileMap.TILE_SIZE);
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
	
	public void render(SpriteBatch batch){
		if(sprite != null){
			sprite.draw(batch);
		}
	}
	
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
		if(solid){
			fixtures = new Fixture[4];
			
			FixtureDef fixterDef = new FixtureDef();
			EdgeShape edge = new EdgeShape();
			fixterDef.shape = edge;
			
			Tile other = getTileMap().getTile(x, y + 1);
			boolean n = other != null && !other.isSolid();
			
			other = getTileMap().getTile(x, y - 1);
			boolean s = other != null && !other.isSolid();
			
			other = getTileMap().getTile(x + 1, y);
			boolean e = other != null && !other.isSolid();
			
			other = getTileMap().getTile(x - 1, y);
			boolean w = other != null && !other.isSolid();
			
			float x1 = TileMap.TILE_SIZE * x;
			float y1 = TileMap.TILE_SIZE * y;
			float x2 = x1 + TileMap.TILE_SIZE;
			float y2 = y1 + TileMap.TILE_SIZE;
			
			if(n){
				edge.set(x1, y2, x2, y2);
				if(w){
					edge.setVertex0(x1, y1);
				} else {
					edge.setVertex0(x1 - TileMap.TILE_SIZE, y2);
				}
				if(e){
					edge.setVertex3(x2, y1);
				} else {
					edge.setVertex3(x2 + TileMap.TILE_SIZE, y2);
				}
				edge.setHasVertex0(true);
				edge.setHasVertex3(true);
				fixtures[Direction.N] = getTileMap().getBody().createFixture(fixterDef);
				fixtures[Direction.N].setUserData(this);
			}
			
			if(s){
				edge.set(x2, y1, x1, y1);
				if(e){
					edge.setVertex0(x2, y2);
				} else {
					edge.setVertex0(x2 + TileMap.TILE_SIZE, y1);
				}
				if(w){
					edge.setVertex3(x1, y2);
				} else {
					edge.setVertex3(x1 - TileMap.TILE_SIZE, y1);
				}
				edge.setHasVertex0(true);
				edge.setHasVertex3(true);
				fixtures[Direction.S] = getTileMap().getBody().createFixture(fixterDef);
				fixtures[Direction.S].setUserData(this);
			}
			
			if(e){
				edge.set(x2, y2, x2, y1);
				if(n){
					edge.setVertex0(x1, y2);
				} else {
					edge.setVertex0(x2, y2 + TileMap.TILE_SIZE);
				}
				if(s){
					edge.setVertex3(x1, y1);
				} else {
					edge.setVertex3(x2, y1 - TileMap.TILE_SIZE);
				}
				edge.setHasVertex0(true);
				edge.setHasVertex3(true);
				fixtures[Direction.E] = getTileMap().getBody().createFixture(fixterDef);
				fixtures[Direction.E].setUserData(this);
			}
			
			if(w){
				edge.set(x1, y1, x1, y2);
				if(s){
					edge.setVertex0(x2, y1);
				} else {
					edge.setVertex0(x1, y1 - TileMap.TILE_SIZE);
				}
				if(n){
					edge.setVertex3(x2, y2);
				} else {
					edge.setVertex3(x1, y2 + TileMap.TILE_SIZE);
				}
				edge.setHasVertex0(true);
				edge.setHasVertex3(true);
				fixtures[Direction.W] = getTileMap().getBody().createFixture(fixterDef);
				fixtures[Direction.W].setUserData(this);
			}
			//tileShape.setAsBox(TileMap.TILE_SIZE/2, TileMap.TILE_SIZE/2, 
			//		new Vector2(x * TileMap.TILE_SIZE + TileMap.TILE_SIZE/2, y * TileMap.TILE_SIZE + TileMap.TILE_SIZE/2), 0);
			//fixterDef.shape = tileShape;
			
			//fixture = tiles.getBody().createFixture(fixterDef);
			//fixture.setUserData(this);
			
			
			edge.dispose();
		}
	}

	public void store() {
		initialized = false;
		if(solid){
			for(int i = 0; i < fixtures.length;i++){
				if(fixtures[i] != null){
					tiles.getBody().destroyFixture(fixtures[i]);
				}
			}
		}
	}
}
