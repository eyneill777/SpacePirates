package rustyice.editor.widgetbuilders

import kotlin.reflect.KMutableProperty

/**
 * @author Gabriel Keith
 */
interface PropertyValidator {
    fun isInstance(property: KMutableProperty<*>): Boolean
    fun widgetBuilder(): PropertyWidgetBuilder
}