package rustyice.game.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import rustyice.core.Core;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.game.Actor;
import rustyice.game.lights.components.ConeLightComponent;
import rustyice.game.lights.components.LightComponent;
import rustyice.game.lights.components.LightContainer;
import rustyice.game.physics.components.PointPhysicsComponent;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;

/**
 * @author gabek
 */
public class ConeLight extends Actor implements LightContainer{
    private Sprite icon;
    private ConeLightComponent light;

    public ConeLight() {
        light = new ConeLightComponent();
        setPhysicsComponent(new PointPhysicsComponent());
        setSize(1, 1);
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if((flags & RenderFlags.EDITOR) == RenderFlags.EDITOR){
            if(icon == null){
                icon = Core.resources.get("art.atlas", TextureAtlas.class).createSprite("EditorLight");
            }
            icon.setBounds(getX() - getWidth()/2, getY() - getHeight()/2, getWidth(), getHeight());
            icon.setOriginCenter();
            icon.setRotation(getRotation());
            icon.setColor(Color.YELLOW);
            icon.draw(batch);
        }
    }

    @Override
    public void init() {
        super.init();
        light.init(this);
        light.setPosition(getX(), getY());
        light.setDirection(getRotation());
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
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        light.setDirection(rotation);
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
