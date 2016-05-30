package rustyice.editor.widgetbuilders;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import java.lang.reflect.Method;

/**
 * @author gabek
 */
public abstract class PropertyWidgetBuilder {
    private Object component;
    private Method getter;
    private Method setter;

    private String title;

    public PropertyWidgetBuilder(Object component, String title){
        this.component = component;
        this.title = title;
    }

    public void setSetter(Method setter){
        this.setter = setter;
    }

    public void setGetter(Method getter){
        this.getter = getter;
    }

    public Object getComponent() {
        return component;
    }

    public void setComponent(Object component) {
        this.component = component;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract void addMethod(Method method);

    public abstract void buildWidgets(WidgetGroup group);
}
