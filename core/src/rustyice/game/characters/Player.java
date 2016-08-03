package rustyice.game.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rustyice.core.Core;
import rustyice.game.Actor;
import rustyice.game.characters.components.CharacterPhysics;
import rustyice.game.physics.Collision;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;
import rustyice.input.Actions;
import rustyice.input.PlayerInput;

public class Player extends Actor {

    private CharacterPhysics pComponent;
    private transient PlayerInput playerInput;
    private transient Sprite boxSprite;

    private float speed = 6;
    private transient int count = 0;

    public Player() {
        setPhysicsComponent(pComponent = new CharacterPhysics());
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

        pComponent.walk(fx, fy, 1);

        if (count > 0) {
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
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if((flags & RenderFlags.NORMAL) == RenderFlags.NORMAL){
            boxSprite.setX(getX() - getWidth() / 2);
            boxSprite.setY(getY() - getHeight() / 2);
            boxSprite.setRotation(getRotation());

            boxSprite.draw(batch);
        }
    }

    public void setPlayerInput(PlayerInput input) {
        this.playerInput = input;
    }

    @Override
    public void init() {
        super.init();
        boxSprite = new Sprite(Core.resources.circle);
        boxSprite.setColor(Color.CYAN);

        boxSprite.setSize(getWidth(), getHeight());
        boxSprite.setOrigin(getWidth() / 2, getHeight() / 2);
    }
}
