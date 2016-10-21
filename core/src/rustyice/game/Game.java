package rustyice.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import rustyice.physics.Collidable;
import rustyice.physics.Collision;
import rustyice.game.tiles.TileMap;
import rustyice.graphics.Camera;
import rustyice.resources.Resources;

import java.util.ArrayList;

public class Game implements ContactListener {
    private static final float UPDATE_RATE = 1/60f;

    private RayHandler rayHandler;
    private Resources resources;
    private World world;
    private Section currentSection;
    private Section sectionToLoad;
    private ArrayList<Collision> collisions;
    private float leftOverTime;

    public Game() {
        world = new World(Vector2.Zero, true);
        world.setContactListener(this);

        rayHandler = new RayHandler(world);
        leftOverTime = 0;

        sectionToLoad = new Section();

        // actors = new ArrayList<>();
        collisions = new ArrayList<>();
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    private void loadSection(Section section) {
        if (currentSection != null) {
            currentSection.store();
        }

        section.setGame(this);
        section.init();

        currentSection = section;
    }

    public void finishLoadingSection() {
        if (sectionToLoad != null) {
            loadSection(sectionToLoad);
            sectionToLoad = null;
        }
    }

    public void setSectionToLoad(Section sectionToLoad) {
        this.sectionToLoad = sectionToLoad;
    }

    public TileMap getTiles() {
        return currentSection.getTiles();
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Resources getResources() {
        return resources;
    }

    public void update(float delta) {
        finishLoadingSection();
        
        leftOverTime += delta;

        while (leftOverTime > 0) {
            leftOverTime -= UPDATE_RATE; 
            world.step(UPDATE_RATE, 6, 2);

            for (Collision col : collisions) {
                if (col.isBegin()) {
                    Collidable collidable = resolveCollidable(col.getThisFixture());
                    if (collidable != null) {
                        collidable.beginCollision(col);
                    }
                } else {
                    Collidable collidable = resolveCollidable(col.getThisFixture());
                    if (collidable != null) {
                        collidable.endCollision(col);
                    }
                }
            }
            collisions.clear();

            currentSection.update(UPDATE_RATE);
        }
    }

    public void render(SpriteBatch batch, Camera camera, int flags) {
        currentSection.render(batch, camera, flags);
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        world.dispose();
    }

    public Collidable resolveCollidable(Fixture fixture) {
        return (Collidable) fixture.getUserData();
    }

    @Override
    public void beginContact(Contact contact) {
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), true));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), true));
    }

    @Override
    public void endContact(Contact contact) {
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), false));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), false));
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}