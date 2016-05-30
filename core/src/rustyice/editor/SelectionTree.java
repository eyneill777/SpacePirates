package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTree;

import java.util.LinkedList;

/**
 * @author gabek
 */
public class SelectionTree{
    private VisTree tree;
    private LinkedList<Tree.Node> nodeStack;

    public SelectionTree(){
        tree = new VisTree();
        nodeStack = new LinkedList<>();
    }

    public void addLeaf(String title, Class<?> data){
        Tree.Node node = new Tree.Node(new VisLabel(title));
        node.setObject(data);
        if(nodeStack.isEmpty()){
            tree.add(node);
        } else {
            nodeStack.getLast().add(node);
        }
    }

    public void pushBranch(String title){
        Tree.Node node = new Tree.Node(new VisLabel(title));
        node.setSelectable(false);

        if(nodeStack.isEmpty()){
            tree.add(node);
        } else {
            nodeStack.getLast().add(node);
        }
        nodeStack.add(node);
    }

    public void popBranch(){
        nodeStack.remove();
    }

    public VisTree getTree(){
        return tree;
    }

    public boolean hasSelection(){
        return !tree.getSelection().isEmpty();
    }
}
