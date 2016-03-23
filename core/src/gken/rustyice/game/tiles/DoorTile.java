package gken.rustyice.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import gken.rustyice.game.physics.Collision;
import gken.rustyice.game.physics.RectWallComponent;
import gken.rustyice.game.Player;
import gken.rustyice.game.level.Section;

/**
 * @author gabek
 */
public class DoorTile extends Tile{
    private Section targetSection;
    private Player player;

    public DoorTile(Section targetSection){
        super();
        this.targetSection = targetSection;
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
            getGame().setSectionToLoad(targetSection);

            getGame().removeActor(player);



            player.setPosition(7, 7);
            targetSection.getActors().add(player);
            player = null;
        }
    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof BoundaryTile || other instanceof DoorTile;
    }
}
