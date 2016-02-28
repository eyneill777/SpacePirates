package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class WallTile extends Tile{

	public WallTile() {
		super();
		setSolid(true);
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
		Sprite sprite = new Sprite(getGame().getResources().box);
		sprite.setColor(Color.BLUE);
		setSprite(sprite);
	}
	
	@Override
	public void update(float delta) {
		
	}
}
