package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import spacepirates.game.Game;
import spacepirates.game.Player;
import spacepirates.game.level.Room;
import spacepirates.game.physics.Collidable;
import spacepirates.game.physics.Collision;
import spacepirates.game.physics.RectWallComponent;

/**
 * @author gabek
 */
public class DoorTile extends Tile{
    private Room targetRoom;
    private Player player;

    public DoorTile(Room targetRoom){
        super();
        this.targetRoom = targetRoom;
        setSolid(true);
        setTilePhysics(new RectWallComponent(this));
    }

    public void init(){
        super.init();
        Sprite sprite = new Sprite(getGame().getResources().box);
        sprite.setColor(Color.ORANGE);
        setSprite(sprite);
    }

    @Override
    public FixtureDef buildFixtureDef(){
        FixtureDef fixtureDef = super.buildFixtureDef();

        fixtureDef.filter.categoryBits = Game.CAT_BOUNDARY | Game.CAT_TILE;

        return fixtureDef;
    }


    @Override
    public void beginCollision(Collision collision) {
        super.beginCollision(collision);
        if(collision.getOther() instanceof Player){
            player = (Player) collision.getOther();
        }
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if(player != null){
            getGame().setRoomToLoad(targetRoom);

            getGame().removeActor(player);



            player.setPosition(7 + targetRoom.getXOffset(), 7 + targetRoom.getYOffset());
            targetRoom.getActors().add(player);
            player = null;
        }
    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof BoundaryTile || other instanceof DoorTile;
    }
}
