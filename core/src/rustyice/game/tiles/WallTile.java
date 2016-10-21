package rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rustyice.editor.annotations.ComponentProperty;
import rustyice.physics.Collision;
import rustyice.game.physics.RectWallComponent;
import rustyice.graphics.Camera;

public class WallTile extends Tile {
    private Color color = Color.BLUE;

    public WallTile() {
        super(true, true);
        RectWallComponent wallComp = new RectWallComponent();
        setTilePhysics(wallComp);
    }

    @Override
    public void init() {
        super.init();
        Sprite sprite = new Sprite(getResources().box);
        sprite.setColor(color);
        setSprite(sprite);
    }

    @ComponentProperty(title = "Color")
    public Color getColor(){
        return color;
    }

    @ComponentProperty(title = "Color")
    public void setColor(Color color){
        this.color = color;
        if(isInitialized()){
            getSprite().setColor(color);
        }
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        super.render(batch, camera, flags);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void beginCollision(Collision collision) {
        super.beginCollision(collision);

    }

    @Override
    public void endCollision(Collision collision) {
        super.endCollision(collision);

    }
}
