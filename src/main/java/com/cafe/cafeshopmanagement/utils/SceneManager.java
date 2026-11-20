package com.cafe.cafeshopmanagement.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage mainStage;

    // Khởi tạo stage chính từ hàm start() của Application
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    // Hàm chuyển đổi màn hình dựa trên đường dẫn file FXML
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setTitle(title);
            mainStage.centerOnScreen();
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Lỗi tải màn hình: " + fxmlPath);
            e.printStackTrace();
        }
    }
}