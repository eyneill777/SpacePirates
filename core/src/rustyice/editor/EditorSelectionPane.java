package rustyice.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import rustyice.game.actors.Player;
import rustyice.game.actors.TestActor;
import rustyice.game.lights.PointLight;
import rustyice.game.tiles.FloorTile;
import rustyice.game.tiles.Tile;
import rustyice.game.tiles.WallTile;

import java.util.EnumSet;

/**
 * @author gabek
 */
class EditorSelectionPane {
    private VisTable table;
    private SelectionTree actorTree;
    private SelectionTree tileTree;
    private VisScrollPane selectionScroll;

    private ButtonGroup<TextButton> modeGroup;
    private VisTextButton actorModeButt;
    private VisTextButton tileModeButt;
    private Mode mode;

    public EditorSelectionPane(){
        load();
    }

    private void load(){
        actorModeButt = new VisTextButton("Actors", "toggle");
        tileModeButt = new VisTextButton("Tiles", "toggle");

        ModeChangeListener modeListener = new ModeChangeListener();
        actorModeButt.addListener(modeListener);
        tileModeButt.addListener(modeListener);

        modeGroup = new ButtonGroup<>(actorModeButt, tileModeButt);
        modeGroup.setMaxCheckCount(1);
        modeGroup.setMinCheckCount(1);

        actorTree = new SelectionTree();
        tileTree = new SelectionTree();

        selectionScroll = new VisScrollPane(actorTree.getTree());
        selectionScroll.setScrollingDisabled(true, false);
        selectionScroll.setFadeScrollBars(false);

        table = new VisTable();
        table.add(actorModeButt).fillX();
        table.add(tileModeButt).fillX();
        table.row();

        table.add(selectionScroll).colspan(2).grow();

        buildActorTree();
        buildTileTree();

        setMode(Mode.TILES);
    }

    private void buildActorTree(){
        actorTree.addLeaf("Player", Player.class);
        actorTree.addLeaf("Test Object", TestActor.class);

        actorTree.pushBranch("Lights");
        actorTree.addLeaf("Point Light", PointLight.class);
        actorTree.popBranch();
    }

    private void buildTileTree(){
        tileTree.pushBranch("Floors");
        tileTree.pushBranch("boring");
        tileTree.addLeaf("normal", FloorTile.class);
        tileTree.popBranch();
        tileTree.popBranch();

        tileTree.pushBranch("Walls");
        tileTree.addLeaf("Wall", WallTile.class);
        tileTree.popBranch();
    }

    public VisTable getTable(){
        return table;
    }

    public Mode getMode(){
        return mode;
    }

    public void setMode(Mode mode){
        switch (mode){
            case ACTORS:
                actorModeButt.setChecked(true);
                break;
            case TILES:
                tileModeButt.setChecked(true);
                break;
        }
    }

    public boolean isActorMode(){
        return mode == Mode.ACTORS;
    }

    public boolean isTileMode(){
        return mode == Mode.TILES;
    }

    public boolean hasSelectedTile(){
        return tileTree.hasSelection() && mode == Mode.TILES;
    }

    public boolean hasSelectedActor(){
        return actorTree.hasSelection() && mode == Mode.ACTORS;
    }

    public rustyice.game.actors.Actor buildSelectedActor(){
        try {
            Class<?> actorClass = (Class<?>) actorTree.getTree().getSelection().getLastSelected().getObject();
            return (rustyice.game.actors.Actor) actorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    public Tile buildSelectedTile(){
        try {
            Class<?> tileClass = (Class<?>) tileTree.getTree().getSelection().getLastSelected().getObject();
            return (Tile) tileClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e){
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    private class ModeChangeListener extends ChangeListener{
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if(actor == actorModeButt){
                mode = Mode.ACTORS;
                selectionScroll.setWidget(actorTree.getTree());
            } else if(actor == tileModeButt){
                mode = Mode.TILES;
                selectionScroll.setWidget(tileTree.getTree());
            }
        }
    }

    private enum Mode {
        ACTORS, TILES
    }
}
