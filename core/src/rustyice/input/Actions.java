package rustyice.input;

import com.badlogic.gdx.Input;

public enum Actions {
	MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT;
	
	public static PlayerInput desktopDefault(){
		PlayerInput playerInputDesktop = new PlayerInput(Actions.values());
		
		playerInputDesktop.bind(MOVE_UP, Input.Keys.W);
		playerInputDesktop.bind(MOVE_DOWN, Input.Keys.S);
		playerInputDesktop.bind(MOVE_LEFT, Input.Keys.A);
		playerInputDesktop.bind(MOVE_RIGHT, Input.Keys.D);
		
		return playerInputDesktop;
	}
}
