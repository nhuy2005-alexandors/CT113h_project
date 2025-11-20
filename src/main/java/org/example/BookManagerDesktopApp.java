package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class BookManagerDesktopApp extends Application {

    // Khai báo biến Spring Context ở cấp độ class
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        // SỬA: Khởi động Spring Boot Context. Thay App.class bằng tên lớp @SpringBootApplication của bạn
        springContext = SpringApplication.run(App.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 1. Tải file FXML UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookManagerUI.fxml"));

        // 2. Thiết lập Controller Factory (Sử dụng springContext đã khởi tạo)
        loader.setControllerFactory(springContext::getBean);

        // 3. Load Parent (root node) và tạo Scene
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // 4. Thiết lập Stage và hiển thị
        primaryStage.setTitle("Quản Lý Sách (Spring + JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Đóng Context Spring khi ứng dụng Desktop đóng
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }

    public static void main(String[] args) {
        // Khởi động JavaFX, từ đó gọi init() và start()
        launch(BookManagerDesktopApp.class, args);
    }
}