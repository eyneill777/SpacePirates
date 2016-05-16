package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FloorTile extends Tile {

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
        batch.draw(getResources().box, +getX() * TileMap.TILE_SIZE, +getY() * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof FloorTile;
    }
}
