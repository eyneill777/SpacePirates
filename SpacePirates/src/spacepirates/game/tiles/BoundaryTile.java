package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import spacepirates.game.Game;

public class BoundaryTile extends Tile{

	public BoundaryTile() {
		super();
		hasFixture = true;
	}
	
	public void init(){
		super.init();
		setColor(Color.YELLOW);
		setTexture(getGame().getResources().box);
	}
	
	@Override
	public FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = super.buildFixtureDef();
		
		fixtureDef.filter.categoryBits = Game.CAT_BOUNDARY | Game.CAT_TILE;
		
		return fixtureDef;
	}
	
	@Override
	public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void update(float delta) {
		
	}
}
