package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import rustyice.game.physics.components.RectWallComponent;

public class BoundaryTile extends Tile {

    public BoundaryTile() {
        super();
        setSolid(true);
        setTilePhysics(new RectWallComponent(this));
    }

    @Override
    public void init() {
        super.init();
        Sprite sprite = new Sprite(getResources().box);
        sprite.setColor(Color.YELLOW);
        setSprite(sprite);
    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof BoundaryTile;
    }
}
