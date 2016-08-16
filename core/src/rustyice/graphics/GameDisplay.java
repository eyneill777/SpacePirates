package rustyice.graphics;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.esotericsoftware.minlog.Log;
import rustyice.game.Game;
import rustyice.game.physics.FillterFlags;

public class GameDisplay extends Widget {
    private RayHandler povHandler;
    private ShaderProgram povShader;
    private PointLight povCameraView;

    private FrameBuffer fbo;
    private OrthographicCamera ortho;

    private Camera cam;
    private Game game;

    private Box2DDebugRenderer debugRenderer;

    //private ShaderProgram lightSharder;
    //private Mesh lightMesh;

    private Vector3 mouseProj;

    private boolean initialized = false;

    public GameDisplay() {
        ortho = new OrthographicCamera();
        mouseProj = new Vector3();
    }

    public void init() {
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);

        if(getWidth() > 0 && getHeight() > 0){
            initFBO((int) getWidth(), (int) getHeight());

            //lightSharder = DiffuseShader.createShadowShader();
            
            //lightMesh = createMesh();

            initialized = true;
        }
    }

    private void initFBO(int width, int height){
        fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
        fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        game.getRayHandler().resizeFBO(width / 2, height / 2);
        if(povHandler == null){
            povShader = PovShader.createLightShader();

            povHandler = new RayHandler(game.getWorld(), width / 4, height / 4);
            povHandler.setLightShader(povShader);
            povHandler.setBlur(false);
            //povHandler.setShadows(false);

            povHandler.setAmbientLight(0, 0, 0, 0);

            povCameraView = new PointLight(povHandler, 400);
            povCameraView.setDistance(10);
            povCameraView.setSoft(false);
            povCameraView.setContactFilter(FillterFlags.CAMERA_POV, (short)0, FillterFlags.WALL);
        } else {
            povHandler.resizeFBO(width / 2, height / 2);
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
        if (initialized && (Math.abs(getWidth() - fbo.getWidth()) > 5 || Math.abs(getHeight() - fbo.getHeight()) > 5)) {
            fbo.dispose();
            initFBO((int) getWidth(), (int) getHeight());

            Log.debug(String.format("Resize buffer to %s x %s", getWidth(), getHeight()));
        }
    }

    public void stageToSection(Vector2 vector) {
        mouseProj.set(vector.x, Gdx.graphics.getHeight() - vector.y, 0);

        Vector3 vec3 = ortho.unproject(mouseProj, 0, 0, getWidth(), getHeight());
        vector.set(vec3.x, vec3.y);
    }

    public FrameBuffer getFBO() {
        return fbo;
    }

    private void fitOrtho() {
        float xScale = getWidth() / ortho.viewportWidth;
        float yScale = getHeight() / ortho.viewportHeight;

        if (xScale < yScale) {
            ortho.viewportHeight *= (yScale / xScale);
        } else {
            ortho.viewportWidth *= (xScale / yScale);
        }

        cam.setHalfRenderSize(ortho.viewportWidth > ortho.viewportHeight ? ortho.viewportWidth / 2 : ortho.viewportHeight / 2);
    }

    public void render(SpriteBatch batch, float delta) {
        boolean lighting = cam.checkFlag(RenderFlags.LIGHTING);
        boolean pov = cam.checkFlag(RenderFlags.POV);
        boolean postLighting = cam.checkAnyFlag(RenderFlags.POST_LIGHT_FLAGS);

        if (!initialized) {
            init();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            if(debugRenderer == null){
                debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);
            } else {
                debugRenderer.dispose();
                debugRenderer = null;
            }
        }

        updateProjection();
        if (lighting) {
            //game.getRayHandler().setLightMapRendering(false);
            game.getRayHandler().setAmbientLight(0, 0, 0, 0.01f);
            //game.getRayHandler().setShadows(true);
            //game.getRayHandler().setBlur(true);
            game.getRayHandler().setCombinedMatrix(ortho);
            game.getRayHandler().update();
            game.getRayHandler().prepareRender();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
        }

        if (pov){
            if(povCameraView.getX() != cam.getX() || povCameraView.getY() != cam.getY()){
                povCameraView.setPosition(cam.getX(), cam.getY());
            }

            povHandler.setCombinedMatrix(ortho);
            povHandler.update();
            povHandler.prepareRender();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
        }
        
        
        fbo.begin();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(ortho.projection);
        batch.setTransformMatrix(ortho.view);

        batch.begin();
        game.render(batch, cam, cam.getFlags() & ~RenderFlags.POST_LIGHT_FLAGS);
        batch.end();

        if (lighting) {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            game.getRayHandler().renderOnly();
        }

        if (pov){
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            povHandler.renderOnly();
        }

        if(postLighting){
            batch.begin();
            game.render(batch, cam, cam.getFlags() & RenderFlags.POST_LIGHT_FLAGS);
            batch.end();
        }

        if(debugRenderer != null){
            debugRenderer.render(game.getWorld(), ortho.combined);
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
        if (initialized) {
            batch.setColor(getColor().a, getColor().g, getColor().b, getColor().a * parentAlpha);
            batch.draw(fbo.getColorBufferTexture(), getX(), getY(), getWidth(), getHeight(), 0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
        }
    }

    public void dispose() {
        if (initialized) {
            fbo.dispose();
            //lightSharder.dispose();
            //lightMesh.dispose();

            fbo = null;
            //lightSharder = null;
            //lightMesh = null;
            initialized = false;
            povHandler.dispose();
            povShader.dispose();

            if(debugRenderer != null){
                debugRenderer.dispose();
            }
        }
    }
}
