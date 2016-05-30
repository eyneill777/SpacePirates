package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import rustyice.editor.annotations.ComponentAccess;
import rustyice.editor.annotations.ComponentProperty;
import rustyice.editor.widgetbuilders.ColorWidgetBuilder;
import rustyice.editor.widgetbuilders.FloatWidgetBuilder;
import rustyice.editor.widgetbuilders.PropertyWidgetBuilder;
import rustyice.game.actors.Actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author gabek
 */
public class EditorPropertyPane {
    private VisTable root;
    private HorizontalFlowGroup tabGroup;
    private HorizontalFlowGroup propertiesGroup;

    private Actor selectedActor;

    public EditorPropertyPane(){

        root = new VisTable();
        tabGroup = new HorizontalFlowGroup();
        propertiesGroup = new HorizontalFlowGroup();
        root.add(tabGroup).expandX().left().row();
        root.add(propertiesGroup).grow();
    }

    public VisTable getTable(){
        return root;
    }

    public void setSelected(Actor selectedActor){
        this.selectedActor = selectedActor;
        tabGroup.clear();
        propertiesGroup.clear();

        for(Method method: selectedActor.getClass().getMethods()){
            Annotation accessAnnotation = null;
            for(Annotation annotation: method.getAnnotations()){
                if(annotation instanceof ComponentAccess){
                    accessAnnotation = annotation;
                    break;
                }
            }

            if(accessAnnotation != null){
                try {
                    Object comp = method.invoke(selectedActor);
                    VisTextButton tab = new VisTextButton(comp.getClass().getSimpleName(), "toggle");
                    tab.addListener(new TabButtonListener(comp));
                    tabGroup.addActor(tab);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

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
                for(Annotation annotation: method.getAnnotations()){
                    if(annotation instanceof ComponentProperty){
                        ComponentProperty propertyAnno = (ComponentProperty)annotation;
                        String title = propertyAnno.title();

                        if(ColorWidgetBuilder.isColorField(method)){
                            propertyMap.putIfAbsent(title, new ColorWidgetBuilder(comp, title));
                        } else if(FloatWidgetBuilder.isFloatField(method)){
                            propertyMap.putIfAbsent(title, new FloatWidgetBuilder(comp, title));
                        }

                        propertyMap.get(title).addMethod(method);
                    }
                }
            }

            propertiesGroup.clear();
            for(PropertyWidgetBuilder widgetBuilder: propertyMap.values()){
                widgetBuilder.buildWidgets(propertiesGroup);
            }
        }

    }
}
