package com.cafe.cafeshopmanagement;

import com.cafe.cafeshopmanagement.utils.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Lưu lại Stage chính để SceneManager chuyển cảnh về sau
        SceneManager.setMainStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/cafe/cafeshopmanagement/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cafe Shop Management - Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}