package mllf.painter;

import mllf.gameengine.gameloop.Application;
import cs195n.Vec2i;
import mllf.painter.ui.MenuScreen;

public class Main {

	public Main() {
		
	}

	public static void main(String[] args) {
		Application app = new Application("Monochrome, formerly Buckingham Palettes", false, new Vec2i(800, 600));
		app.pushTopScreen(new MenuScreen(app));
		app.startup();
	}
}
