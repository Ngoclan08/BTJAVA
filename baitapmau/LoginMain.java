package baitapmau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Load FXML file ra đối tượng parent đóng vai trò vùng chứa tổng quát
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
			Parent root = loader.load();
			
			//Tạo Scene từ root(giao diện)
			Scene scene = new Scene(root);
			
			//Cài đặt tiêu đề cửa sổ
			primaryStage.setTitle("Đăng nhập CLB Tin học");
			
			//Hiển thị Scene lên cửa sổ
			primaryStage.setScene(scene);
			
			//Hiển thị cửa sổ
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		public static void main(String[] args) {
			Application.launch(args);//Khởi động javafx 
	}
}
