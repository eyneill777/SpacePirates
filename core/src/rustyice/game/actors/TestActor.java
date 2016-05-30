package rustyice.game.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import rustyice.game.physics.components.SBPhysicsComponent;

public class TestActor extends Actor {

    private transient Sprite testSprite;
    private SBPhysicsComponent pComponent;

    public TestActor() {
        super();
        setPhysicsComponent(pComponent = new SBPhysicsComponent(this));
        //pComponent.setFlying(true);
        testSprite = new Sprite();

        setPosition(5, 5);
        setWidth(1.9f);
        setHeight(1.9f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // walk(0,1, .95f);
    }

    @Override
    public SBPhysicsComponent getPhysicsComponent() {
        return this.pComponent;
    }

    @Override
    public void render(SpriteBatch batch) {
        this.testSprite.setX(getX() - getWidth() / 2);
        this.testSprite.setY(getY() - getHeight() / 2);
        this.testSprite.setRotation(getRotation());
        this.testSprite.draw(batch);
    }

    @Override
    public void init() {
        super.init();
        this.pComponent.addRectangle(getWidth(), getHeight(), 2, false);
        this.testSprite = getResources().get("art.atlas", TextureAtlas.class).createSprite("may2015-3");
        this.testSprite.setSize(getWidth(), getHeight());
        this.testSprite.setOrigin(getWidth() / 2, getHeight() / 2);
    }
}
