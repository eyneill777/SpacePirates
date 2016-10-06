package rustyice.game.tiles;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author gabek
 */
public enum TileDirection {
    EAST(0), NORTH(90), WEST(180), SOUTH(270);

    private float degrees;
    private float rad;
    TileDirection(float degrees){
        this.degrees = degrees;
        rad = MathUtils.degRad * degrees;
    }

    public float getDegrees() {
        return degrees;
    }

    public float getRad() {
        return rad;
    }
}
