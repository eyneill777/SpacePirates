package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import rustyice.game.physics.RectWallComponent;

public class GlassTile extends Tile {

    public GlassTile() {
        super(true, false);
        setTilePhysics(new RectWallComponent());
    }

    @Override
    public void init() {
        super.init();
        Sprite sprite = new Sprite(getResources().box);
        sprite.setColor(new Color(0, 0.1f, 0.1f, 0.5f));
        setSprite(sprite);
    }
}
