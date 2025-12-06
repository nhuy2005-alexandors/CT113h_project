package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class SpringBootFxApplication {

    public static ConfigurableApplicationContext startSpring() {
        return new SpringApplicationBuilder(SpringBootFxApplication.class)
                .run();
    }

    // --- ĐOẠN CODE KIỂM TRA KẾT NỐI THÊM VÀO TẠI ĐÂY ---
    @Bean
    public CommandLineRunner checkMongoDBConnection(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                // Lấy tên database để kiểm tra kết nối
                String dbName = mongoTemplate.getDb().getName();

                System.out.println("\n==================================================");
                System.out.println("    KET NOI MONGODB THANH CONG!");
                System.out.println("    Dang su dung Database: " + dbName);
                System.out.println("==================================================\n");
            } catch (Exception e) {
                System.err.println("\n  LỖI KẾT NỐI MONGODB: " + e.getMessage() + "\n");
            }
        };
    }
    // ----------------------------------------------------

    public static void main(String[] args) {
        // Hàm main này có thể để trống nếu bạn chạy app từ BookManagerDesktopApp
    }
}