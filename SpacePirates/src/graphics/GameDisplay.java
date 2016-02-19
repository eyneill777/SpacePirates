package graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import Game.Camera;
import Game.GameWorld;

public class GameDisplay {
	private FrameBuffer fbo;
	private OrthographicCamera ortho;
	
	private Camera cam;
	private GameWorld world;
	
	public GameDisplay(int w, int h){
		ortho = new OrthographicCamera();
		
		fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public void setTarget(GameWorld world, Camera camera){
		this.world = world;
		this.cam = camera;
	}
	
	public void resize(int w, int h){
		if(w != fbo.getWidth() || h != fbo.getHeight()){
			fbo.dispose();
			fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
			fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
	}
	
	public void render(SpriteBatch batch, float delta){
		fbo.bind();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.apply(ortho);
		ortho.update();
		
		batch.setProjectionMatrix(ortho.projection);
		batch.setTransformMatrix(ortho.view);
		
		batch.begin();
		
		world.render(batch);
		
		batch.end();
		
		fbo.end();
	}
	
	public void draw(SpriteBatch batch, float delta, float x, float y, float w, float h){
		batch.draw(fbo.getColorBufferTexture(), x, y, w, h, 0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
	}
	
	public void dispose(){
		fbo.dispose();
	}
}
