package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rustyice.game.characters.Player;
import rustyice.game.physics.Collision;
import rustyice.game.physics.components.RectWallComponent;
import rustyice.graphics.Camera;

public class WallTile extends Tile {

    private int count = 0;

    public WallTile() {
        super();
        RectWallComponent wallComp = new RectWallComponent(this);
        setTilePhysics(wallComp);

    }

    @Override
    public void init() {
        super.init();
        Sprite sprite = new Sprite(getResources().box);
        sprite.setColor(Color.BLUE);
        setSprite(sprite);
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if (this.count == 0) {
            getSprite().setColor(Color.BLUE);
        } else {
            getSprite().setColor(Color.GRAY);
        }
        super.render(batch, camera, flags);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void beginCollision(Collision collision) {
        super.beginCollision(collision);
        if (collision.getOther() instanceof Player) {
            this.count++;
        }
    }

    @Override
    public void endCollision(Collision collision) {
        super.endCollision(collision);
        if (collision.getOther() instanceof Player) {
            this.count--;
        }
    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof WallTile || other instanceof BoundaryTile;
    }
}
