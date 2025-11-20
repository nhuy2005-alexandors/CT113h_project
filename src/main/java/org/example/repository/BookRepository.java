package org.example.repository;

import org.example.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
// Đây là lớp Data Access
// Spring Data sẽ tự động triển khai các phương thức CRUD
public interface BookRepository extends MongoRepository<Book, String> {

    // Phương thức tùy chỉnh: Tìm kiếm theo ISBN (Khóa nghiệp vụ)
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

}