package rustyice.editor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Tree
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTree
import rustyice.editor.annotations.ComponentAccess
import rustyice.editor.annotations.ComponentProperty
import rustyice.editor.widgetbuilders.*
import rustyice.game.GameObject
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.memberProperties

/**
 * @author gabek
 */
class EditorPropertyPane {
    private val propertyTreeScroll: VisScrollPane
    private val propertyTree: VisTree

    private var selectedObject: GameObject? = null
    private val validatorList: List<PropertyValidator>

    val root: WidgetGroup get() = propertyTreeScroll

    init {
        propertyTree = VisTree()
        propertyTreeScroll = VisScrollPane(propertyTree)

        val mValList = ArrayList<PropertyValidator>()
        mValList.add(TypePropertyValidator(Float::class, {FloatWidgetBuilder()}))
        mValList.add(TypePropertyValidator(Boolean::class, {BoolWidgetBuilder()}))
        mValList.add(TypePropertyValidator(Color::class, {ColorWidgetBuilder()}))

        validatorList = mValList
    }

    fun setSelected(selectedObject: GameObject){
        if(this.selectedObject != selectedObject){
            this.selectedObject = selectedObject
            propertyTree.clearChildren()

            propertyTree.add(buildCompEditNode(selectedObject))
            val compSet = HashSet<Any>()

            val c = selectedObject.javaClass.kotlin
            for(prop in c.members){
                if(prop is KProperty) {
                    if (getInheritedAnnotation(c, prop, ComponentAccess::class) != null) {
                        val comp = prop.call(selectedObject)
                        if (comp != null && compSet.add(comp)) {
                            propertyTree.add(buildCompEditNode(comp))
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <A: Annotation> getInheritedAnnotation(cls: KClass<*>, prop: KProperty<*>, anno: KClass<A>): A?{
        for(a in prop.annotations){
            if(a.annotationClass == anno){
                return a as A
            }
        }
        return null

        //check superclasses
        //var superclass = cls.
        //while (superclass != null && out == null){
        //    for(Method method: superclass.getDeclaredMethods()){
        //        if(method.getName().equals(mth.getName())
        //                && Arrays.equals(method.getParameterTypes(), mth.getParameterTypes())
        //                && method.getAnnotation(anno) != null){
        //            return true
        //        }
        //    }
        //    superclass = superclass.getSuperclass();
        //}

    }


    private fun buildCompEditNode(comp: Any): Tree.Node {
        val kClass = comp.javaClass.kotlin

        val node = Tree.Node(VisLabel(kClass.simpleName))
        node.isSelectable = false

        val propertyMap = ArrayList<PropertyWidgetBuilder>()

        for(prop in kClass.memberProperties){
            if(prop is KMutableProperty<*>) {
                val propertyAnno = getInheritedAnnotation(kClass, prop, ComponentProperty::class)

                if (propertyAnno != null) {
                    val title = prop.name
                    for(validator in validatorList){
                        if(validator.isInstance(prop)){
                            propertyMap.add(validator.widgetBuilder().init(title, comp, prop))
                        }
                    }
                }
            }
        }

        for (propertyWidgetBuilder in propertyMap) {
            val widgetNode = Tree.Node(propertyWidgetBuilder.buildWidgets())
            widgetNode.isSelectable = false
            node.add(widgetNode)
        }

        return node
    }

}
