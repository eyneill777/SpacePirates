package rustyice.game.characters.components;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import rustyice.game.actors.Actor;
import rustyice.game.physics.FillterFlags;
import rustyice.game.physics.components.SBPhysicsComponent;

/**
 * @author gabek
 */
public class CharacterPhysics extends SBPhysicsComponent{
    private float characterRadius;
    private float activatorRadius;

    public CharacterPhysics(Actor master){
        super(master);
    }

    public void walk(float maxX, float maxY, float acceleration) {
        float diffX = maxX - body.getLinearVelocity().x;
        float diffY = maxY - body.getLinearVelocity().y;
        if (Math.abs(diffX) > .1f || Math.abs(diffY) > .1f) {
            // cap on force generated.
            if (Math.abs(diffX) > acceleration) {
                diffX = (diffX < 0) ? -acceleration : acceleration;
            }
            if (Math.abs(diffY) > acceleration) {
                diffY = (diffY < 0) ? -acceleration : acceleration;
            }

            body.applyLinearImpulse(diffX, diffY, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
    }

    @Override
    public void init() {
        super.init();
        FixtureDef characterDef = new FixtureDef();
        characterDef.density = 1;
        characterDef.filter.categoryBits = FillterFlags.LARGE;
        characterDef.filter.maskBits = FillterFlags.LARGE | FillterFlags.WALL;

        addCircle(getMaster().getWidth()/2, characterDef);

        FixtureDef activatorDef = new FixtureDef();
        activatorDef.density = 0;
        characterDef.filter.categoryBits = FillterFlags.ACTIVATOR;
        characterDef.filter.maskBits = FillterFlags.ACTIVATABLE;

        addCircle(activatorRadius, activatorDef);

    }
}
