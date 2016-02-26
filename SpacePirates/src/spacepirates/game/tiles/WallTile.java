package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class WallTile extends Tile{

	public WallTile() {
		super();
		 hasFixture = true;
	}
	
	@Override
	public void beginCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void endCollision(Fixture thisFixture, Fixture otherFixture, Contact contact) {
		
	}

	@Override
	public void init(){
		super.init();
		setTexture(getGame().getResources().box);
		setColor(Color.BLUE);
	}
	
	@Override
	public void update(float delta) {
		
	}
}
