package org.example.service;

import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Đánh dấu là Bean Service để Spring quản lý
public class BookService {

    private final BookRepository bookRepository;

    // Dependency Injection (Tiêm phụ thuộc) qua Constructor
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // CREATE / UPDATE
    public Book save(Book book) {
        // Có thể thêm logic kiểm tra dữ liệu, ví dụ: ISBN không trống
        return bookRepository.save(book);
    }

    // READ (Tất cả)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // READ (Theo ISBN)
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }


    // DELETE
    public void delete(String isbn) {
        // Thường xóa theo ID của Mongo, nhưng xóa theo ISBN sẽ tiện hơn cho UI
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        book.ifPresent(bookRepository::delete);
    }


}