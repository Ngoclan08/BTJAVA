package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Tạo thành phần UI nút bấm và trường text
			Button btn = new Button("Xem ngày giờ");
			TextField tf = new TextField();

			// Xử lý sự kiện bấm nút
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					// Thêm ngày giờ hiện tại vào ô text
					tf.setText(new java.util.Date().toString());

				}

			});
			// Thêm nút bấm, trường text vào layout Hbox
			HBox root = new HBox();
			root.getChildren().addAll(btn, tf);

			// Thêm layout vào khung chứa Scene
			Scene scene = new Scene(root, 300, 100);

			// Thêm Scene vào khung chứa Stage
			primaryStage.setScene(scene);
			// Đặt tiêu đề cho khung chứa Stage và hiển thị
			primaryStage.setTitle("Ví dụ JavaFX");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
