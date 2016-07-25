package rustyice.game.tiles;

/**
 * @author gabek
 */
public class DoorTile extends Tile{

    public DoorTile(){
        super();

    }

    @Override
    public boolean isConnected(Tile other) {
        return other instanceof DoorTile;
    }
}
