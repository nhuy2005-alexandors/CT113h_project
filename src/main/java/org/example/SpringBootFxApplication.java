package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring Boot sẽ quét và khởi tạo tất cả các Bean (@Service, @Repository)
@SpringBootApplication
public class SpringBootFxApplication {
    // Lớp này chỉ cần đóng vai trò là điểm vào của Spring Context
    // Hàm main() của Spring sẽ được gọi từ BookManagerDesktopApp
}