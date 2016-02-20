package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class PlayerInput {
	private Enum<?>[] actions;
	private int[] keyBindings;
	private Vector2 mouse;
	
	public PlayerInput(Enum<?>[] actions){
		this.actions = actions;
		keyBindings = new int[actions.length];
		
		for(int i = 0; i < keyBindings.length; i++){
			keyBindings[i] = -1;
		}
	}
	
	public void bind(int action, int buttion){
		keyBindings[action] = buttion;
	}
	
	public boolean isPressed(int action){
		if(keyBindings[action] == -1){
			return false;
		}
		
		return Gdx.input.isKeyPressed(keyBindings[action]);
	}
	
	public void bind(Enum<?> action, int buttion){
		bind(action.ordinal(), buttion);
	}
	
	public boolean isPressed(Enum<?> action){
		return isPressed(action.ordinal());
	}
	
	public void setMouseControl(Vector2 mouse){
		this.mouse = mouse;
	}
	
	public float getMouseX(){
		return mouse.x;
	}
	
	public float getMouseY(){
		return mouse.y;
	}
	
	public boolean getMouseButton(int button){
		return Gdx.input.isButtonPressed(button);
	}
}
