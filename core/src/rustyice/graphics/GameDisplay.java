package rustyice.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import box2dLight.RayHandler;
import rustyice.game.Game;
import shaders.DiffuseShader;
import shaders.ShadowShader;

public class GameDisplay extends Widget{
	private FrameBuffer fbo;
	private OrthographicCamera ortho;
	private Vector3 mouseProj;
	private Vector2 mouse;

	private Camera cam;
	private Game game;
	
	private ShaderProgram lightSharder;
	private Mesh lightMesh;
	
	public GameDisplay(int w, int h){
		ortho = new OrthographicCamera();
		mouseProj = new Vector3();
		mouse = new Vector2();
		setWidth(w);
		setHeight(h);
		
		fbo = new FrameBuffer(Format.RGBA8888, w, h, false);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		lightSharder = DiffuseShader.createShadowShader();
		lightMesh = createMesh();
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
	
	public void render(SpriteBatch batch, RayHandler rayHandler, float delta){
		rayHandler.setLightMapRendering(false);
		rayHandler.setCombinedMatrix(ortho);
		rayHandler.updateAndRender();
		
		fbo.begin();
		//Gdx.gl.glC
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(ortho.projection);
		batch.setTransformMatrix(ortho.view);
		
		batch.begin();
		game.render(batch);
		batch.end();
		
		Color c = new Color(.05f, .05f, .05f, 1);
		
		rayHandler.getLightMapTexture().bind(0);
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		lightSharder.begin();
		rayHandler.diffuseBlendFunc.apply();
		lightSharder.setUniformf("ambient", c.r, c.b, c.g, c.a);
		
		lightMesh.render(lightSharder, GL20.GL_TRIANGLE_FAN);
		
		lightSharder.end();
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		fbo.end();
	}
	
	public void updateProjection(){
		resize();
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
		lightSharder.dispose();
		lightMesh.dispose();
	}
	
	private Mesh createMesh(){
		float[] verts = {-1, -1, 0, 0,
						  1, -1, 1, 0,
						  1,  1, 1, 1,
						  -1, 1, 0, 1};
		
		Mesh mesh = new Mesh(true, 4, 0, new VertexAttribute(
				Usage.Position, 2, "a_position"), new VertexAttribute(
				Usage.TextureCoordinates, 2, "a_texCoord"));
		
		mesh.setVertices(verts);
		return mesh;
	}
}
