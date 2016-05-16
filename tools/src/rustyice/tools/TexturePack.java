package rustyice.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.kotcrab.vis.usl.Main;

public class TexturePack {
	public static void main(String args[]) {
		TexturePacker.process("assets-raw/gui/x1", "core/assets/gui", "uiskin");
		TexturePacker.process("assets-raw/game-art", "core/assets", "art");

		String[] uslArgs = { "assets-raw/gui/uiskin.usl", "core/assets/gui/uiskin.json" };
		Main.main(uslArgs);
	}
}
