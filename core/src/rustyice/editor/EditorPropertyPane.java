package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.editor.widgetbuilders.BoolWidgetBuilder;
import rustyice.editor.widgetbuilders.ColorWidgetBuilder;
import rustyice.editor.widgetbuilders.FloatWidgetBuilder;
import rustyice.editor.widgetbuilders.PropertyWidgetBuilder;
import rustyice.game.actors.Actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author gabek
 */
public class EditorPropertyPane {
    private VisTable root;
    private HorizontalFlowGroup tabGroup;
    private VisTable propertiesGroup;
    private ButtonGroup<VisTextButton> tabToggleGroup;

    private Actor selectedActor;

    public EditorPropertyPane(){
        root = new VisTable();
        tabGroup = new HorizontalFlowGroup();
        propertiesGroup = new VisTable();
        root.add(tabGroup).fillX().left().row();
        root.add(propertiesGroup).grow();

        tabToggleGroup = new ButtonGroup<>();

        propertiesGroup.align(Align.topLeft);
    }

    public VisTable getTable(){
        return root;
    }

    public void setSelected(Actor selectedActor){
        if(this.selectedActor != selectedActor){
            this.selectedActor = selectedActor;
            tabGroup.clear();
            tabToggleGroup.clear();
            propertiesGroup.clear();

            HashSet<Object> compSet = new HashSet<>();

            VisTextButton tab = new VisTextButton(selectedActor.getClass().getSimpleName(), "toggle");
            tab.addListener(new TabButtonListener(selectedActor));
            tabGroup.addActor(tab);
            tabToggleGroup.add(tab);

            for(Method method: selectedActor.getClass().getMethods()){
                if(getInheritedAnnotation(selectedActor.getClass(), method, ComponentAccess.class) != null){
                    try {
                        Object comp = method.invoke(selectedActor);
                        if(compSet.add(comp)){
                            tab = new VisTextButton(comp.getClass().getSimpleName(), "toggle");
                            tab.addListener(new TabButtonListener(comp));
                            tabGroup.addActor(tab);
                            tabToggleGroup.add(tab);
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

    private class TabButtonListener extends ChangeListener{
        private Object comp;

        public TabButtonListener(Object comp){
            this.comp = comp;
        }

        @Override
        public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {

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

            propertiesGroup.clear();
            Iterator<PropertyWidgetBuilder> iter = propertyMap.values().iterator();
            while(iter.hasNext()){
                propertiesGroup.add(iter.next().buildWidgets());

                if(iter.hasNext()){
                    propertiesGroup.add(new Separator()).pad(0, 10, 0, 10).prefHeight(35);
                }
            }
        }

    }
}