package project.util;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p> Back class </p>
 * 
 * <p> Description: The handler for the back button
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class Back {
	static Stack<Scene> history;
	
	public static void initBack() {
		history = new Stack<Scene>();
	}
	
	public static Scene back(Stage stage) {
		history.pop();
		Scene s = history.peek();
		return s;
	}
	
	public static void pushBack(Scene s) {
		history.push(s);
	}
}