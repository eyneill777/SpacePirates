package rustyice.game.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.esotericsoftware.minlog.Log;

import rustyice.game.Section;
import rustyice.graphics.Camera;

public class TileMap {

    public static final float TILE_SIZE = 1;

    private transient boolean initialized = false;
    private transient Body body;

    private Tile[] tileMap;
    private Section section;

    private int mapW, mapH;
    private int offsetX, offsetY;

    public TileMap() {
        this.tileMap = new Tile[0];
    }

    public void setTile(Tile tile, int x, int y) {
    	int minX, minY, w, h;
        if(x < offsetX){
        	minX = x;
        	w = mapW + offsetX - x;
        } else {
        	minX = offsetX;
        	w = mapW;
        }
        
        if(y < offsetY){
        	minY = y;
        	h = mapH + offsetY - y;
        } else {
        	minY = offsetY;
        	h = mapH;
        }
        w = x - minX + 1 > w ? x - minX + 1 : w;
        h = y - minY + 1 > h ? y - minY + 1 : h;

        if (x < offsetX || y < offsetY || w > mapW || h > mapH) {
            resize(minX, minY, w, h);
        }

        int index = getIndex(x, y);

        if (this.initialized) {
            Tile oldTile = this.tileMap[index];
            if (oldTile != null) {
                oldTile.store();
            }
        }

        tile.tiles = this;
        tile.setPosition(x, y);
        this.tileMap[index] = tile;

        if (this.initialized) {
            tile.init();
            refreshArea(x, y);
        }
    }

    private void refreshArea(int x, int y){
        refreahTile(x-1, y);
        refreahTile(x+1, y);
        refreahTile(x, y-1);
        refreahTile(x, y+1);

        refreahTile(x-1, y-1);
        refreahTile(x+1, y+1);
        refreahTile(x+1, y-1);
        refreahTile(x-1, y+1);
    }

    private void refreahTile(int x, int y){
        Tile tile = getTile(x, y);
        if(tile != null){
            tile.store();
            tile.init();
        }
    }
    
    private void resize(int minX, int minY, int w, int h) {
    	Log.debug(String.format("resize %d %d %d %d", minX, minY, w, h));
        Tile map[] = new Tile[w * h];

        for (int i = 0; i < mapH; i++) {
            for (int j = 0; j < mapW; j++) {
                int oldIndex = i * mapW + j;
                int newIndex = (i + offsetY - minY) * w + (j + offsetX - minX);
                map[newIndex] = tileMap[oldIndex];
            }
        }

        offsetX = minX;
        offsetY = minY;
        mapW = w;
        mapH = h;

        tileMap = map;
    }

    public Tile getTile(int x, int y) {
        if (x >= this.offsetX && y >= this.offsetY && x < this.mapW + this.offsetX && y < this.mapH + this.offsetY) {
            return this.tileMap[getIndex(x, y)];
        } else {
            return null;
        }
    }

    public Tile getTileAt(float x, float y){
        return getTile((int) (x / TILE_SIZE) - (x<0?1:0),
                       (int) (y / TILE_SIZE) - (y<0?1:0));
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public Body getBody() {
        return this.body;
    }

    public void render(SpriteBatch batch, Camera camera, int flags) {
        int startX = (int) ((camera.getX() - camera.getHalfRenderSize()) / TILE_SIZE) - 1;
        int startY = (int) ((camera.getY() - camera.getHalfRenderSize()) / TILE_SIZE) - 1;
        int endX = (int) ((camera.getX() + camera.getHalfRenderSize()) / TILE_SIZE) + 1;
        int endY = (int) ((camera.getY() + camera.getHalfRenderSize()) / TILE_SIZE) + 1;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                Tile tile = getTile(j, i);
                if (tile != null) {
                    tile.render(batch, camera, flags);
                }
            }
        }

    }

    public void update(float delta) {
        for (Tile tile : this.tileMap) {
           if (tile != null) {
                tile.update(delta);
            }
        }
    }

    public void init() {
        this.initialized = true;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;

        this.body = this.section.getWorld().createBody(bodyDef);

        for (Tile tile : this.tileMap) {
            if (tile != null) {
                tile.init();
            }
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void store() {
        this.initialized = false;

        for (Tile tile : tileMap) {
            if (tile != null) {
                tile.store();
            }
        }

        section.getWorld().destroyBody(this.body);
    }

    private int getIndex(int x, int y) {
        return (y - this.offsetY) * this.mapW + (x - this.offsetX);
    }
}
