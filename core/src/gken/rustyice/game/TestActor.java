package gken.rustyice.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gken.rustyice.game.physics.SBPhysicsComponent;

public class TestActor extends Actor{

	private Sprite testSprite;
	private SBPhysicsComponent pComponent;

	public TestActor() {
		super();
		setPhysicsComponent(pComponent = new SBPhysicsComponent(this));
		testSprite = new Sprite();
		
		setPosition(5, 5);
		setWidth(1.9f);
		setHeight(1.9f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		//walk(0,1, .95f);
	}

    @Override
    public SBPhysicsComponent getPhysicsComponent(){
        return pComponent;
    }

	@Override
	public void render(SpriteBatch batch) {
		testSprite.setX(getX() - getWidth()/2);
		testSprite.setY(getY() - getHeight()/2);
		testSprite.setRotation(getRotation());
		testSprite.draw(batch);
	}

	@Override
	public void init() {
		super.init();
        pComponent.addRectangle(getWidth(), getHeight(), 2);
		testSprite = getResources().gameArt.createSprite("may2015-3");
		testSprite.setSize(getWidth(), getHeight());
		testSprite.setOrigin(getWidth()/2, getHeight()/2);
	}
}