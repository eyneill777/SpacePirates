package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Fixture;
import spacepirates.game.physics.RectWallComponent;

public class BoundaryTile extends Tile{

	public BoundaryTile() {
		super();
        setSolid(true);
		setTilePhysics(new RectWallComponent(this));
	}
	
	public void init(){
		super.init();
		Sprite sprite = new Sprite(getGame().getResources().box);
		sprite.setColor(Color.YELLOW);
		setSprite(sprite);
	}

	@Override
	public void store() {
		super.store();

	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public boolean shouldCollide(Fixture thisFixture, Fixture otherFixture) {
		return (thisFixture.getFilterData().maskBits & otherFixture.getFilterData().categoryBits) != 0;
	}
}
