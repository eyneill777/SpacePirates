package rustyice.graphics

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.esotericsoftware.minlog.Log
import rustyengine.RustyEngine
import rustyice.game.Game
import rustyice.physics.*
import rustyengine.resources.Resources

class GameDisplay: Widget {
    var isInitialized: Boolean = false

    var povHandler: RayHandler? = null
    var povCameraView: PointLight? = null

    var fbo: FrameBuffer? = null
    val ortho: OrthographicCamera

    var camera: Camera? = null
    var game: Game? = null

    var debugRenderer: Box2DDebugRenderer? = null

    //private ShaderProgram lightSharder;
    //private Mesh lightMesh;

    val mouseProj: Vector3

    constructor(): super() {
        ortho = OrthographicCamera()
        mouseProj = Vector3()
    }

    fun init() {
        isInitialized = true

        debugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)

        if(width > 0 && height > 0){
            initFBO(width.toInt(), height.toInt())

            //lightSharder = DiffuseShader.createShadowShader();
            
            //lightMesh = createMesh();
        }
    }

    private fun initFBO(width: Int, height: Int){
        val game = game!!
        val fbo: FrameBuffer
        var povHandler = povHandler

        fbo = FrameBuffer(Format.RGBA8888, width, height, false)
        this.fbo = fbo
        fbo.colorBufferTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)

        game.lightingHandler.resizeFBO(width / 4, height / 4)
        if(povHandler == null){
            povHandler = RayHandler(game.world, width / 4, height / 4)
            povHandler.lightMapTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
            this.povHandler = povHandler

            povHandler.setLightShader(RustyEngine.resorces.povShader)
            povHandler.setBlur(false)
            //povHandler.setShadows(false);

            povHandler.setAmbientLight(0f, 0f, 0f, 0f)

            val povCameraView = PointLight(povHandler, 200)
            this.povCameraView = povCameraView
            povCameraView.distance = 10f
            povCameraView.isSoft = false
            povCameraView.setContactFilter(CAMERA_POV.toShort(), 0, WALL.toShort())
        } else {
            povHandler.resizeFBO(width / 4, height / 4)
            povHandler.lightMapTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
        }
    }

    private fun resize() {
        val fbo = fbo
        if (isInitialized && fbo != null && (Math.abs(width - fbo.width) > 5 || Math.abs(height - fbo.height) > 5)) {
            fbo.dispose()
            initFBO(width.toInt(), height.toInt())

            Log.debug(String.format("Resize buffer to %s x %s", width, height))
        }
    }

    fun stageToSection(vector: Vector2) {
        mouseProj.set(vector.x, Gdx.graphics.height - vector.y, 0f)

        val vec3 = ortho.unproject(mouseProj, 0f, 0f, width, height)
        vector.set(vec3.x, vec3.y)
    }

    private fun fitOrtho() {
        val camera = camera

        if(camera != null) {
            val xScale = width / ortho.viewportWidth
            val yScale = height / ortho.viewportHeight

            if (xScale < yScale) {
                ortho.viewportHeight *= (yScale / xScale)
            } else {
                ortho.viewportWidth *= (xScale / yScale)
            }

            camera.halfRenderSize = if (ortho.viewportWidth > ortho.viewportHeight)
                ortho.viewportWidth / 2 else ortho.viewportHeight / 2
        }
    }

    fun render(batch: Batch, delta: Float) {
        val camera = camera!!
        val game = game!!

        val lighting = camera.checkFlag(LIGHTING)
        val pov = camera.checkFlag(POV)
        val postLighting = camera.checkAnyFlag(POST_LIGHT_FLAGS)

        if (!isInitialized) {
            init()
        }
        val povCameraView = povCameraView!!
        val fbo = fbo!!

        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            if(debugRenderer == null){
                debugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)
            } else {
                debugRenderer?.dispose()
                debugRenderer = null
            }
        }

        updateProjection()
        if (lighting) {
            //game.getLightingHandler().setLightMapRendering(false);
            game.lightingHandler.setAmbientLight(0f, 0f, 0f, 0.01f)
            //game.getLightingHandler().setShadows(true);
            //game.getLightingHandler().setBlur(true);
            game.lightingHandler.setCombinedMatrix(ortho)
            game.lightingHandler.update()
            game.lightingHandler.prepareRender()
            Gdx.gl20.glDisable(GL20.GL_BLEND)
        }

        val povHandler = povHandler
        if (pov && povHandler != null){
            if(povCameraView.x != camera.x || povCameraView.y != camera.y){
                povCameraView.setPosition(camera.x, camera.y)
            }

            povHandler.setCombinedMatrix(ortho)
            povHandler.update()
            povHandler.prepareRender()
            Gdx.gl20.glDisable(GL20.GL_BLEND)
        }
        
        
        fbo.begin()
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        batch.begin()
        game.render(batch, camera, camera.flags and POST_LIGHT_FLAGS.inv())
        batch.end()

        if (lighting) {
            Gdx.gl20.glEnable(GL20.GL_BLEND)
            game.lightingHandler.renderOnly()
        }

        if (pov && povHandler != null){
            Gdx.gl20.glEnable(GL20.GL_BLEND)
            povHandler.renderOnly()
        }

        if(postLighting){
            batch.begin()
            game.render(batch, camera, camera.flags and POST_LIGHT_FLAGS)
            batch.end()
        }

        debugRenderer?.render(game.world, ortho.combined)

        fbo.end()
    }

    private fun updateProjection() {
        resize()
        camera!!.apply(ortho)
        fitOrtho()
        ortho.update()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (isInitialized) {
            val fbo = fbo!!

            batch.setColor(color.a, color.g, color.b, color.a * parentAlpha)
            batch.draw(fbo.colorBufferTexture, x, y, width, height, 0, 0, fbo.width, fbo.height, false, true)
        }
    }

    fun dispose() {
        if (isInitialized) {
            val fbo = fbo!!
            val povHandler = povHandler!!


            fbo.dispose()
            //lightSharder.dispose();
            //lightMesh.dispose();

            this.fbo = null
            //lightSharder = null;
            //lightMesh = null;
            isInitialized = false
            povHandler.dispose()
            this.povHandler = null

            debugRenderer?.dispose()
            debugRenderer = null
        }
    }
}
