package rustyice.game;

import rustyice.editor.annotations.ComponentAccess;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.physics.Collidable;
import rustyice.physics.Collision;
import rustyice.physics.PhysicsComponent;
import rustyice.resources.Resources;


public abstract class Actor implements GameObject, Collidable {

    private Section section;
    private transient boolean initialized = false;
    private PhysicsComponent physicsComponent;
    private float width, height;

    @Override
    @ComponentProperty(title = "X")
    public float getX() {
        return physicsComponent.getX();
    }

    @Override
    @ComponentProperty(title = "Y")
    public float getY() {
        return physicsComponent.getY();
    }

    @Override
    @ComponentProperty(title = "X")
    public void setX(float x) {
        physicsComponent.setX(x);
    }

    @Override
    @ComponentProperty(title = "Y")
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

    @ComponentProperty(title = "Rotation")
    public float getRotation() {
        return physicsComponent.getRotation();
    }

    @ComponentProperty(title = "Rotation")
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
        physicsComponent.init(this);
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
