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
	static Stack<SceneTitle> history;
	
	public static void initBack() {
		history = new Stack<SceneTitle>();
	}
	
	public static void back(Stage stage) {
		if(history.size() > 1) {
			history.pop();
		}

		SceneTitle s = history.peek();
        stage.setScene(s.getScene());
        stage.setTitle(s.getTitle());
	}
	
	public static void pushBack(Scene s, String title) {
		history.push(new SceneTitle(s, title));
	}
}