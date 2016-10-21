package rustyice.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import rustyice.core.Core;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.game.Actor;
import rustyice.game.lights.LightComponent;
import rustyice.game.lights.LightContainer;
import rustyice.game.lights.PointLightComponent;
import rustyice.game.physics.PointPhysicsComponent;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;



public class PointLight extends Actor implements LightContainer {
    private transient Sprite icon;
    private PointLightComponent light;
    
    public PointLight() {
        light = new PointLightComponent();
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
