package org.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class BookManagerController implements ApplicationContextAware {

    @Autowired
    private BookService bookService;

    // --- Cần để mở cửa sổ Pop-up bằng Spring Bean ---
    private ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }
    // --- Kết thúc phần Spring Context ---


    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> isbnCol;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, Integer> yearCol;
    @FXML private TableColumn<Book, String> genreCol;

    private ObservableList<Book> bookList;

    // --- PHƯƠNG THỨC KHỞI TẠO ---

    @FXML
    public void initialize() {
        bookList = FXCollections.observableArrayList();
        bookTable.setItems(bookList);

        // Thiết lập ánh xạ cột
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        loadBookData();

        // Gấp đôi click để Sửa sách
        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && bookTable.getSelectionModel().getSelectedItem() != null) {
                handleUpdateBook();
            }
        });
    }

    // --- LOGIC CHÍNH ---

    @FXML
    private void loadBookData() {
        bookList.clear();
        bookList.addAll(bookService.findAll());
    }

    @FXML
    private void handleAddBook() {
        Book tempBook = new Book();

        // Kiểm tra xem người dùng đã nhấn OK trong popup chưa
        if (showBookDetailDialog(tempBook, "Thêm Sách Mới")) {
            try {
                bookService.save(tempBook);
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm sách mới thành công!");
                loadBookData();
            } catch (org.springframework.dao.DuplicateKeyException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Nghiệp vụ", "Mã ISBN này đã tồn tại. Vui lòng kiểm tra lại.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi DB", "Không thể thêm sách: " + e.getMessage());
            }
        } else {
            // Thêm thông báo khi người dùng nhấn Hủy/Đóng
            showAlert(Alert.AlertType.WARNING, "Đã hủy", "Hủy thêm sách mới.");
        }
    }

    @FXML
    private void handleUpdateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một sách để cập nhật.");
            return;
        }

        // Mở popup trực tiếp với selectedBook
        if (showBookDetailDialog(selectedBook, "Sửa Thông Tin Sách")) {
            try {
                // Save lại selectedBook → UPDATE vào DB
                bookService.save(selectedBook);
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sách thành công!");
                loadBookData();
            } catch (org.springframework.dao.DuplicateKeyException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Nghiệp vụ", "Mã ISBN này đã tồn tại.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi DB", "Không thể cập nhật sách: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Đã hủy", "Hủy cập nhật sách.");
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
            // Logic xóa đã được chứng minh là đúng nếu tên package Repository đã được đồng bộ
            bookService.delete(selectedBook.getIsbn());
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa sách thành công!");
            loadBookData();
        }
    }

    // --- HÀM MỞ POPUP (QUAN TRỌNG) ---

    private boolean showBookDetailDialog(Book book, String title) {
        try {
            // 1. Tải FXML và sử dụng Spring Controller Factory
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookDetail.fxml"));
            loader.setControllerFactory(springContext::getBean); // Cho phép Spring quản lý BookDetailController

            Parent page = loader.load();

            BookDetailController controller = loader.getController();
            controller.setBook(book); // Truyền đối tượng Book

            // 2. Thiết lập Stage (cửa sổ Popup)
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookTable.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            controller.setDialogStage(dialogStage);

            // 3. Hiển thị và đợi (modal)
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi UI", "Không tải được cửa sổ chi tiết: " + e.getMessage());
            return false;
        }
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