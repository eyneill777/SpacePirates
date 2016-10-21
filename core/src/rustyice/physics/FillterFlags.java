package rustyice.physics;

/**
 * @author gabek
 */
public class FillterFlags {
    public static final short
            OPAQUE = 0x1,
            LIGHT = 0x2,
            CAMERA_POV = 0x4,
            ACTIVATABLE = 0x8,
            ACTIVATOR = 0x10,
            LARGE = 0x20,
            SMALL = 0x40,
            WALL = 0x80;
}
