package rustyice.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rustyice.game.physics.Collision;
import rustyice.game.physics.SBPhysicsComponent;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;

public class Player extends Actor{
	private SBPhysicsComponent pComponent;
	private PlayerInput playerInput;
	private Sprite boxSprite;
	private float speed = 6;
	private int count = 0;

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
		
		if(count > 0){
			boxSprite.setColor(Color.BLUE);
		} else {
			boxSprite.setColor(Color.GREEN);
		}
	}

    @Override
    public void beginCollision(Collision collision) {
        super.beginCollision(collision);
        count++;
    }

    @Override
    public void endCollision(Collision collision) {
        super.endCollision(collision);
        count--;
    }

    @Override
	public void render(SpriteBatch batch) {
		boxSprite.setX(getX() - getWidth()/2);
		boxSprite.setY(getY() - getHeight()/2);
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
