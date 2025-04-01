package baitapmau;

import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterContronller {
	//Khai báo các thành phần giao diện
	@FXML
	private TextField emailTf;
	@FXML
	private TextField fullnameTf;
	@FXML
	private ToggleGroup genderGroup;//Nhóm radio nam, nữ vào 1 nhóm
	@FXML
	private ChoiceBox<String> courseChoicebox; //Lưu ds khóa học
	@FXML
	private PasswordField passwordPf;//Lưu trữ mật khẩu dạng mã hóa
	@FXML
	private TextField passwordTf;//Lưu trũ mật khẩu dãng rõ
	@FXML
	private CheckBox displayCheckbox;//Lựa chọn xem mk dạng nào
	@FXML
	private Label messageLabel;//Nhãn tb Lỗi hoặc đk thành công
	@FXML
	private Hyperlink toHomeLink;//Link tới màn hình Home xuất hiện nếu đk thành công
	@FXML
	private Button registerButton;
	
	private UserDao userDao = new UserDao();
	private User loginedUser;//lưu trữ user đã đăng ký thành công, ghi vào usersession nếu cần
	
	//Phương thức khởi tạo cho ChoiceBox và các thành phần khác
	public void initialize() {
		//Dữ liệu ccho ChoiceBox khóa học(đối tượng cấp dữ liệu cho ChoiceBox là String)
		List<String> courses = Arrays.asList("Java","Python","C++","Web Development");
		
		//Thiết lập dữ liệu cho ChoiceBox
		courseChoicebox.getItems().addAll(courses);
		courseChoicebox.getSelectionModel().selectFirst();//Chọn mặc định khóa đầu tiên
	}
	
	@FXML
	private void onClickToHomeLink() {
		try {
			UserSession.createInstance(loginedUser.getEmail(),loginedUser.getFullname());
			//Thông qua 1 tp con của nó tìm ra cửa sổ
			Stage registerStage =(Stage)emailTf.getScene().getWindow();
			
			//Đóng cửa sổ
			registerStage.close();
			
			//Tạo FXMLLoader tương ứng HomeScene.fxml
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
			//Lấy về đối tượng root layout
			Parent root = (Parent) fxmlLoader.load();
			
			//Tạo Stage, Scene từ root
			Stage homeStage = new Stage();
			homeStage.setTitle("CLB TIN HỌC");
			homeStage.setScene(new Scene(root));
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Phương thức xử lý sự kiện khi checkbox "hiển thị" được thay đổi
	@FXML
	private void onClickDisplayCheckbox(ActionEvent event) {
		//Kiểm tra trạng thái của checkbox
		boolean isChecked = displayCheckbox.isSelected();
		
		//Xử lý thay đổi hiện thị mật khẩu
		handlePasswordVisibility(isChecked);
	}
	
	//Phương thức giúp thay đổi hiển thị mật khẩu khi checkbox thay đổi
	private void handlePasswordVisibility(boolean isChecked) {
		if(isChecked) {
			//Hiển thị mật khẩu dưới dạng văn bản
			passwordTf.setText(passwordPf.getText());
			passwordTf.setVisible(true);
			passwordPf.setVisible(false);
		}else {
			//ẩn mật khẩu, chỉ hiển thị dưới dạng mã hóa
			passwordPf.setText(passwordTf.getText());
			passwordPf.setVisible(true);
			passwordPf.setVisible(false);
		}
	}
	
	@FXML
	public void onClickRegister(ActionEvent event) {
		//Lấy dữ liệu từ form
		String email = emailTf.getText();
		String fullname = fullnameTf.getText();
		String password = passwordPf.getText();
		
		//Lấy về giới tính, vì không đặt fx:id cho radio nên ta phân biệt qua nhãn đi kèm radio(nam/nữ)
		String gender = ((RadioButton)genderGroup.getSelectedToggle()).getText();
		String course = courseChoicebox.getValue();
		//Kiểm tra tính hợp lệ của các trường nhập liệu
		String validationMessage = validateForm(email, fullname, password, course); 
		if(validationMessage != null) {
			//Nếu có lỗi trong việc validate, hiển thị thông báo lỗi và dừng lại
			showMessage(false, validationMessage);
			return;
		}
		
		//Kiểm tra email đã tồn tại chưa
		if(userDao.getUserByEmail(email) != null) {
			showMessage(false, "Email đã tồn tại!");
			return;
		}
		
		//Tạo đối tượng User từ dữ liệu nhập vào
		User newUser = new User(email, fullname, gender.equals("Nam"),course,password);
		loginedUser = newUser;//Lưu trữ để ghi vào session khi chuyển tới màn home
		
		//Thêm người dùng vào csdl
		boolean isAdded = userDao.addUser(newUser);
		
		//Hiển thị thông báo kết quả
		if(isAdded ) {
			showMessage(true, "Đăng kí thành công!");
		}else {
			showMessage(false, "Đăng kí không thành công!");
		}
	}
	
	//Phương thức kiểm tra tính hợp lệ của dữ liệu
	private String validateForm(String email, String fullname, String password, String course) {
		if(email==null || email.isEmpty() || !isEmailValid(email)) {
			return "Email không hợp lệ!";
		}
		if(fullname== null || fullname.isEmpty()) {
			return "Vui lòng nhập họ tên!";
		}
		if(password == null || password.isEmpty()) {
			return "Vui lòng nhập mật khẩu!";
		}
		if(course == null || course.isEmpty()) {
			return "Vui lòng chọn khóa học!";
		}
		return null;//Nếu tất cả đều hợp lệ, trả về null
	}
	
	//Phương thức kiểm tra tính hợp lệ email
	private boolean isEmailValid(String email) {
		//Demo đơn giản: kiểm tra xem email có chưa kí tự @ không
		return email != null && email.contains("@");
	}
	
	//Hiển thị message báo lỗi hoặc thành công
	private void showMessage(boolean messageType, String message) {
		if(messageType) {//messageType = true: đăng kí thành công
			messageLabel.setText(message);
			messageLabel.setTextFill(Color.BLUE);
			toHomeLink.setVisible(false);
		}else {
			messageLabel.setText(message);
			messageLabel.setTextFill(Color.RED);
			toHomeLink.setVisible(false);
		}
	}
}
