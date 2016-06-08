package rustyice.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;

import java.util.EnumSet;

public interface GameObject {

    void init();

    void store();

    void update(float delta);

    void render(SpriteBatch batch, Camera camera, int flags);

    boolean isInitialized();

    float getX();

    float getY();

    Section getSection();
}
