package rustyice.game.actors;

import rustyice.editor.annotations.ComponentAccess;
import rustyice.game.GameObject;
import rustyice.game.Section;
import rustyice.game.physics.Collidable;
import rustyice.game.physics.Collision;
import rustyice.game.physics.components.PhysicsComponent;
import rustyice.resources.Resources;

public abstract class Actor implements GameObject, Collidable {

    private Section section;
    private transient boolean initialized = false;
    private PhysicsComponent physicsComponent;
    private float width, height;

    @Override
    public float getX() {
        return physicsComponent.getX();
    }

    @Override
    public float getY() {
        return physicsComponent.getY();
    }

    public void setX(float x) {
        physicsComponent.setX(x);
    }

    public void setY(float y) {
        physicsComponent.setY(y);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y) {
        physicsComponent.setPosition(x, y);
    }

    public float getRotation() {
        return physicsComponent.getRotation();
    }

    public void setRotation(float rotation) {
        physicsComponent.setRotation(rotation);
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public Section getSection() {
        return section;
    }

    public Resources getResources() {
        return section.getResources();
    }

    @Override
    public void init() {
        initialized = true;
        physicsComponent.init();
    }

    @Override
    public void store() {
        initialized = false;
        physicsComponent.store();
    }

    @Override
    public void update(float delta) {
        physicsComponent.update(delta);
    }

    @ComponentAccess
    public PhysicsComponent getPhysicsComponent() {
        return physicsComponent;
    }

    public void setPhysicsComponent(PhysicsComponent physicsComponent) {
        this.physicsComponent = physicsComponent;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void beginCollision(Collision collision) {
        physicsComponent.beginCollision(collision);
    }

    @Override
    public void endCollision(Collision collision) {
        physicsComponent.endCollision(collision);
    }
}
