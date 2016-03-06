package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import spacepirates.game.Game;
import spacepirates.game.level.Room;

/**
 * @author gabek
 */
public class DoorTile extends Tile{
    private Room targetRoom;

    public DoorTile(Room targetRoom){
        super();
        setSolid(true);
        this.targetRoom = targetRoom;
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

    /*
    @Override
    public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
        if(((Collidable)otherFixture.getBody().getUserData()).getMaster() instanceof Player){
            getGame().setRoomToLoad(targetRoom);

            Player player = (Player) ((Collidable)otherFixture.getBody().getUserData()).getMaster();

            player.store();
            getGame().removeActor(player);

            player.setPosition(0, 0);
            targetRoom.getActors().add(player);
        }
    }
    */

    @Override
    public void update(float delta) {

    }
}
