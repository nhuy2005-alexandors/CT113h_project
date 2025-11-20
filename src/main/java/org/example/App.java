package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example"}) // Đảm bảo Spring quét các components và controllers
@EnableMongoRepositories(basePackages = "org.example.repository") // Đảm bảo quét các Repository
public class App {
    // Lớp này không cần hàm main() vì nó được gọi bởi BookManagerDesktopApp.init()
}