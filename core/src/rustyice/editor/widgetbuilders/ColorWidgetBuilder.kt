package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;
import ktx.actors.onClick

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaType

/**
 * @author Gabriel Keith
 */


class ColorWidgetBuilder: PropertyWidgetBuilder(){
    override fun buildWidgets(): VisTable {
        val group = VisTable()

        val colorPicker = ColorPicker(object : ColorPickerAdapter(){
            override fun finished(newColor: Color) {
                property.setter.call(component, newColor)
            }
        })

        colorPicker.color = property.getter.call(component) as Color

        group.add(VisLabel(title))

        val changeButt = VisTextButton("Change")
        changeButt.onClick { inputEvent, visTextButton ->
            group.stage.addActor(colorPicker.fadeIn())
        }
        group.add(changeButt)
        return group
    }
}