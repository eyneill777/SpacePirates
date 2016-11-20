package rustyice.editor

import com.badlogic.gdx.scenes.scene2d.ui.Tree
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTree

import java.util.LinkedList
import kotlin.reflect.KClass

/**
 * @author gabek
 */
class SelectionTree{
    val tree: VisTree
    private val nodeStack: LinkedList<Tree.Node>
    val hasSelection: Boolean get() = !tree.selection.isEmpty

    init {
        tree = VisTree()
        nodeStack = LinkedList()
    }

    fun addLeaf(title: String, data: KClass<*>){
        val node = Tree.Node(VisLabel(title))
        node.`object` = data
        if(nodeStack.isEmpty()){
            tree.add(node)
        } else {
            nodeStack.last.add(node)
        }
    }

    fun pushBranch(title: String){
        val node = Tree.Node(VisLabel(title))
        node.isSelectable = false

        if(nodeStack.isEmpty()){
            tree.add(node)
        } else {
            nodeStack.last.add(node)
        }
        nodeStack.add(node)
    }

    fun popBranch() {
        nodeStack.remove()
    }
}
