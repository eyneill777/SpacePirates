package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.kotcrab.vis.ui.util.Validators
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisValidatableTextField
import ktx.actors.onClick
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaType

/**
 * @author gabek
 */
class FloatWidgetBuilder: PropertyWidgetBuilder(){
    override fun buildWidgets(): VisTable {
        val group = VisTable()

        group.add(VisLabel(title))
        val numberField = VisValidatableTextField(Validators.FloatValidator())


        numberField.text = property.getter.call(component).toString()

        group.add(numberField).width(50f)

        val applyButt = VisTextButton("Apply")
        applyButt.onClick { input: InputEvent, textButton: VisTextButton ->
            if(numberField.isInputValid){
                property.setter.call(component, numberField.text.toFloat())
            }
        }

        group.add(applyButt)
        return group
    }
}
