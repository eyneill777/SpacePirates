package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kotcrab.vis.ui.widget.*;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.editor.widgetbuilders.BoolWidgetBuilder;
import rustyice.editor.widgetbuilders.ColorWidgetBuilder;
import rustyice.editor.widgetbuilders.FloatWidgetBuilder;
import rustyice.editor.widgetbuilders.PropertyWidgetBuilder;
import rustyice.game.Actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author gabek
 */
public class EditorPropertyPane {
    private VisScrollPane propertyTreeScroll;
    private VisTree propertyTree;

    private Actor selectedActor;

    public EditorPropertyPane(){
        propertyTree = new VisTree();
        propertyTreeScroll = new VisScrollPane(propertyTree);
    }

    public WidgetGroup getRoot(){
        return propertyTreeScroll;
    }

    public void setSelected(Actor selectedActor){
        if(this.selectedActor != selectedActor){
            this.selectedActor = selectedActor;
            propertyTree.clearChildren();


            propertyTree.add(buildCompEditNode(selectedActor));
            HashSet<Object> compSet = new HashSet<>();

            for(Method method: selectedActor.getClass().getMethods()){
                if(getInheritedAnnotation(selectedActor.getClass(), method, ComponentAccess.class) != null){
                    try {
                        Object comp = method.invoke(selectedActor);
                        if(compSet.add(comp)){
                            propertyTree.add(buildCompEditNode(comp));
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private <T extends Annotation> T getInheritedAnnotation(Class cls, Method mth, Class<T> anno){
        T out = mth.getAnnotation(anno);

        //check superclasses
        Class superclass = cls.getSuperclass();
        while (superclass != null && out == null){
            for(Method method: superclass.getDeclaredMethods()){
                if(method.getName().equals(mth.getName())
                        && Arrays.equals(method.getParameterTypes(), mth.getParameterTypes())
                        && method.getAnnotation(anno) != null){
                    out = method.getAnnotation(anno);
                    break;
                }
            }
            superclass = superclass.getSuperclass();
        }

        return out;
    }

    private VisTree.Node buildCompEditNode(Object comp) {

        VisTree.Node node = new VisTree.Node(new VisLabel(comp.getClass().getSimpleName()));
        node.setSelectable(false);

        HashMap<String, PropertyWidgetBuilder> propertyMap = new HashMap<>();

        for(Method method: comp.getClass().getMethods()){
            ComponentProperty propertyAnno = getInheritedAnnotation(comp.getClass(), method, ComponentProperty.class);

            if(propertyAnno != null){
                String title = propertyAnno.title();

                if(ColorWidgetBuilder.isColorField(method)){
                    propertyMap.putIfAbsent(title, new ColorWidgetBuilder(comp, title));
                } else if(FloatWidgetBuilder.isFloatField(method)){
                    propertyMap.putIfAbsent(title, new FloatWidgetBuilder(comp, title));
                } else if(BoolWidgetBuilder.isBoolField(method)){
                    propertyMap.putIfAbsent(title, new BoolWidgetBuilder(comp, title));
                }

                propertyMap.get(title).addMethod(method);
            }

        }

        for (PropertyWidgetBuilder propertyWidgetBuilder : propertyMap.values()) {
            node.add(new VisTree.Node(propertyWidgetBuilder.buildWidgets()));
        }

        return node;
    }

}
