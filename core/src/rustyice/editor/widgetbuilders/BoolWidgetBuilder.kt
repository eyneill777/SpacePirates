package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import ktx.actors.onChange
import ktx.actors.onClick

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gabek
 */


class BoolWidgetBuilder: PropertyWidgetBuilder(){
    override fun buildWidgets(): VisTable {
        val group = VisTable()

        group.add(VisLabel(title))

        val selectBox = VisSelectBox<Boolean>()
        selectBox.setItems(true, false)

        selectBox.selected = property.getter.call(component) as Boolean

        selectBox.onChange { changeEvent, visSelectBox ->
            property.setter.call(component, selectBox.selected)
        }

        group.add(selectBox)

        return group
    }
}
