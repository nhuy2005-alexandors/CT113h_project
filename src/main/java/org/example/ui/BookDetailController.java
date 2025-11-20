package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Author;
import org.example.model.Book;
import org.example.service.BookService; // CẦN THIẾT
import org.springframework.beans.factory.annotation.Autowired; // CẦN THIẾT
import org.springframework.dao.DuplicateKeyException; // CẦN THIẾT
import org.springframework.stereotype.Component;

@Component
public class BookDetailController {

    // --- TIÊM SERVICE VÀO CONTROLLER ---
    @Autowired
    private BookService bookService;

    // --- FXML UI Elements ---
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthorName;
    @FXML private TextField txtGenre;
    @FXML private TextField txtYear;

    private Stage dialogStage;
    private Book book;
    private boolean okClicked = false;

    // --- Thiết lập dữ liệu (Không thay đổi) ---

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBook(Book book) {
        this.book = book;
        okClicked = false;
        // Điền dữ liệu nếu là chế độ Sửa (UPDATE)
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            txtIsbn.setEditable(false); // KHÔNG CHO PHÉP SỬA ISBN KHI UPDATE
            txtIsbn.setText(book.getIsbn());
            txtTitle.setText(book.getTitle());
            txtYear.setText(String.valueOf(book.getPublicationYear()));
            txtGenre.setText(book.getGenre());
            if (book.getAuthor() != null) {
                txtAuthorName.setText(book.getAuthor().getName());
            }
        } else {
            txtIsbn.setEditable(true); // Cho phép sửa ISBN khi ADD
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    // --- Xử lý Sự kiện LƯU (Đã thêm logic Service và Bắt lỗi) ---

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            try {
                // 1. Cập nhật đối tượng Book từ các trường nhập liệu
                book.setIsbn(txtIsbn.getText().trim());
                book.setTitle(txtTitle.getText());

                // Thử chuyển đổi năm trước khi lưu
                int year = Integer.parseInt(txtYear.getText());
                book.setPublicationYear(year);

                book.setGenre(txtGenre.getText());

                // Cập nhật Author
                if (book.getAuthor() == null) {
                    book.setAuthor(new Author());
                }
                book.getAuthor().setName(txtAuthorName.getText());

                if (txtIsbn.isEditable()) { // nghĩa là đang thêm mới, không phải update
                    if (bookService.existsByIsbn(book.getIsbn())) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi Nghiệp vụ",
                                "ISBN đã tồn tại, vui lòng nhập mã khác.");
                        return;
                    }
                }
                // 2. GỌI SERVICE ĐỂ LƯU VÀ BẮT LỖI TRÙNG LẶP
                bookService.save(book);

                // Nếu lưu thành công:
                okClicked = true;
                dialogStage.close();

            } catch (NumberFormatException e) {
                // Lỗi nếu Năm xuất bản không phải là số
                showAlert(Alert.AlertType.ERROR, "Lỗi Dữ liệu", "Năm xuất bản phải là số hợp lệ.");

            } catch (Exception e) {
                // Bắt các lỗi DB khác (ví dụ: mất kết nối)
                showAlert(Alert.AlertType.ERROR, "Lỗi Lưu Dữ liệu", "Lỗi DB: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        okClicked = false;
        dialogStage.close();
    }

    // --- Hàm Hỗ trợ (Không thay đổi) ---
    private boolean isInputValid() {
        String errorMessage = "";

        // ... (Logic kiểm tra các trường khác) ...

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi Nhập liệu", errorMessage);
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}