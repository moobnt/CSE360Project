package project.util;

import javafx.scene.Scene;

public class SceneTitle {
    private Scene scene;
    private String title;

    public SceneTitle(Scene scene, String title) {
        this.scene = scene;
        this.title = title;
    }

    public Scene getScene() {
        return scene;
    }

    public String getTitle() {
        return title;
    }
}