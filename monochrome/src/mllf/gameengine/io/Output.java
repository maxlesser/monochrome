package mllf.gameengine.io;

import java.util.ArrayList;
import java.util.List;

public class Output {

	private List<Connection> connections;
	
	public Output() {
		connections = new ArrayList<>();
	}
	
	public void connect(Connection c) {
		connections.add(c);
	}
	
	public void disconnect(Connection c) {
		connections.remove(c);
	}
	
	public void run() {
		for (Connection c : connections) {
			c.run();
		}
	}

}
