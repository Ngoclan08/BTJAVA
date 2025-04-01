package baitapmau;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginContronller {
	@FXML
	private TextField emailTf;
	@FXML
	private PasswordField passwordPf;
	@FXML
	private Label errorLabel;

	private UserDao userDao; // Đối tượng UserDao để tương tác với csdl

	// Phương thức chạy khởi tạo khi contronller được tạo
	public void initialize() {
		userDao = new UserDao();
		errorLabel.setVisible(false);// Ẩn "labbel nhãn lỗi" khi mới khởi tạo
	}

	// Phương thức xử lý sự kiện khi người dùng nhấn nút "login"
	@FXML
	private void onClickLogin() {
		// Lấy thông tin nhập từ giao diên
		String email = emailTf.getText().trim();
		String password = passwordPf.getText().trim();

		// Kiểm tra tính hợp lệ của form login
		if (email.isEmpty() || password.isEmpty()) {
			errorLabel.setText("Email và mật khẩu không được để trống");
			errorLabel.setVisible(true);// Hiển thị nhãn lỗi
			return;// Ngắt chạy đoạn code bên dưới
		}

		// Kiểm tra người dùng theo email, password trong csdl
		User user = userDao.getUserByEmail(email);
		if (user == null || !user.getPassword().equals(password)) {
			errorLabel.setText("Tài khoản nhập chưa đúng");
			errorLabel.setVisible(true);
		} else {
			// Đăng nhập thành công,ẩn nhãn lỗi
			errorLabel.setVisible(false);

			// Ghi dl User Session
			UserSession.createInstance(email, user.getFullname());

			// Chuyển màn hình Home
			goHomeScreen();
		}
	}

	// Phương thức xử lý sự kiện khhi người dùng nhấn vào link" ĐK tài khoản"
	// Đóng cửa sổ login hiện tại, mở cửa sổ màn hình đăng ký
	@FXML
	private void onClickRegisterLink() {
		try {
			// Đóng cửa sổ login hiện tại thông qua q thành phần con cảu nó tìm ra cửa sổ
			Stage loginStage = (Stage) emailTf.getScene().getWindow();
			loginStage.close();

			// Lấy về đối tượng root
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
			Parent root = loader.load();

			// Tạo Stage, Scene từ root
			Stage homeStage = new Stage();
			homeStage.setTitle("Đăng kí khóa học");
			homeStage.setScene(new Scene(root));
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Chuyển tới màn hình Home
	private void goHomeScreen() {
		try {
			//Đóng cửa sổ login hiện tại thông qua 1 tp con của nó tìm ra cửa sổ
			Stage loginStage =(Stage)emailTf.getScene().getWindow();
			loginStage.close();
			
			//Tạo FXMLLoader tương ứng HomeScene.fxml
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
			//Lấy về đối tượng root layout
			Parent root =(Parent)fxmlLoader.load();
			
			//Tạo Stage, Scnene từ root
			Stage homeStage = new Stage();
			homeStage.setTitle("CLB TIN HỌC");
			homeStage.setScene(new Scene(root));
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
