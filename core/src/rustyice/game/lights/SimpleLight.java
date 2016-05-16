package rustyice.game.lights;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import rustyice.game.actors.Actor;
import rustyice.game.physics.components.PointPhysicsComponent;

public class SimpleLight extends Actor {

    private transient PointLight pointLight;

    private float lightRes = 0.25f;
    private float distance;
    private Color color;

    public SimpleLight() {
        setPhysicsComponent(new PointPhysicsComponent(7, 7));
        this.distance = 10;
        this.color = Color.CYAN;
    }

    public void setColor(Color color) {
        this.color = color;
        if (isInitialized()) {
            this.pointLight.setColor(color);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
    }

    @Override
    public void init() {
        super.init();
        this.pointLight = new PointLight(getSection().getRayHandler(), getRayNum(this.distance, this.lightRes));
        this.pointLight.setColor(this.color);
        this.pointLight.setDistance(this.distance);
        this.pointLight.setPosition(getX(), getY());
        // pointLight.setStaticLight(true);
    }

    public int getRayNum(float r, float space) {
        return (int) ((MathUtils.PI2 * r) / space);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isInitialized() && this.pointLight.getX() != getX() || this.pointLight.getY() != getY()) {
            this.pointLight.setPosition(getX(), getY());
        }
    }

    @Override
    public void store() {
        super.store();
        this.pointLight.remove(true);
    }
}
