package rustyice.tools;

import java.io.File;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.kotcrab.vis.usl.USL;

public class TexturePack {
	public static void main(String args[]){
		System.out.println(USL.parse(new File("gui/gui.usl")));
	}
}
