package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kotcrab.vis.ui.widget.*;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.editor.widgetbuilders.BoolWidgetBuilder;
import rustyice.editor.widgetbuilders.ColorWidgetBuilder;
import rustyice.editor.widgetbuilders.FloatWidgetBuilder;
import rustyice.editor.widgetbuilders.PropertyWidgetBuilder;
import rustyice.game.GameObject;

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

    private GameObject selectedObject;

    public EditorPropertyPane(){
        propertyTree = new VisTree();
        propertyTreeScroll = new VisScrollPane(propertyTree);
    }

    public WidgetGroup getRoot(){
        return propertyTreeScroll;
    }

    public void setSelected(GameObject selectedObject){
        if(this.selectedObject != selectedObject){
            this.selectedObject = selectedObject;
            propertyTree.clearChildren();


            propertyTree.add(buildCompEditNode(selectedObject));
            HashSet<Object> compSet = new HashSet<>();

            for(Method method: selectedObject.getClass().getMethods()){
                if(getInheritedAnnotation(selectedObject.getClass(), method, ComponentAccess.class) != null){
                    try {
                        Object comp = method.invoke(selectedObject);
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

                if(ColorWidgetBuilder.isSetter(method)) {
                    propertyMap.computeIfAbsent(title, s -> new ColorWidgetBuilder(comp, title)).setSetter(method);
                } else if(ColorWidgetBuilder.isGetter(method)){
                        propertyMap.computeIfAbsent(title, s -> new ColorWidgetBuilder(comp, title)).setGetter(method);
                }
                else if(FloatWidgetBuilder.isSetter(method)){
                    propertyMap.computeIfAbsent(title, s -> new FloatWidgetBuilder(comp, title)).setSetter(method);
                } else if(FloatWidgetBuilder.isGetter(method)) {
                    propertyMap.computeIfAbsent(title, s -> new FloatWidgetBuilder(comp, title)).setGetter(method);
                }
                else if(BoolWidgetBuilder.isSetter(method)){
                    propertyMap.computeIfAbsent(title, s -> new BoolWidgetBuilder(comp, title)).setSetter(method);
                } else if(BoolWidgetBuilder.isGetter(method)){
                    propertyMap.computeIfAbsent(title, s -> new BoolWidgetBuilder(comp, title)).setGetter(method);
                }
            }

        }

        for (PropertyWidgetBuilder propertyWidgetBuilder : propertyMap.values()) {
            VisTree.Node widgetNode = new VisTree.Node(propertyWidgetBuilder.buildWidgets());
            widgetNode.setSelectable(false);
            node.add(widgetNode);
        }

        return node;
    }

}
