package project;

import java.util.Stack;

import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Back {
	static Stack<Scene> history;
	
	static void initBack() {
		history = new Stack<Scene>();
	}
	
	static Scene back(Stage stage) {
		history.pop();
		Scene s = history.peek();
		return s;
	}
	
	static void pushBack(Scene s) {
		history.push(s);
	}
}