package gken.rustyice.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import gken.rustyice.game.Game;

public class GameDisplay extends Widget{
	private FrameBuffer fbo;
	private OrthographicCamera ortho;
	private Vector3 mouseProj;
	private Vector2 mouse;

	private Camera cam;
	private Game game;
	
	public GameDisplay(int w, int h){
		ortho = new OrthographicCamera();
		mouseProj = new Vector3();
		mouse = new Vector2();
		setWidth(w);
		setHeight(h);
		
		fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public void setTarget(Game world, Camera camera){
		this.game = world;
		this.cam = camera;
	}
	
	public OrthographicCamera getOrtho(){
		return ortho;
	}
	
	private void resize(){
		if(getWidth() != fbo.getWidth() || getHeight() != fbo.getHeight()){
			fbo.dispose();
			fbo = new FrameBuffer(Format.RGBA8888, (int)getWidth(), (int)getHeight(), false);
			fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
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
		float xScale = getWidth()/ortho.viewportWidth;
		float yScale = getHeight()/ortho.viewportHeight;
		
		if(xScale < yScale){
			ortho.viewportHeight *= (yScale/xScale);
		} else {
			ortho.viewportWidth *= (xScale/yScale);
		}
	}
	
	public void render(SpriteBatch batch, float delta){
		resize();

		fbo.begin();
		//Gdx.gl.glC
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(ortho.projection);
		batch.setTransformMatrix(ortho.view);
		
		batch.begin();
		
		game.render(batch);
		
		batch.end();
		
		fbo.end();
	}
	
	public void updateProjection(){
		cam.apply(ortho);
		fitOrtho();
		ortho.update();
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		batch.draw(fbo.getColorBufferTexture(), getX(), getY(), getWidth(), getHeight(),
				0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
	}
	
	public void dispose(){
		fbo.dispose();
	}
}
