package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gabek
 */
public class FloatWidgetBuilder extends PropertyWidgetBuilder{
    public FloatWidgetBuilder(Object component, String title) {
        super(component, title);
    }

    public static boolean isFloatField(Method method){
        return (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(float.class)) ||
                (method.getReturnType().equals(float.class));
    }

    @Override
    public void addMethod(Method method) {
        if(method.getReturnType().equals(float.class)){
            setGetter(method);
        } else {
            setSetter(method);
        }
    }

    @Override
    public VisTable buildWidgets() {
        VisTable group = new VisTable();

        group.add(new VisLabel(getTitle())).spaceRight(5);
        final VisValidatableTextField numberField = new VisValidatableTextField(new Validators.FloatValidator());

        try {
            numberField.setText(String.valueOf( getGetter().invoke(getComponent())));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        group.add(numberField).prefWidth(35);

        VisTextButton applyButt = new VisTextButton("Apply");
        applyButt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(numberField.isInputValid()){
                    try {
                        getSetter().invoke(getComponent(), Float.valueOf(numberField.getText()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        group.add(applyButt);
        return group;
    }
}
