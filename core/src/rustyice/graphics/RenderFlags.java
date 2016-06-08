package rustyice.graphics;

/**
 * @author gabek
 */
public class RenderFlags{
    public static final int
            NORMAL = 0x1,
            LIGHTING = 0x2,
            POV = 0x4,
            EDITOR = 0x8;
    public static final int POST_LIGHT_FLAGS = EDITOR;
}