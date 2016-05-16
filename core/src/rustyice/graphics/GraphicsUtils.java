package rustyice.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import rustyice.resources.Resources;

public class GraphicsUtils {

    public static void drawRect(SpriteBatch batch, Resources resources, float x, float y, float width, float height, float thickness) {
        batch.draw(resources.box, x, y, width, thickness);
        batch.draw(resources.box, x, y, thickness, height);
        batch.draw(resources.box, x, y + height - thickness, width, thickness);
        batch.draw(resources.box, x + width - thickness, y, thickness, height);
    }

    public static void drawLine(SpriteBatch batch, Resources resources, float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float rad = (float) (Math.atan2(dy, dx) * MathUtils.radDeg);
        batch.draw(resources.box, x1, y1, 0, 0, dist, thickness, 1, 1, rad);
    }
}
