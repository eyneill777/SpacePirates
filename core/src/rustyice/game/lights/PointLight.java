package rustyice.game.lights;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.game.actors.Actor;
import rustyice.game.lights.components.LightComponent;
import rustyice.game.lights.components.LightContainer;
import rustyice.game.lights.components.PointLightComponent;
import rustyice.game.physics.components.PointPhysicsComponent;


public class PointLight extends Actor implements LightContainer {

    private PointLightComponent light;
    
    public PointLight() {
        light = new PointLightComponent(this, 10);
        setPhysicsComponent(new PointPhysicsComponent());
        setSize(1, 1);
    }

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void init() {
        super.init();
        light.init();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }
    
    @Override
    public void setX(float x) {
        super.setX(x);
        light.setPosition(x, getY());
    }
    
    @Override
    public void setY(float y) {
        super.setY(y);
        light.setPosition(getX(), y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        light.setPosition(x, y);
    }
    
    @Override
    public void store() {
        super.store();
        light.store();
    }

    @Override
    @ComponentAccess
    public LightComponent getLightComponent() {
        return light;
    }
}
