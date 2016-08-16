package rustyice.game.physics.components;

import com.badlogic.gdx.physics.box2d.*;
import rustyice.game.GameObject;
import rustyice.game.Section;
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
    private transient Fixture fixtures[] = new Fixture[FIXTURES_SIZE];
    private transient boolean initialized = false;

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

    private boolean checkTileSolid(int rx, int ry){
        Tile other = master.getTileMap().getTile(master.getTileX() + rx, master.getTileY() + ry);
        return other != null && other.isSolid() && (!master.isOpaque() || other.isOpaque());
    }

    private boolean checkTileOpaque(int rx, int ry){
        Tile other = master.getTileMap().getTile(master.getTileX() + rx, master.getTileY() + ry);
        return other != null && other.isOpaque();
    }

    private void initSolid(){
        boolean n = checkTileSolid(0, 1),
                s = checkTileSolid(0, -1),
                e = checkTileSolid(1, 0),
                w = checkTileSolid(-1, 0);

        if(!n || !s || !e || !w) {
            TileMap map = master.getTileMap();

            FixtureDef fixtureDef = new FixtureDef();
            EdgeShape edge = new EdgeShape();
            fixtureDef.shape = edge;
            fixtureDef.filter.categoryBits = FillterFlags.WALL;
            fixtureDef.filter.maskBits = FillterFlags.LARGE | FillterFlags.SMALL;

            if(master.isOpaque()){
                fixtureDef.filter.categoryBits |= FillterFlags.OPAQUE;
                fixtureDef.filter.maskBits |= FillterFlags.LIGHT;
            }

            float x1 = TileMap.TILE_SIZE * master.getTileX();
            float y1 = TileMap.TILE_SIZE * master.getTileY();
            float x2 = x1 + TileMap.TILE_SIZE;
            float y2 = y1 + TileMap.TILE_SIZE;

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
                fixtures[N] = map.getBody().createFixture(fixtureDef);
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
                fixtures[S] = map.getBody().createFixture(fixtureDef);
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
                fixtures[E] = map.getBody().createFixture(fixtureDef);
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
                fixtures[W] = map.getBody().createFixture(fixtureDef);
                fixtures[W].setUserData(master);
            }

            edge.dispose();
        }
    }

    private void initOpaque(){
        boolean n = checkTileOpaque(0, 1),
                s = checkTileOpaque(0, -1),
                e = checkTileOpaque(1, 0),
                w = checkTileOpaque(-1, 0),
                ne = checkTileOpaque(1, 1),
                sw = checkTileOpaque(-1, -1),
                se = checkTileOpaque(1, -1),
                nw = checkTileOpaque(-1, 1);


        if(!n || !s || !e || !w || !ne || !nw || !se || !sw) {
            TileMap map = master.getTileMap();
            float x1 = TileMap.TILE_SIZE * master.getTileX();
            float y1 = TileMap.TILE_SIZE * master.getTileY();
            float x2 = x1 + TileMap.TILE_SIZE;
            float y2 = y1 + TileMap.TILE_SIZE;
            float centreX = x1 + TileMap.TILE_SIZE / 2;
            float centreY = y1 + TileMap.TILE_SIZE / 2;

            FixtureDef fixtureDef = new FixtureDef();
            EdgeShape edge = new EdgeShape();

            fixtureDef.filter.categoryBits = FillterFlags.WALL;
            fixtureDef.filter.maskBits = FillterFlags.CAMERA_POV;
            fixtureDef.shape = edge;


            if(n && s){
                edge.set(centreX, y1, centreX, y2);
                fixtures[POVNS] = map.getBody().createFixture(fixtureDef);
                fixtures[POVNS].setUserData(master);
            } else if(n && (!ne || !nw || !w || !e)){
                edge.set(centreX, centreY, centreX, y2);
                fixtures[POVN] = map.getBody().createFixture(fixtureDef);
                fixtures[POVN].setUserData(master);
            } else if(s && (!se || !sw || !w || !e)){
                edge.set(centreX, y1, centreX, centreY);
                fixtures[POVS] = map.getBody().createFixture(fixtureDef);
                fixtures[POVS].setUserData(master);
            }


            if(e && w){
                edge.set(x1, centreY, x2, centreY);
                fixtures[POVEW] = map.getBody().createFixture(fixtureDef);
                fixtures[POVEW].setUserData(master);
            } else if(e && (!ne || !se || !n || !s)){
                edge.set(centreX, centreY, x2, centreY);
                fixtures[POVE] = map.getBody().createFixture(fixtureDef);
                fixtures[POVE].setUserData(master);
            } else if(w && (!nw || !sw || !n || !s)){
                edge.set(x1, centreY, centreX, centreY);
                fixtures[POVW] = map.getBody().createFixture(fixtureDef);
                fixtures[POVW].setUserData(master);
            }


            edge.dispose();
        }
    }

    @Override
    public void init(GameObject parent) {
        master = (Tile) parent;
        if(!initialized){
            initialized = true;

            if(master.isSolid()){
                initSolid();
            }

            if(master.isOpaque()){
                initOpaque();
            }
        }
    }

    @Override
    public void store() {
        if(initialized){
            initialized = false;

            Body body = master.getTileMap().getBody();
            for(int i = 0; i < FIXTURES_SIZE; i++) {
                if (fixtures[i] != null) {
                    body.destroyFixture(fixtures[i]);
                    fixtures[i] = null;
                }
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
}
