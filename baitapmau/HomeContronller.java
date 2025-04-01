package baitapmau;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HomeContronller {
	@FXML
	private TableView<User> userListTableView;
	@FXML
	private TableColumn<User, Integer> idCol;
	@FXML
	private TableColumn<User, String> emailCol;
	@FXML
	private TableColumn<User, String> fullnameCol;
	@FXML
	private TableColumn<User, String> courseCol;

	@FXML
	private Label welcomeLabel;// Nhãn xin chào + user đang đăng nhập
	// Khai báo quản lý các trường của khối thông tin chi tiết
	@FXML
	private TextField emailTf;
	@FXML
	private TextField fullnameTf;
	@FXML
	private ChoiceBox<String> courseChoicebox;
	@FXML
	private RadioButton maleRadio;
	@FXML
	private RadioButton femaleRadio;
	@FXML
	private ToggleGroup genderGroup;
	@FXML
	private StackPane passwordStackPane;
	@FXML
	private PasswordField passwordPf;
	@FXML
	private TextField passwordTf;
	@FXML
	private CheckBox displayCheckbox;

	@FXML
	private Button addbtn;
	@FXML
	private Button updatebtn;
	@FXML
	private Button deletebtn;

	private UserSession userSession; // Lấy tham chiếu UserSession để sử dụng nhiều lần
	private UserDao userDao = new UserDao();

	// Phương thức khởi tạo hiện thị ds người dùng
	public void initialize() {
		//  Nếu chưa qua đn hoặc đk sẽ có ngoại lệ không cho vào màn hình home
		userSession = UserSession.getInstance();

		//  Thiết lập nhãn xin chào
		welcomeLabel.setText("Xin chào " + userSession.getFullname());

		// giả định có user cso quyền admin ngầm định :admin@gmail.com
		// sẽ có thể vào màn home qua chức năng login
		if ("admin@gmail.com".equals(userSession.getEmail())) {
			// Nếu là admin, cho phép tát cả thao tác
			addbtn.setDisable(false);
			updatebtn.setDisable(false);
			deletebtn.setDisable(false);
			passwordStackPane.setDisable(false);
			displayCheckbox.setDisable(false);
		}

		// Bắt cặp các cột của Tableview với các chương trình của đối tượng user cung
		// cấp dl
		idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("userId"));
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		fullnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("fullname"));
		courseCol.setCellValueFactory(new PropertyValueFactory<User, String>("course"));

		// Lấy ds người dùng và hiển thị
		loadUserList();

		// Khởi tạo dl cho ChoiceBox khóa học
		List<String> courses = Arrays.asList("Java", "Pyhton", "C++", "Web Development");
		courseChoicebox.getItems().addAll(courses);
	}

	// Tải ds người dùng từ csdl và hiển thị lên tableview
	private void loadUserList() {
		// Chuyển sang dạng ObservableList để phù hợp với tableview
		ObservableList<User> users = FXCollections.observableArrayList(userDao.getAllUsers());
		userListTableView.setItems(users);
	}

	@FXML
	private void onClickLogout(ActionEvent event) {
		// Xóa UserSession
		userSession.cleanUserSession();

		// Lấy đối tượng Stage từ scene hiện tại thông qua đối tượng evnet
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		// Đóng cửa sổ
		stage.close();
	}

	// Phương thức xử lý sự kiện khi checkbox "Hiển thị" được thay đổi
	@FXML
	private void onClickDisplayCheckbox(ActionEvent event) {
		// Kiểm tra trạng thái cuả checkbox
		boolean isChecked = displayCheckbox.isSelected();

		// Xử lý thay đổi hiển thị mật khẩu
		handlePasswordVisiblity(isChecked);
	}

	// Phương thức thay đổi mk khi checkbox
	private void handlePasswordVisiblity(boolean isChecked) {
		if (isChecked) {
			passwordTf.setText(passwordPf.getText());
			passwordTf.setVisible(true);
			passwordPf.setVisible(false);
		} else {
			// ẩn mk, chỉ hiện dạng dấu chấm
			passwordPf.setText(passwordTf.getText());
			passwordPf.setVisible(true);
			passwordTf.setVisible(false);
		}
	}

	// Thêm mới người dùng
	@FXML
	private void onClickAdd() {
		String email = emailTf.getText();
		String fullname = fullnameTf.getText();
		boolean gender = maleRadio.isSelected();
		String course = courseChoicebox.getValue();

		String password = displayCheckbox.isSelected() ? passwordTf.getText() : passwordPf.getText();

		User newUser = new User(email, fullname, gender, course, password);

		if (userDao.addUser(newUser)) {
			loadUserList();// Cập nhật ds
			showAlert(AlertType.INFORMATION, "Thành công", "Thêm người dùng thành công!");
			clearForm();// Xóa các trường nhập liệu
		} else {
			showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi thêm người dùng!");
		}
	}

	// Cập nhật thông tin người dùng
	@FXML
	private void onClickUpdate() {
		User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
		if (selectedUser != null) {
			selectedUser.setEmail(emailTf.getText());
			selectedUser.setFullname(fullnameTf.getText());
			selectedUser.setGender(maleRadio.isSelected());
			selectedUser.setCourse(courseChoicebox.getValue());
			selectedUser.setPassword(displayCheckbox.isSelected() ? passwordTf.getText() : passwordPf.getText());

			// Đối tượng User truyền vào như khi thêm mới vì trong selectedUser có chứa
			// userId
			if (userDao.updateUser(selectedUser)) {
				loadUserList();// Cập nhật lại ds
				showAlert(AlertType.INFORMATION, "Thành công", "Cập nhật thành công!");
				clearForm();
			} else{
				showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi cập nhật người dùng! ");
			}
		}else {
				showAlert(AlertType.ERROR, "Lỗi", "Chưa chọn người dùng để cập nhật!");
			}
		}

	// Xóa người dùng
	@FXML
	private void onClickDelete() {
		User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
		if (selectedUser != null) {
			//Chỉ admin có quyền xóa, k cho admin tự xóa chính minhf
			if("admin@gmail.com".equals(selectedUser.getEmail())) {
				showAlert(AlertType.ERROR, "Lỗi", "Không thể xóa tài khoản super admin");
				return;
			}
			
			Optional<ButtonType> option = showAlert(AlertType.CONFIRMATION, "Xác minh", "Bạn chắc chắn muốn xóa");
			if (option.get() == ButtonType.OK) {
				if (userDao.deleteUser(selectedUser.getUserId())) {
					loadUserList();
					showAlert(AlertType.INFORMATION, "Thành công", "Xóa thành công");
					clearForm();
				} else {
					showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi xóa người dùng");
				}
			}
		} else {
			showAlert(AlertType.ERROR, "Lỗi", "Chưa chọn người dùng để xóa");
		}
	}

	@FXML
	private void onClickRow(MouseEvent event) {
		// Lấy về user được chọn hiện tại
		User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
		if (selectedUser != null) {
			showUserDetails(selectedUser);// Hiển thị chi tiết người dùng
		}
	}

	// Hiển thị thông tin chi tiết người dùng khi chọn 1 dòng
	private void showUserDetails(User user) {
		emailTf.setText(user.getEmail());
		fullnameTf.setText(user.getFullname());
		courseChoicebox.setValue(user.getCourse());
		if (user.isGender()) {
			maleRadio.setSelected(true);// true :nam
		} else {
			femaleRadio.setSelected(true);
		}

		// Ghi dl vào 2 trường
		passwordPf.setText(user.getPassword());
		passwordTf.setText(user.getPassword());
		
		//Xử lý vô hiệu hóa hay k nút cập nhật nếu user kp admin
		if(!"admin@gmail.com".equals(userSession.getEmail())) {
			if(userSession.getEmail().equals(user.getEmail())) {
				//Chỉ cho chỉnh sửa chính mình
				passwordStackPane.setDisable(false);
				displayCheckbox.setDisable(false);
				updatebtn.setDisable(false);
			}else {
				//đối với tt khác của user đang nhập
				passwordStackPane.setDisable(true);//Không cho thao tác khối mk
				displayCheckbox.setDisable(true);//k cho thao tác checkbox
				passwordTf.clear();//k hiển thị mk
				passwordPf.clear();
				updatebtn.setDisable(true);
			}
		}
	}

	// Xóa các trường nhập liệu
	private void clearForm() {
		emailTf.clear();
		fullnameTf.clear();
		courseChoicebox.setValue(null);
		maleRadio.setSelected(false);
		femaleRadio.setSelected(false);
		passwordPf.clear();
		passwordTf.clear();
	}

	//Phương thức hiển thị hộp thoại thông báo
	private Optional<ButtonType> showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		return alert.showAndWait();
	}
}
