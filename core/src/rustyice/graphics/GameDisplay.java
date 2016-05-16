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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.esotericsoftware.minlog.Log;
import rustyice.game.Game;
import shaders.DiffuseShader;

public class GameDisplay extends Widget {
    private FrameBuffer fbo;
    private OrthographicCamera ortho;

    private Camera cam;
    private Game game;

    private ShaderProgram lightSharder;
    private Mesh lightMesh;

    private Vector3 mouseProj;

    private boolean lightsActive = true;
    private boolean initialized = false;

    public GameDisplay() {
        this.ortho = new OrthographicCamera();
        this.mouseProj = new Vector3();
    }

    public void setLightsActive(boolean active) {
        this.lightsActive = active;
    }

    public boolean isLightsActive() {
        return this.lightsActive;
    }

    public void init() {
        if(getWidth() > 0 && getHeight() > 0){
            fbo = new FrameBuffer(Format.RGBA8888, (int) getWidth(), (int) getHeight(), false);
            fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            game.getRayHandler().resizeFBO((int) getWidth() / 4, (int) getHeight() / 4);
            //game.getRayHandler().getLightMapTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            lightSharder = DiffuseShader.createShadowShader();
            
            lightMesh = createMesh();

            initialized = true;
        }
    }

    public void setTarget(Game world, Camera camera) {
        game = world;
        cam = camera;
    }

    public OrthographicCamera getOrtho() {
        return ortho;
    }

    private void resize() {
        if (this.initialized && (Math.abs(getWidth() - this.fbo.getWidth()) > 2 || Math.abs(getHeight() - this.fbo.getHeight()) > 2)) {
            fbo.dispose();
            fbo = new FrameBuffer(Format.RGBA8888, (int) getWidth(), (int) getHeight(), false);
            //fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            game.getRayHandler().resizeFBO((int) getWidth() / 4, (int) getHeight() / 4);
            //game.getRayHandler().getLightMapTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            Log.debug(String.format("Resize buffer to %s x %s", getWidth(), getHeight()));
        }
    }

    public void stageToSection(Vector2 vector) {
        this.mouseProj.set(vector.x, getHeight() - vector.y, 0);

        Vector3 vec3 = this.ortho.unproject(this.mouseProj, getX(), getStage().getHeight() - (getY() + getHeight()), getWidth(), getHeight());
        vector.set(vec3.x, vec3.y);
    }

    public FrameBuffer getFBO() {
        return this.fbo;
    }

    private void fitOrtho() {
        float xScale = getWidth() / this.ortho.viewportWidth;
        float yScale = getHeight() / this.ortho.viewportHeight;

        if (xScale < yScale) {
            this.ortho.viewportHeight *= (yScale / xScale);
        } else {
            this.ortho.viewportWidth *= (xScale / yScale);
        }

        this.cam.setHalfRenderSize(this.ortho.viewportWidth > this.ortho.viewportHeight ? this.ortho.viewportWidth / 2 : this.ortho.viewportHeight / 2);
    }

    public void render(SpriteBatch batch, float delta) {
        if (!initialized) {
            init();
        }

        updateProjection();
        if (lightsActive) {
            game.getRayHandler().setLightMapRendering(false);
            game.getRayHandler().setCombinedMatrix(this.ortho);
            game.getRayHandler().updateAndRender();
        }
        
        
        fbo.begin();
        // Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(ortho.projection);
        batch.setTransformMatrix(ortho.view);

        batch.begin();
        game.render(batch, this.cam);
        batch.end();

        if (this.lightsActive) {
            Color c = new Color(.05f, .05f, .05f, 1);

            game.getRayHandler().getLightMapTexture().bind(0);

            Gdx.gl20.glEnable(GL20.GL_BLEND);
            lightSharder.begin();
            game.getRayHandler().diffuseBlendFunc.apply();
            lightSharder.setUniformf("ambient", c.r, c.b, c.g, c.a);

            lightMesh.render(this.lightSharder, GL20.GL_TRIANGLE_FAN);

            lightSharder.end();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
        }

        fbo.end();
        
    }

    private void updateProjection() {
        resize();
        cam.apply(this.ortho);
        fitOrtho();
        ortho.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.initialized) {
            batch.setColor(getColor().a, getColor().g, getColor().b, getColor().a * parentAlpha);
            batch.draw(fbo.getColorBufferTexture(), getX(), getY(), getWidth(), getHeight(), 0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
        }
    }

    public void dispose() {
        if (this.initialized) {
            this.fbo.dispose();
            this.lightSharder.dispose();
            this.lightMesh.dispose();

            this.fbo = null;
            this.lightSharder = null;
            this.lightMesh = null;
            this.initialized = false;
        }
    }

    private Mesh createMesh() {
        float[] verts = { -1, -1, 0, 0, 1, -1, 1, 0, 1, 1, 1, 1, -1, 1, 0, 1 };

        Mesh mesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, "a_position"), new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord"));

        mesh.setVertices(verts);

        return mesh;
    }
}
