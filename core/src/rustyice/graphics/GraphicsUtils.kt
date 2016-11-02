package rustyice.graphics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import rustyice.resources.Resources


fun drawRect(batch: Batch, x: Float, y: Float, width: Float, height: Float, thickness: Float) {
    val box = Resources.box

    batch.draw(box, x, y, width, thickness)
    batch.draw(box, x, y, thickness, height)
    batch.draw(box, x, y + height - thickness, width, thickness)
    batch.draw(box, x + width - thickness, y, thickness, height)
}

fun drawLine(batch: Batch, x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float) {
    val dx = (x2 - x1).toDouble()
    val dy = (y2 - y1).toDouble()
    val dist = Math.sqrt(dx * dx + dy * dy).toFloat()
    val rad = (Math.atan2(dy, dx) * MathUtils.radDeg).toFloat()
    batch.draw(Resources.box, x1, y1, 0f, 0f, dist, thickness, 1f, 1f, rad)
}
