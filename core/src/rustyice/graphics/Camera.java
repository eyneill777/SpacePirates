package rustyice.graphics;

import com.badlogic.gdx.math.MathUtils;

import rustyice.game.Actor;

/**
 * @author gabek
 */
public class Camera {

    private Actor target;
    private boolean tracking;
    private boolean relative_rotation;
    private float x, y, rot, target_x, target_y, target_rot;
    private float width, height;
    private float halfRenderSize;
    private int flags;

    public Camera() {
        flags = RenderFlags.NORMAL | RenderFlags.LIGHTING;

        target = null;
        tracking = false;
        relative_rotation = false;
    }

    public int getFlags(){
        return flags;
    }

    public void setFlags(int flags){
        this.flags = flags;
    }

    public boolean checkFlag(int flag){
        return (flags & flag) == flag;
    }

    public boolean checkAnyFlag(int flag){
        return (flags & flag) != 0;
    }

    public void enableFlag(int flag){
        flags |= flag;
    }

    public void disableFlag(int flag){
        flags &= ~flag;
    }

    public void toggleFlag(int flag){
        flags ^= flag;
    }

    public Camera(float width, float height){
        flags = RenderFlags.NORMAL | RenderFlags.LIGHTING;


        target = null;
        tracking = false;
        relative_rotation = false;
        this.width = width;
        this.height = height;
    }

    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }

    public Actor getTarget() {
        return target;
    }

    public void update(float delta) {
        if (tracking) {
            if (relative_rotation) {
                rot = target.getRotation() + target_rot;
            } else {
                rot = target_rot;
            }

            x += MathUtils.clamp(target.getX() - x, -.5f, .5f);
            y += MathUtils.clamp(target.getY() - y, -.5f, .5f);
        } else {
            rot = target_rot;
            x = target_x;
            y = target_y;
        }
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTarget(Actor target) {
        this.target = target;
    }

    public void setRelativeRotation(boolean relative) {
        this.relative_rotation = relative;
    }

    public float getRotation() {
        return this.rot;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRotation(float rotation) {
        this.target_rot = rotation;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void apply(com.badlogic.gdx.graphics.Camera ortho) {
        ortho.position.x = this.x;
        ortho.position.y = this.y;
        ortho.viewportWidth = getWidth();
        ortho.viewportHeight = getHeight();

        ortho.up.set(MathUtils.cosDeg(this.rot + 90), MathUtils.sinDeg(this.rot + 90), 0);
        ortho.direction.set(0, 0, -1);
    }

    void setHalfRenderSize(float hsize) {
        this.halfRenderSize = hsize;
    }

    public float getHalfRenderSize() {
        return this.halfRenderSize;
    }
}
