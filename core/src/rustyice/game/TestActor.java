package rustyice.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import rustyice.core.Core;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.game.physics.FillterFlags;
import rustyice.game.physics.components.SBPhysicsComponent;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;

public class TestActor extends Actor {

    private transient Sprite testSprite;
    private SBPhysicsComponent pComponent;

    public TestActor() {
        super();
        setPhysicsComponent(pComponent = new SBPhysicsComponent());
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
    @ComponentAccess
    public SBPhysicsComponent getPhysicsComponent() {
        return this.pComponent;
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if((flags & RenderFlags.NORMAL) == RenderFlags.NORMAL){
            testSprite.setX(getX() - getWidth() / 2);
            testSprite.setY(getY() - getHeight() / 2);
            testSprite.setRotation(getRotation());
            testSprite.draw(batch);
        }
    }

    @Override
    public void init() {
        super.init();

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits = FillterFlags.LARGE | FillterFlags.OPAQUE;
        fixtureDef.filter.maskBits = FillterFlags.LARGE | FillterFlags.WALL | FillterFlags.LIGHT;
        pComponent.addRectangle(getWidth(), getHeight(), fixtureDef);


        testSprite = new Sprite(Core.resources.box);
        testSprite.setSize(getWidth(), getHeight());
        testSprite.setOrigin(getWidth() / 2, getHeight() / 2);
    }
}
