package graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Game.Game;

public class GameDisplay {
	private FrameBuffer fbo;
	private OrthographicCamera ortho;
	private Vector3 mouseProj;
	private Vector2 mouse;
	
	private Rectangle screenLocation;
	private Camera cam;
	private Game game;
	
	public GameDisplay(int w, int h){
		ortho = new OrthographicCamera();
		mouseProj = new Vector3();
		mouse = new Vector2();
		screenLocation = new Rectangle(0, 0, w, h);
		
		fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public void setTarget(Game world, Camera camera){
		this.game = world;
		this.cam = camera;
	}
	
	public void resize(int w, int h){
		if(w != fbo.getWidth() || h != fbo.getHeight()){
			fbo.dispose();
			fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
			fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		screenLocation.width = w;
		screenLocation.height = h;
	}
	
	public void updateMouse(float x, float y){
		mouseProj.set(x, y, 0);
		
		Vector3 vec3 = ortho.unproject(mouseProj);
		mouse.x = vec3.x;
		mouse.y = vec3.y;
	}
	
	public Vector2 getMouseControl(){
		return mouse;
	}
	
	private void fitOrtho(){
		float xScale = screenLocation.width/ortho.viewportWidth;
		float yScale = screenLocation.height/ortho.viewportHeight;
		
		if(xScale < yScale){
			ortho.viewportHeight *= (yScale/xScale);
		} else {
			ortho.viewportWidth *= (xScale/yScale);
		}
	}
	
	public void render(SpriteBatch batch, float delta){
		fbo.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.apply(ortho);
		fitOrtho();
		ortho.update();
		
		batch.setProjectionMatrix(ortho.projection);
		batch.setTransformMatrix(ortho.view);
		
		batch.begin();
		
		game.render(batch);
		
		batch.end();
		
		fbo.end();
	}
	
	public void draw(SpriteBatch batch, float delta){
		batch.draw(fbo.getColorBufferTexture(), screenLocation.x, screenLocation.y, screenLocation.width, screenLocation.height,
				0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
	}
	
	public void dispose(){
		fbo.dispose();
	}
}
