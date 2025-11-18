package org.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.model.Book;
import org.example.model.Author;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookManagerController {

    @Autowired
    private BookService bookService;

    // --- FXML UI ELEMENTS ---
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthorName;
    @FXML private TextField txtGenre;
    @FXML private TextField txtYear;

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> isbnCol;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, Integer> yearCol;

    private ObservableList<Book> bookList;

    // --- PHƯƠNG THỨC KHỞI TẠO ---

    @FXML
    public void initialize() {
        bookList = FXCollections.observableArrayList();
        bookTable.setItems(bookList);

        // Thiết lập ánh xạ cột
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorName")); // Yêu cầu getAuthorName() trong Book.java
        yearCol.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));

        loadBookData();

        // Gán sự kiện khi click vào một hàng (Gọi showBookDetails)
        bookTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBookDetails(newValue));
    }

    // --- CÁC THAO TÁC CƠ BẢN ---

    private void loadBookData() {
        bookList.clear();
        bookList.addAll(bookService.findAll());
    }

    // ĐÃ FIX LỖI CHÍNH TẢ: Đổi showBookDetamcils thành showBookDetails
    private void showBookDetails(Book book) {
        if (book != null) {
            txtIsbn.setText(book.getIsbn());
            txtTitle.setText(book.getTitle());
            txtYear.setText(String.valueOf(book.getPublicationYear()));
            txtGenre.setText(book.getGenre());

            if (book.getAuthor() != null) {
                txtAuthorName.setText(book.getAuthor().getName());
            } else {
                txtAuthorName.setText("");
            }
        }
    }

    // --- XỬ LÝ SỰ KIỆN TỪ NÚT BẤM ---

    @FXML
    private void handleAddBook() {
        try {
            // 1. Lấy dữ liệu và kiểm tra (Đã có .trim() cho ISBN)
            String isbn = txtIsbn.getText().trim();
            String title = txtTitle.getText();
            String authorName = txtAuthorName.getText();
            int year = Integer.parseInt(txtYear.getText());
            String genre = txtGenre.getText();

            // Tùy chọn: Thêm kiểm tra ISBN duy nhất tại đây nếu cần

            // 2. Tạo đối tượng Book và Author
            Book newBook = new Book();
            newBook.setIsbn(isbn);
            newBook.setTitle(title);
            newBook.setPublicationYear(year);
            newBook.setGenre(genre);
            newBook.setAuthor(new Author(null, authorName, null));

            // 3. Gọi Service để lưu
            bookService.save(newBook);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm sách mới thành công!");
            clearFields();
            loadBookData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Năm xuất bản phải là số!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi DB", "Không thể thêm sách: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một sách để cập nhật.");
            return;
        }

        try {
            // Cập nhật các trường
            selectedBook.setTitle(txtTitle.getText());
            selectedBook.setPublicationYear(Integer.parseInt(txtYear.getText()));
            selectedBook.setGenre(txtGenre.getText());

            // Cập nhật Author Name
            if (selectedBook.getAuthor() == null) {
                selectedBook.setAuthor(new Author());
            }
            selectedBook.getAuthor().setName(txtAuthorName.getText());

            // Gọi Service để lưu (thực hiện UPDATE vì selectedBook có ID)
            bookService.save(selectedBook);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sách thành công!");
            loadBookData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Năm xuất bản phải là số hợp lệ!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi DB", "Không thể cập nhật sách: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một sách để xóa.");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa sách ISBN: " + selectedBook.getIsbn() + "?", ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            bookService.delete(selectedBook.getIsbn()); // Xóa theo ISBN
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa sách thành công!");
            clearFields();
            loadBookData();
        }
    }

    @FXML
    private void clearFields() {
        txtIsbn.setText("");
        txtTitle.setText("");
        txtAuthorName.setText("");
        txtYear.setText("");
        txtGenre.setText("");
        bookTable.getSelectionModel().clearSelection();
    }

    // --- HÀM HỖ TRỢ ---

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}