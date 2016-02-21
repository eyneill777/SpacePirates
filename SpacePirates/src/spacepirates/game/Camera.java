package spacepirates.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author gabek
 */
public class Camera{
	private Actor target;
    private boolean tracking;
    private boolean relative_rotation;
    private float x, y, rot, target_x, target_y, target_rot;
    private float width, height;

    public Camera() {
        target = null;
        tracking = false;
        relative_rotation = false;
    }


    public void update(float delta) {
    	if(tracking){
            if(relative_rotation){
                rot = target.getRotation() + target_rot;
            } else {
                rot = target_rot;
            }

            x = target.getX() + target.getWidth()/2;
            y = target.getY() + target.getHeight()/2;
        } else {
            rot = target_rot;
            x = target_x;
            y = target_y;
        }
    }

    public void setTracking(boolean tracking){
        this.tracking = tracking;
    }

    public boolean isTracking(){
        return tracking;
    }

    public void setTarget(Actor target){
        this.target = target;
    }

    public void setRelativeRotation(boolean relative){
        relative_rotation = relative;
    }


    public float getRotation() {
		return rot;
	}

    public float getX(){
    	return x;
    }
    
    public float getY(){
    	return y;
    }

    public void setX(float x){
    	target_x = x;
    }
    
    public void setY(float y){
    	target_y = y;
    }
    
    public void setRotation(float rotation){
    	target_rot = rotation;
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
    
	public void apply(com.badlogic.gdx.graphics.Camera ortho){
        ortho.position.x = x;
        ortho.position.y = y;
        ortho.viewportWidth = getWidth();
        ortho.viewportHeight = getHeight();

        ortho.up.set(MathUtils.cosDeg(rot + 90),  -MathUtils.sinDeg(rot + 90), 0);
        ortho.direction.set(0, 0, 1);
    }
}
