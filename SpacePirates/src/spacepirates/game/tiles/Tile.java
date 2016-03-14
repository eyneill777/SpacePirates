package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import spacepirates.game.physics.Collidable;
import spacepirates.game.Game;
import spacepirates.game.GameObject;
import spacepirates.game.physics.Collision;
import spacepirates.game.physics.PhysicsComponent;

public abstract class Tile implements GameObject, Collidable{
	private int x;
	private int y;
	private TileMap tiles;
	private boolean initialized = false;
	private Sprite sprite;
	private PhysicsComponent tilePhysics;
	private boolean solid;

	protected void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setSprite(Sprite sprite){
		this.sprite = sprite;
		sprite.setBounds(getX() * TileMap.TILE_SIZE + getTileMap().getOffsetX(),
				getY() * TileMap.TILE_SIZE + getTileMap().getOffsetY(),
				TileMap.TILE_SIZE, TileMap.TILE_SIZE);
	}

    public Sprite getSprite(){
        return sprite;
    }

    public void setTilePhysics(PhysicsComponent tilePhysics){
        this.tilePhysics = tilePhysics;
    }

	public void setTiles(TileMap tiles){
		this.tiles = tiles;
	}
	
	public int getTileX(){
		return x;
	}
	
	public int getTileY(){
		return y;
	}

	public float getX(){
		return x * TileMap.TILE_SIZE;
	}

	public float getY(){
		return y * TileMap.TILE_SIZE;
	}

	public TileMap getTileMap(){
		return tiles;
	}

	public Game getGame(){
		return tiles.getGame();
	}
	
	public void update(float delta){
        if(tilePhysics != null){
            tilePhysics.update(delta);
        }
    }
	
	public void render(SpriteBatch batch){
		if(sprite != null){
			sprite.draw(batch);
		}
	}
	
	public boolean isInitialized(){
		return initialized;
	}

	public boolean isSolid(){
		return solid;
	}

	public void setSolid(boolean solid){
		this.solid = solid;
	}

	public FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = new FixtureDef();
		
		fixtureDef.filter.categoryBits = Game.CAT_TILE;
		
		return fixtureDef;
	}
	
	public void init() {
		initialized = true;
        if(tilePhysics != null){
            tilePhysics.init();
        }
	}

	public void store() {
		initialized = false;
        if(tilePhysics != null){
            tilePhysics.store();
        }
	}

	public abstract boolean isConnected(Tile other);

    @Override
    public void beginCollision(Collision collision) {
        tilePhysics.beginCollision(collision);
    }

    @Override
    public void endCollision(Collision collision) {
        tilePhysics.endCollision(collision);
    }

    @Override
    public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
        return tilePhysics.shouldCollide(thisFixture, otherFixture);
    }
}
