package spacepirates.input;

import com.badlogic.gdx.Input;

public enum Actions {
	MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT;
	
	public static PlayerInput desktopDefault(){
		PlayerInput playerInputDesktop = new PlayerInput(Actions.values());
		
		playerInputDesktop.bind(MOVE_UP, Input.Keys.UP);
		playerInputDesktop.bind(MOVE_DOWN, Input.Keys.DOWN);
		playerInputDesktop.bind(MOVE_LEFT, Input.Keys.LEFT);
		playerInputDesktop.bind(MOVE_RIGHT, Input.Keys.RIGHT);
		
		return playerInputDesktop;
	}
}
