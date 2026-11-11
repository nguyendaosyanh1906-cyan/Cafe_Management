module com.cafe.cafeshopmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt; // Cần thiết cho JDBC kết nối MySQL

    // Mở các package cho JavaFX FXML truy cập qua Reflection
    opens com.cafe.cafeshopmanagement to javafx.fxml;
    opens com.cafe.cafeshopmanagement.controller to javafx.fxml;

    // Export các package để ứng dụng sử dụng
    exports com.cafe.cafeshopmanagement;
    exports com.cafe.cafeshopmanagement.controller;
    exports com.cafe.cafeshopmanagement.model;
    exports com.cafe.cafeshopmanagement.dao;
    exports com.cafe.cafeshopmanagement.utils;
}