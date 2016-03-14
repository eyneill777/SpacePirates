package spacepirates.game.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import spacepirates.game.level.Direction;
import spacepirates.game.tiles.Tile;
import spacepirates.game.tiles.TileMap;

/**
 * @author gabek
 */
public class RectWallComponent implements PhysicsComponent {
    private Tile master;
    private Fixture[] fixtures;
    private boolean initialized = false;

    public RectWallComponent(Tile master){
        this.master = master;
        fixtures = new Fixture[4];
    }

    @Override
    public float getX() {
        return master.getX();
    }

    @Override
    public float getY() {
        return master.getY();
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public void setX(float x) {}

    @Override
    public void setY(float y) {}

    @Override
    public void setRotation(float rotation) {}

    @Override
    public void setPosition(float x, float y) {}

    @Override
    public void init() {
        initialized = true;

        TileMap map = master.getTileMap();

        fixtures = new Fixture[4];

        FixtureDef fixterDef = new FixtureDef();
        EdgeShape edge = new EdgeShape();
        fixterDef.shape = edge;

        int x = master.getTileX();
        int y = master.getTileY();

        Tile other = map.getTile(x, y + 1);
        boolean n = other == null || other.isSolid();

        other = map.getTile(x, y - 1);
        boolean s = other == null || other.isSolid();

        other = map.getTile(x + 1, y);
        boolean e = other == null || other.isSolid();

        other = map.getTile(x - 1, y);
        boolean w = other == null || other.isSolid();

        float x1 = TileMap.TILE_SIZE * x;
        float y1 = TileMap.TILE_SIZE * y;
        float x2 = x1 + TileMap.TILE_SIZE;
        float y2 = y1 + TileMap.TILE_SIZE;

        if(!n){
            edge.set(x1, y2, x2, y2);
            if(w){
                edge.setHasVertex0(true);
                //edge.setVertex0(x1 - TileMap.TILE_SIZE, y2);
            }
            if(e){
                edge.setHasVertex3(true);
                //edge.setVertex3(x2 + TileMap.TILE_SIZE, y2);
            }
            fixtures[Direction.N] = map.getBody().createFixture(fixterDef);
            fixtures[Direction.N].setUserData(master);
        }

        if(!s){
            edge.set(x2, y1, x1, y1);
            if(e){
                edge.setHasVertex0(true);
                edge.setVertex0(x2 + TileMap.TILE_SIZE, y1);
            }
            if(w){
                edge.setHasVertex3(true);
                edge.setVertex3(x1 - TileMap.TILE_SIZE, y1);
            }
            fixtures[Direction.S] = map.getBody().createFixture(fixterDef);
            fixtures[Direction.S].setUserData(master);
        }

        if(!e){
            edge.set(x2, y2, x2, y1);
            if(n){
                edge.setHasVertex0(true);
                edge.setVertex0(x2, y2 + TileMap.TILE_SIZE);
            }
            if(s){
                edge.setHasVertex3(true);
                edge.setVertex3(x2, y1 - TileMap.TILE_SIZE);
            }
            fixtures[Direction.E] = map.getBody().createFixture(fixterDef);
            fixtures[Direction.E].setUserData(master);
        }

        if(!w){
            edge.set(x1, y1, x1, y2);
            if(s){
                edge.setHasVertex0(true);
                edge.setVertex0(x1, y1 - TileMap.TILE_SIZE);
            }
            if(n){
                edge.setHasVertex3(true);
                edge.setVertex3(x1, y2 + TileMap.TILE_SIZE);
            }
            fixtures[Direction.W] = map.getBody().createFixture(fixterDef);
            fixtures[Direction.W].setUserData(master);
        }

        edge.dispose();
    }

    @Override
    public void store() {
        initialized = false;
        Body body = master.getTileMap().getBody();
        for (Fixture fixture : fixtures) {
            if (fixture != null) {
                body.destroyFixture(fixture);
            }
        }
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void beginCollision(Collision collision) {

    }

    @Override
    public void endCollision(Collision collision) {

    }

    @Override
    public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
        return true;
    }
}
