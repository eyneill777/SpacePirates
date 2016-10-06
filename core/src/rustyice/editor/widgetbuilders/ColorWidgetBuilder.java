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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gabek
 */
public class ColorWidgetBuilder extends PropertyWidgetBuilder{
    public ColorWidgetBuilder(Object component, String title) {
        super(component, title);
    }

    public static boolean isGetter(Method method){
        return method.getReturnType().equals(Color.class);
    }

    public static boolean isSetter(Method method){
        return method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(Color.class);
    }

    @Override
    public VisTable buildWidgets() {
        VisTable group = new VisTable();

        final ColorPicker colorPicker = new ColorPicker(new ColorPickerAdapter(){
            @Override
            public void finished(Color newColor) {
                try {
                    getSetter().invoke(getComponent(), newColor);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            colorPicker.setColor((Color) getGetter().invoke(getComponent()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        group.add(new VisLabel(getTitle()));

        VisTextButton changeButt = new VisTextButton("Change");
        changeButt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                group.getStage().addActor(colorPicker.fadeIn());
            }
        });
        group.add(changeButt);
        return group;
    }
}
