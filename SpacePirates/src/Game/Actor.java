package Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import resources.Resources;

public abstract class Actor {
	private Game game;
	private float x, y, width, height, rotation;
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	public Resources getResources(){
		return game.getResources();
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public abstract void init();
	public abstract void remove();
}
