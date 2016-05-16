package rustyice.graphics;

import com.badlogic.gdx.math.MathUtils;

import rustyice.game.actors.Actor;

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

    public Camera() {
        this.target = null;
        this.tracking = false;
        this.relative_rotation = false;
    }

    public Actor getTarget() {
        return this.target;
    }

    public void update(float delta) {
        if (this.tracking) {
            if (this.relative_rotation) {
                this.rot = this.target.getRotation() + this.target_rot;
            } else {
                this.rot = this.target_rot;
            }

            this.x += MathUtils.clamp(this.target.getX() - this.x, -.5f, .5f);
            this.y += MathUtils.clamp(this.target.getY() - this.y, -.5f, .5f);
        } else {
            this.rot = this.target_rot;
            this.x = this.target_x;
            this.y = this.target_y;
        }
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isTracking() {
        return this.tracking;
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