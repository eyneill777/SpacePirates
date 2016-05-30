package rustyice.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rustyice.game.physics.Collision;
import rustyice.game.physics.components.SBPhysicsComponent;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;
import rustyice.core.Core;

public class Player extends Actor {

    private SBPhysicsComponent pComponent;
    private transient PlayerInput playerInput;
    private transient Sprite boxSprite;

    private float speed = 6;
    private transient int count = 0;

    public Player() {
        setPhysicsComponent(pComponent = new SBPhysicsComponent(this));
        //pComponent.setFlying(true);
        setWidth(0.98f);
        setHeight(0.98f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float fx = 0, fy = 0;
        if (this.playerInput != null) {
            if (this.playerInput.isPressed(Actions.MOVE_UP)) {
                fy += this.speed;
            }
            if (this.playerInput.isPressed(Actions.MOVE_DOWN)) {
                fy -= this.speed;
            }
            if (this.playerInput.isPressed(Actions.MOVE_LEFT)) {
                fx -= this.speed;
            }
            if (this.playerInput.isPressed(Actions.MOVE_RIGHT)) {
                fx += this.speed;
            }
        }

        this.pComponent.walk(fx, fy, 1);

        if (this.count > 0) {
            this.boxSprite.setColor(Color.BLUE);
        } else {
            this.boxSprite.setColor(Color.GREEN);
        }
    }

    @Override
    public void beginCollision(Collision collision) {
        super.beginCollision(collision);
        this.count++;
    }

    @Override
    public void endCollision(Collision collision) {
        super.endCollision(collision);
        this.count--;
    }

    @Override
    public void render(SpriteBatch batch) {
        this.boxSprite.setX(getX() - getWidth() / 2);
        this.boxSprite.setY(getY() - getHeight() / 2);
        this.boxSprite.setRotation(getRotation());

        this.boxSprite.draw(batch);
    }

    public void setPlayerInput(PlayerInput input) {
        this.playerInput = input;
    }

    @Override
    public void init() {
        super.init();
        boxSprite = new Sprite(Core.resources.box);
        boxSprite.setColor(Color.CYAN);

        boxSprite.setSize(getWidth(), getHeight());
        boxSprite.setOrigin(getWidth() / 2, getHeight() / 2);

        pComponent.addRectangle(getWidth(), getHeight(), 1, false);
    }
}
