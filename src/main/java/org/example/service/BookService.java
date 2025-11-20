package org.example.service;

import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
// Không cần import BookRepository 2 lần

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // CREATE / UPDATE
    // Chỉ thực hiện lưu, loại bỏ toàn bộ logic UI và try-catch.
    public Book save(Book book) {
        // Lệnh này sẽ ném ra DuplicateKeyException nếu ISBN trùng (do Unique Index)
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
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        book.ifPresent(bookRepository::delete);
    }

    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

}