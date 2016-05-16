package rustyice.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class PerformanceTracker extends Widget {

    private BitmapFont font;
    private boolean glMoniter;
    private int fps;
    private int calls;
    private int drawCalls;
    private int switches;
    private int bindings;
    private int vertexCount;

    public PerformanceTracker(BitmapFont font, boolean glMoniter) {
        super();
        setTouchable(Touchable.disabled);
        this.font = font;
        this.glMoniter = glMoniter;
    }

    public void update() {
        fps = Gdx.graphics.getFramesPerSecond();
        
        if(glMoniter){
            calls = GLProfiler.calls;
            drawCalls = GLProfiler.drawCalls;
            switches = GLProfiler.shaderSwitches;
            bindings = GLProfiler.textureBindings;
            vertexCount = GLProfiler.vertexCount.count;
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha){
        font.setColor(Color.CYAN);
        font.draw(batch, String.format("fps: %d", fps), getWidth() - 100, getHeight() - 20);
        
        if(glMoniter){
            font.draw(batch, String.format("calls: %d", calls), getWidth() - 100, getHeight() - 40);
            font.draw(batch, String.format("draws: %d", drawCalls), getWidth() - 100, getHeight() - 60);
            font.draw(batch, String.format("switches: %d", switches), getWidth() - 100, getHeight() - 80);
            font.draw(batch, String.format("bindings: %d", bindings), getWidth() - 100, getHeight() - 100);
            font.draw(batch, String.format("vertex: %d", vertexCount), getWidth() - 100, getHeight() - 120);
        }
    }
}
