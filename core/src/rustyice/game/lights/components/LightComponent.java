package rustyice.game.lights.components;

import com.badlogic.gdx.graphics.Color;

public interface LightComponent {
    Color getColor();
    void setColor(Color color);
    
    void setStaticLight(boolean isStatic);
    boolean isStaticLight();

    void setPosition(float x, float y);

    void init();
    void store();
}
