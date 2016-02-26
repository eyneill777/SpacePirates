package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import spacepirates.game.Game;
import spacepirates.game.GameObject;

public class TileMap implements GameObject{
	public static final float TILE_SIZE = 1;
	
	private boolean initialized = false;
	private Tile[][] tiles;
	private Body body;
	private PolygonShape tileShape;
	private Game game;
	
	private int w, h;
	private float offsetX, offsetY;
	
	public TileMap(int w, int h){
		this.w = w;
		this.h = h;
		offsetX = 0;
		offsetY = 0;
		
		tiles = new Tile[h][w];
		
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				Tile floor = new FloorTile();
				floor.setPosition(j, i);
				floor.setTiles(this);
				tiles[i][j] = floor;
			}
		}
	}
	
	public void setTile(Tile tile, int x, int y){
		if(isInitialized()){
			tiles[y][x].store();
		}
		
		tile.setTiles(this);
		tile.setPosition(x, y);
		tiles[y][x] = tile;
		
		if(isInitialized()){
			tile.init();
		}
	}
	
	public void setOffset(float x, float y){
		offsetX = x;
		offsetY = y;
	}
	
	public float getOffsetX(){
		return offsetX;
	}
	
	public float getOffsetY(){
		return offsetY;
	}
	
	public Body getBody(){
		return body;
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j].render(batch);
			}
		}
	}
	
	public void update(float delta){
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j].update(delta);
			}
		}
	}
	
	public void init(){
		initialized = true;
		tileShape = new PolygonShape();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(offsetX, offsetY);
		
		body = game.getWorld().createBody(bodyDef);
		
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j].init();
			}
		}
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public Game getGame(){
		return game;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public void store(){
		initialized = false;
		tileShape.dispose();
		tileShape = null;
		
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j].init();
			}
		}
		
		game.getWorld().destroyBody(body);
	}
	
	public PolygonShape getTileShape(){
		return tileShape;
	}
}
