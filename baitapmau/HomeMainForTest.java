package baitapmau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeMainForTest extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Load FXML file ra đối tượng parent đóng vai trò vùng chứa tổng quát
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
			
			//Hiển thị Scene lên cửa sổ
			primaryStage.setScene( new Scene(loader.load()));
			primaryStage.setTitle("JAVAFX");
			
			//Hiển thị cửa sổ
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		public static void main(String[] args) {
			launch(args);//Khởi động javafx 
	}
}
