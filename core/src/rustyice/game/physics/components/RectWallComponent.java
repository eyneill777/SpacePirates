package rustyice.game.physics.components;

import com.badlogic.gdx.physics.box2d.*;
import rustyice.game.physics.Collision;
import rustyice.game.physics.FillterFlags;
import rustyice.game.tiles.Tile;
import rustyice.game.tiles.TileMap;

/**
 * @author gabek
 */
public class RectWallComponent implements PhysicsComponent {
    private static final int FIXTURES_SIZE = 10;
    private static final int N = 0, S = 1, E = 2, W = 3, POVN = 4, POVS = 5, POVE = 6, POVW = 7, POVNS = 8, POVEW = 9;

    private Tile master;
    private boolean opaque = true;
    private transient Fixture fixtures[];
    private transient boolean initialized = false;

    @Deprecated
    public RectWallComponent() {
    }

    public RectWallComponent(Tile master) {
        this.master = master;
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
    public void setX(float x) {
    }

    @Override
    public void setY(float y) {
    }

    @Override
    public void setRotation(float rotation) {
    }

    @Override
    public void setPosition(float x, float y) {
    }

    @Override
    public void init() {
        initialized = true;

        TileMap map = master.getTileMap();
        fixtures = new Fixture[FIXTURES_SIZE];

        int x = master.getTileX();
        int y = master.getTileY();

        Tile other = map.getTile(x, y + 1);
        boolean n = other != null && master.isConnected(other);

        other = map.getTile(x, y - 1);
        boolean s = other != null && master.isConnected(other);

        other = map.getTile(x + 1, y);
        boolean e = other != null && master.isConnected(other);

        other = map.getTile(x - 1, y);
        boolean w = other != null && master.isConnected(other);


        other = map.getTile(x + 1, y + 1);
        boolean ne = other != null && master.isConnected(other);

        other = map.getTile(x - 1, y - 1);
        boolean sw = other != null && master.isConnected(other);

        other = map.getTile(x + 1, y - 1);
        boolean se = other != null && master.isConnected(other);

        other = map.getTile(x - 1, y + 1);
        boolean nw = other != null && master.isConnected(other);

        FixtureDef fixterDef = new FixtureDef();
        EdgeShape edge = new EdgeShape();
        fixterDef.shape = edge;
        fixterDef.filter.categoryBits = FillterFlags.WALL | FillterFlags.OPAQUE;
        fixterDef.filter.maskBits = FillterFlags.LARGE | FillterFlags.SMALL | FillterFlags.LIGHT;

        float x1 = TileMap.TILE_SIZE * x;
        float y1 = TileMap.TILE_SIZE * y;
        float x2 = x1 + TileMap.TILE_SIZE;
        float y2 = y1 + TileMap.TILE_SIZE;
        float centreX = x1 + TileMap.TILE_SIZE/2;
        float centreY = y1 + TileMap.TILE_SIZE/2;

        if (!n) {
            edge.set(x1, y2, x2, y2);
            if (w) {
                edge.setHasVertex0(true);
                edge.setVertex0(x1 - TileMap.TILE_SIZE, y2);
            }
            if (e) {
                edge.setHasVertex3(true);
                edge.setVertex3(x2 + TileMap.TILE_SIZE, y2);
            }
            fixtures[N] = map.getBody().createFixture(fixterDef);
            fixtures[N].setUserData(master);
        }

        if (!s) {
            edge.set(x2, y1, x1, y1);
            if (e) {
                edge.setHasVertex0(true);
                edge.setVertex0(x2 + TileMap.TILE_SIZE, y1);
            }
            if (w) {
                edge.setHasVertex3(true);
                edge.setVertex3(x1 - TileMap.TILE_SIZE, y1);
            }
            fixtures[S] = map.getBody().createFixture(fixterDef);
            fixtures[S].setUserData(master);
        }

        if (!e) {
            edge.set(x2, y2, x2, y1);
            if (n) {
                edge.setHasVertex0(true);
                edge.setVertex0(x2, y2 + TileMap.TILE_SIZE);
            }
            if (s) {
                edge.setHasVertex3(true);
                edge.setVertex3(x2, y1 - TileMap.TILE_SIZE);
            }
            fixtures[E] = map.getBody().createFixture(fixterDef);
            fixtures[E].setUserData(master);
        }

        if (!w) {
            edge.set(x1, y1, x1, y2);
            if (s) {
                edge.setHasVertex0(true);
                edge.setVertex0(x1, y1 - TileMap.TILE_SIZE);
            }
            if (n) {
                edge.setHasVertex3(true);
                edge.setVertex3(x1, y2 + TileMap.TILE_SIZE);
            }
            fixtures[W] = map.getBody().createFixture(fixterDef);
            fixtures[W].setUserData(master);
        }

        //pov
        if(opaque){
            fixterDef.filter.categoryBits = FillterFlags.WALL;
            fixterDef.filter.maskBits = FillterFlags.CAMERA_POV;
            edge.setHasVertex0(false);
            edge.setHasVertex3(false);


            if(n && s && (!w || !e)){
                edge.set(centreX, y1, centreX, y2);
                fixtures[POVNS] = map.getBody().createFixture(fixterDef);
                fixtures[POVNS].setUserData(master);
            } else if(n && (!ne || !nw || !w || !e)){
                edge.set(centreX, centreY, centreX, y2);
                fixtures[POVN] = map.getBody().createFixture(fixterDef);
                fixtures[POVN].setUserData(master);
            } else if(s && (!se || !sw || !w || !e)){
                edge.set(centreX, y1, centreX, centreY);
                fixtures[POVS] = map.getBody().createFixture(fixterDef);
                fixtures[POVS].setUserData(master);
            }


            if(e && w && (!n || !s)){
                edge.set(x1, centreY, x2, centreY);
                fixtures[POVEW] = map.getBody().createFixture(fixterDef);
                fixtures[POVEW].setUserData(master);
            } else if(e && (!ne || !se || !n || !s)){
                edge.set(centreX, centreY, x2, centreY);
                fixtures[POVE] = map.getBody().createFixture(fixterDef);
                fixtures[POVE].setUserData(master);
            } else if(w && (!nw || !sw || !n || !s)){
                edge.set(x1, centreY, centreX, centreY);
                fixtures[POVW] = map.getBody().createFixture(fixterDef);
                fixtures[POVW].setUserData(master);
            }
        }

        edge.dispose();
    }

    @Override
    public void store() {
        initialized = false;
        Body body = master.getTileMap().getBody();

        for(int i = 0; i < FIXTURES_SIZE; i++){
            if(fixtures[i] != null){
                body.destroyFixture(fixtures[i]);
            }
        }
        fixtures = null;
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
}
