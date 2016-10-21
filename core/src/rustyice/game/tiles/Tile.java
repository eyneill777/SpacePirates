package rustyice.game.tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import rustyice.game.GameObject;
import rustyice.game.Section;
import rustyice.game.physics.Collidable;
import rustyice.game.physics.Collision;
import rustyice.game.physics.components.PhysicsComponent;
import rustyice.graphics.Camera;
import rustyice.graphics.RenderFlags;
import rustyice.resources.Resources;

public abstract class Tile implements GameObject, Collidable {

    TileMap tiles;

    private transient boolean initialized = false;
    private transient Sprite sprite;
    private boolean solid, opaque;
    private PhysicsComponent tilePhysics;
    private int x, y;
    private float rotation;

    public Tile(){}

    public Tile(boolean solid, boolean opaque){
        this.solid = solid;
        this.opaque = opaque;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        sprite.setBounds(getX(), getY(), TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setTilePhysics(PhysicsComponent tilePhysics) {
        this.tilePhysics = tilePhysics;
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getTileX() {
        return x;
    }

    public int getTileY() {
        return y;
    }

    @Override
    public float getX() {
        return x * TileMap.TILE_SIZE;
    }

    @Override
    public float getY() {
        return y * TileMap.TILE_SIZE;
    }

    @Override
    public void setX(float x){

    }

    @Override
    public void setY(float y){

    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation){
        this.rotation = MathUtils.round(rotation / 90) * 90;
    }

    public TileMap getTileMap() {
        return tiles;
    }

    @Override
    public Section getSection() {
        return tiles.getSection();
    }

    public Resources getResources() {
        return tiles.getSection().getResources();
    }

    @Override
    public void update(float delta) {
        if (tilePhysics != null) {
            tilePhysics.update(delta);
        }
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, int flags) {
        if (sprite != null && (flags & RenderFlags.NORMAL) == RenderFlags.NORMAL) {
            sprite.draw(batch);
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void init() {
        initialized = true;
        if (tilePhysics != null) {
            tilePhysics.init(this);
        }
        if(sprite != null){
            setSprite(sprite);
        }
    }

    @Override
    public void store() {
        initialized = false;
        if (tilePhysics != null) {
            tilePhysics.store();
        }
    }

    public boolean isSolid(){
        return solid;
    }

    public boolean isOpaque(){
        return opaque;
    }

    @Override
    public void beginCollision(Collision collision) {
        tilePhysics.beginCollision(collision);
    }

    @Override
    public void endCollision(Collision collision) {
        tilePhysics.endCollision(collision);
    }
}
