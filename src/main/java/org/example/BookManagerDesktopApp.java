package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ui.BookManagerController; // Cần import Controller
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class BookManagerDesktopApp extends Application {

    private ConfigurableApplicationContext springContext;

    // ... (Phần init() giữ nguyên) ...
    @Override
    public void init() throws Exception {
        // KHỞI ĐỘNG SPRING BOOT CONTEXT
        springContext = SpringApplication.run(SpringBootFxApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 1. Tải file FXML UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookManagerUI.fxml"));

        // 2. Thiết lập Controller Factory
        // Dùng Spring Context để tạo Controller, giúp Spring có thể @Autowired BookService vào Controller
        loader.setControllerFactory(springContext::getBean);

        // 3. Load Parent (root node) và tạo Scene
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // 4. Thiết lập Stage và hiển thị
        primaryStage.setTitle("Quản Lý Sách (Spring + JavaFX)");
        primaryStage.setScene(scene);

        // primaryStage.setWidth(800);
        // Kích thước thường được đặt trong FXML
        // primaryStage.setHeight(600);

        primaryStage.show();
    }

    // ... (Phần stop() và main() giữ nguyên) ...
    @Override
    public void stop() {
        // Đóng Context Spring khi ứng dụng Desktop đóng
        springContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        // Khởi động JavaFX, từ đó gọi init() và start()
        launch(BookManagerDesktopApp.class, args);
    }
}