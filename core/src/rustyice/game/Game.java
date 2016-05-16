package rustyice.game;

import java.util.ArrayList;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import rustyice.game.actors.Actor;
import rustyice.game.actors.Player;
import rustyice.game.physics.Collidable;
import rustyice.game.physics.Collision;
import rustyice.game.tiles.TileMap;
import rustyice.graphics.Camera;
import rustyice.input.PlayerInput;
import rustyice.resources.Resources;

public class Game implements ContactListener {
    private static final float UPDATE_RATE = 1/60f;
    
    private ArrayList<Camera> cameras;
    private RayHandler rayHandler;
    private Resources resources;
    private PlayerInput playerInput;
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

        Camera camera = new Camera();
        camera.setWidth(10);
        camera.setHeight(10);

        sectionToLoad = new Section();

        // actors = new ArrayList<>();
        collisions = new ArrayList<>();
        cameras = new ArrayList<>();

        cameras.add(camera);

        Player player = new Player();

        player.setPosition(7, 7);
        camera.setTarget(player);
        camera.setTracking(true);

        sectionToLoad.addActor(player);
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

        for (Camera camera : cameras) {
            camera.setX(camera.getTarget().getX());
            camera.setY(camera.getTarget().getY());
        }

        section.setGame(this);
        section.init();

        for (Actor actor : section.getActors()) {
            if (actor instanceof Player) {
                ((Player) actor).setPlayerInput(playerInput);
            }
        }

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

    public void setPlayerInput(PlayerInput playerInput) {
        this.playerInput = playerInput;
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
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

            for (int i = 0; i < collisions.size(); i++) {
                Collision col = collisions.get(i);
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

            for (Camera camera : cameras) {
                camera.update(UPDATE_RATE);
            }
        }
    }

    public void render(SpriteBatch batch, Camera camera) {
        currentSection.render(batch, camera);
    }

    public Camera getCamera(int playerId) {
        return cameras.get(playerId);
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
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), contact, true));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), contact, true));
    }

    @Override
    public void endContact(Contact contact) {
        collisions.add(new Collision(contact.getFixtureA(), contact.getFixtureB(), contact, false));
        collisions.add(new Collision(contact.getFixtureB(), contact.getFixtureA(), contact, false));
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}