package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import spacepirates.game.Game;
import spacepirates.game.GameObject;

public class BoundaryTile extends Tile{

	public BoundaryTile() {
		super();
		setSolid(true);
	}
	
	public void init(){
		super.init();
		Sprite sprite = new Sprite(getGame().getResources().box);
		sprite.setColor(Color.YELLOW);
		setSprite(sprite);
	}
	
	@Override
	public FixtureDef buildFixtureDef(){
		FixtureDef fixtureDef = super.buildFixtureDef();
		
		fixtureDef.filter.categoryBits = Game.CAT_BOUNDARY | Game.CAT_TILE;
		
		return fixtureDef;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
		return (thisFixture.getFilterData().maskBits & otherFixture.getFilterData().categoryBits) != 0;
	}
}
