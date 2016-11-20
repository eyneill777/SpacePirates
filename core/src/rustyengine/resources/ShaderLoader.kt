package rustyengine.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.minlog.Log;

class ShaderLoader(fileHandleResolver: FileHandleResolver) :
        SynchronousAssetLoader<ShaderProgram, ShaderParameter>(fileHandleResolver) {

    override fun load(assetManager: AssetManager, fileName: String, file: FileHandle, parameter: ShaderParameter?):
            ShaderProgram {
        val dir = file.parent()

        val program = ShaderProgram(dir.child(file.name() + ".vert"), dir.child(file.name() + ".frag"))

        if (!program.isCompiled) {
            Log.error(program.log)
        }

        return program
    }

    override fun getDependencies(fileName: String, file: FileHandle, parameter: ShaderParameter?):
            Array<AssetDescriptor<Any>> = Array()
}
