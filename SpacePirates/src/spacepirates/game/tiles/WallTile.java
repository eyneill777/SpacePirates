package spacepirates.game.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import spacepirates.game.GameObject;
import spacepirates.game.physics.RectWallComponent;

public class WallTile extends Tile{

	public WallTile() {
		super();
        setSolid(true);
		setTilePhysics(new RectWallComponent(this));
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
