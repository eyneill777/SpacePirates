package rustyice.graphics

import com.badlogic.gdx.graphics.profiling.GLErrorListener
import com.esotericsoftware.minlog.Log

object GLErrorLogger: GLErrorListener {
    override fun onError(error: Int) {
        Log.error("GL_ERROR", String.format("Error code: %d", error))
    }
}
