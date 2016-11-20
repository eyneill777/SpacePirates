package rustyice.editor.widgetbuilders

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaType

/**
 * @author Gabriel Keith
 */

class TypePropertyValidator(val type: KClass<*>, val builder: () -> PropertyWidgetBuilder) : PropertyValidator {
    override fun isInstance(property: KMutableProperty<*>): Boolean {
        return property.getter.returnType.javaType === type.java
                && property.setter.parameters.size == 2 && property.setter.parameters[1].type.javaType === type.java
    }

    override fun widgetBuilder(): PropertyWidgetBuilder {
        return builder()
    }

}