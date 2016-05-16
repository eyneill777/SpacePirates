package rustyice.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.minlog.Log;

public class ShaderLoader extends SynchronousAssetLoader<ShaderProgram, ShaderParameter>{

    public ShaderLoader(FileHandleResolver resolver) {
        super(resolver);
        
    }

    @Override
    public ShaderProgram load(AssetManager assetManager, String fileName, FileHandle file, ShaderParameter parameter) {
        FileHandle dir = file.parent();
        
        ShaderProgram program = new ShaderProgram(dir.child(file.name() + ".vert"), dir.child(file.name() + ".frag"));
        
        if(!program.isCompiled()){
            Log.error(program.getLog());
        }
        
        return program;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderParameter parameter) {
        return null;
    }

}
