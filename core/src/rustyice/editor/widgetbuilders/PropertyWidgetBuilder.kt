package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kotcrab.vis.ui.widget.VisTable;

import java.lang.reflect.Method;
import kotlin.reflect.KMutableProperty

/**
 * @author gabek
 */
abstract class PropertyWidgetBuilder() {
    lateinit var title: String private set
    lateinit var component: Any private set
    lateinit var property: KMutableProperty<*> private set

    fun init(title: String, component: Any, property: KMutableProperty<*>): PropertyWidgetBuilder{
        this.title = title
        this.component = component
        this.property = property
        return this
    }

    abstract fun buildWidgets(): VisTable
}
