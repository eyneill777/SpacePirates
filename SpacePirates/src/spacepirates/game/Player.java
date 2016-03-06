package spacepirates.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import spacepirates.game.physics.SBPhysicsComponent;
import spacepirates.input.Actions;
import spacepirates.input.PlayerInput;

public class Player extends Actor{
	private SBPhysicsComponent pComponent;
	private PlayerInput playerInput;
	private Sprite boxSprite;
	private float speed = 6;
	
	public Player() {
        setPhysicsComponent(pComponent = new SBPhysicsComponent(this));
		setWidth(0.98f);
		setHeight(0.98f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		float fx = 0, fy = 0;
		if(playerInput.isPressed(Actions.MOVE_UP)){
			fy += speed;
		}
		if(playerInput.isPressed(Actions.MOVE_DOWN)){
			fy -= speed;
		}
		if(playerInput.isPressed(Actions.MOVE_LEFT)){
			fx -= speed;
		}
		if(playerInput.isPressed(Actions.MOVE_RIGHT)){
			fx += speed;
		}
		
		pComponent.walk(fx, fy, 1);
		
		//if(pComponent.getCollisions().size() > 0){
		//	boxSprite.setColor(Color.BLUE);
		//} else {
		//	boxSprite.setColor(Color.GREEN);
		//}
	}

	@Override
	public void render(SpriteBatch batch) {
		boxSprite.setX(getX());
		boxSprite.setY(getY());
		boxSprite.setRotation(getRotation());

		boxSprite.draw(batch);
	}

	@Override
	public void init() {
		super.init();
		playerInput = getGame().getPlayerInput();
		boxSprite = new Sprite(getResources().circle);
        boxSprite.setColor(Color.CYAN);

		boxSprite.setSize(getWidth(), getHeight());
		boxSprite.setOrigin(getWidth()/2, getHeight()/2);

        pComponent.addCircle(getWidth()/2, 1);
	}
}
