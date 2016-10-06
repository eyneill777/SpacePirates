package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gabek
 */
public class BoolWidgetBuilder extends PropertyWidgetBuilder{
    public BoolWidgetBuilder(Object component, String title) {
        super(component, title);
    }

    public static boolean isGetter(Method method){
        return method.getReturnType().equals(boolean.class);
    }

    public static boolean isSetter(Method method){
        return method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(boolean.class);
    }

    @Override
    public VisTable buildWidgets() {
        VisTable group = new VisTable();

        group.add(new VisLabel(getTitle()));

        final VisSelectBox<Boolean> selectBox = new VisSelectBox<>();
        selectBox.setItems(true, false);

        try {
            selectBox.setSelected((boolean) getGetter().invoke(getComponent()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    getSetter().invoke(getComponent(), selectBox.getSelected());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        group.add(selectBox);

        return group;
    }
}
