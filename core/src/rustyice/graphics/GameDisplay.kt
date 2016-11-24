package rustyice.graphics

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
import rustyice.game.Game

class GameDisplay: Widget() {
    var isInitialized: Boolean = false

    var fbo: FrameBuffer? = null
    val ortho: OrthographicCamera

    var game: Game? = null
    var camera: Camera? = null

    var debugRenderer: Box2DDebugRenderer? = null

    //private ShaderProgram lightSharder;
    //private Mesh lightMesh;

    val mouseProj: Vector3

    init {
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
        val fbo: FrameBuffer

        fbo = FrameBuffer(Format.RGBA8888, width, height, false)
        this.fbo = fbo
        fbo.colorBufferTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
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

    fun prepareRender(){
        if (!isInitialized) {
            init()
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            if(debugRenderer == null){
                debugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)
            } else {
                debugRenderer?.dispose()
                debugRenderer = null
            }
        }

        updateProjection()
    }

    fun render(batch: Batch) {
        prepareRender()

        val fbo = fbo!!
        val game = game!!
        val camera = camera!!

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        fbo.begin()
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        game.render(batch, camera, RenderLayer.ACTORS)
        batch.end()

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

            fbo.dispose()

            this.fbo = null
            isInitialized = false

            debugRenderer?.dispose()
            debugRenderer = null
        }
    }
}
