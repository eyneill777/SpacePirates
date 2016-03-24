package gken.rustyice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gken.rustyice.screens.Screen;

/**
 * @author Gabriel Keith
 */
public class EditorScreen extends Screen{
	private Mesh mesh;
	private ShaderProgram shader;
	private OrthographicCamera cam;
	public EditorScreen() {
		shader = new ShaderProgram(Gdx.files.internal("shaders/simple.vert"),
				Gdx.files.internal("shaders/simple.frag"));
		
		System.out.println(shader.isCompiled());
		
		cam = new OrthographicCamera(2, 2);
		cam.update();
		
		mesh = new Mesh(true, 3, 0,
				new VertexAttribute(Usage.Position, 2, "a_position"),
				new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"));
		float[] verts = new float[18];
		
		Color color = Color.RED;
		
		verts[0] = -1;
		verts[1] = -1;
		
		verts[2] = color.r;
		verts[3] = color.g;
		verts[4] = color.b;
		verts[5] = color.a;
		
		verts[6] = -1;
		verts[7] = 1;
		
		verts[8] = .2f;
		verts[9] = 1;
		verts[10] = color.b;
		verts[11] = color.a;
		
		verts[12] = 1;
		verts[13] = 1;
		
		verts[14] = color.r;
		verts[15] = color.g;
		verts[16] = color.b;
		verts[17] = color.a;
		
		mesh.setVertices(verts);
	}
	
    @Override
    public void load(Skin skin) {
    	
    }

    @Override
    public void show(Stage stage) {
    	
    }

    @Override
    public void hide(Stage stage) {
    	stage.clear();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(SpriteBatch batch, float delta) {
    	Gdx.gl.glDepthMask(false);
    	
    	Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	
        shader.begin();
        
        shader.setUniformMatrix("u_projTrans", cam.combined);
        
        mesh.render(shader, GL20.GL_TRIANGLES, 0, 18);
        
        shader.end();
        
        Gdx.gl.glDepthMask(true);
    }
}
