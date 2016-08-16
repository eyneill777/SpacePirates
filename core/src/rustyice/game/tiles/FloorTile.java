package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;

public class FloorTile extends Tile {

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if((flags & RenderFlags.NORMAL) == RenderFlags.NORMAL){
            batch.setColor(Color.WHITE);
            batch.draw(getResources().box, getX(), getY(), TileMap.TILE_SIZE, TileMap.TILE_SIZE);
        }
    }

    @Override
    public void init() {
        super.init();
    }
}
