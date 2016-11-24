package rustyice.graphics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import rustyengine.RustyEngine
import rustyengine.resources.Resources


fun Batch.drawRect(x: Float, y: Float, width: Float, height: Float, thickness: Float) {
    val box = RustyEngine.resorces.box

    draw(box, x, y, width, thickness)
    draw(box, x, y, thickness, height)
    draw(box, x, y + height - thickness, width, thickness)
    draw(box, x + width - thickness, y, thickness, height)
}

fun Batch.drawLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float) {
    val box = RustyEngine.resorces.box

    val dx = (x2 - x1).toDouble()
    val dy = (y2 - y1).toDouble()
    val dist = Math.sqrt(dx * dx + dy * dy).toFloat()
    val rad = (Math.atan2(dy, dx) * MathUtils.radDeg).toFloat()
    draw(box, x1, y1, 0f, 0f, dist, thickness, 1f, 1f, rad, 0, 0, box.width, box.height, false, false)
}
