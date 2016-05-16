package rustyice.graphics;

import com.badlogic.gdx.graphics.profiling.GLErrorListener;
import com.esotericsoftware.minlog.Log;

public class GLErrorLogger implements GLErrorListener {

    @Override
    public void onError(int error) {
        Log.error("GL_ERROR", String.format("Error code: %d", error));
    }
}
