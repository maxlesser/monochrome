package mllf.gameengine.io;

import java.util.Map;

public abstract class Input {

	public Input() {}

	public abstract void run(Map<String, String> args);

}