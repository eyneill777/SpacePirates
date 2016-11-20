package rustyice.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

class PerformanceTracker(private val font: BitmapFont, private val glMoniter: Boolean) : Widget() {

    private var fps: Int = 0
    private var calls: Int = 0
    private var drawCalls: Int = 0
    private var switches: Int = 0
    private var bindings: Int = 0
    private var vertexCount: Int = 0

    fun update() {
        fps = Gdx.graphics.framesPerSecond
        
        if(glMoniter){
            calls = GLProfiler.calls
            drawCalls = GLProfiler.drawCalls
            switches = GLProfiler.shaderSwitches
            bindings = GLProfiler.textureBindings
            vertexCount = GLProfiler.vertexCount.count
        }
    }
    
    override fun draw(batch: Batch, alpha: Float){
        font.color = Color.ORANGE
        font.draw(batch, String.format("fps: %d", fps), width - 100, height - 20)
        if(glMoniter){
            font.draw(batch, String.format("calls: %d", calls), width - 100, height - 40)
            font.draw(batch, String.format("draws: %d", drawCalls), width - 100, height - 60)
            font.draw(batch, String.format("switches: %d", switches), width - 100, height - 80)
            font.draw(batch, String.format("bindings: %d", bindings), width - 100, height - 100)
            font.draw(batch, String.format("vertex: %d", vertexCount), width - 100, height - 120)
        }
    }

    init {
        touchable = Touchable.disabled
    }
}
