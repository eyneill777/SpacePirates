package rustyice.game.physics.components;

import rustyice.game.physics.Collision;

/**
 * Created by gabek
 */
public interface PhysicsComponent {

    float getX();

    float getY();

    float getRotation();

    void setX(float x);

    void setY(float y);

    void setRotation(float rotation);

    void setPosition(float x, float y);

    void init();

    void store();

    void update(float delta);

    void beginCollision(Collision collision);

    void endCollision(Collision collision);
}
