package rustyice.game.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import rustyice.game.Game;

public class TileMap{
	public static final float TILE_SIZE = 1;
	
	private boolean initialized = false;
	private Tile[][] tiles;
	private Body body;
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
				Tile tile;
				if(i == 0 || j == 0 || i == h-1 || j == w-1){
					tile = new BoundaryTile();
				} else {
					tile = new FloorTile();
				}
				
				tile.setPosition(j, i);
				tile.setTiles(this);
				tiles[i][j] = tile;
			}
		}
	}
	
	public int getWidth(){
		return w;
	}
	
	public int getHeight(){
		return h;
	}
	
	public void setTile(Tile tile, int x, int y){
		if(initialized){
			tiles[y][x].store();
		}
		
		tile.setTiles(this);
		tile.setPosition(x, y);
		tiles[y][x] = tile;
		
		if(initialized){
			tile.init();
		}
	}
	
	public Tile getTile(int x, int y){
		if(x >= 0 && y >= 0 && x < w && y < h){
			return tiles[y][x];
		} else {
			return null;
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
		
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j].init();
			}
		}
		
		game.getWorld().destroyBody(body);
	}
}
