package baitapmau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	// Đối tượng connection khai báo như thuộc tính để sử dụng chung
	private Connection connection;

	// Kết nối đến cơ sở dữ liệu Access
	public UserDao() {
		try {
			// cập nhật đường dẫn đến file .accdb
			String URL = "jdbc:ucanaccess://lib/QLNS.accdb";// đường dẫn tới csdl
			connection = DriverManager.getConnection(URL);// tạo kết nối khi đối tượng userdao được tạo ra
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// lấy danh sách tất cả người dùng
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<>();
		String query = "SELECT * FROM tblUser";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				int userId = rs.getInt("UserID");
				String email = rs.getString("Email");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				String course = rs.getString("Course");
				String password = rs.getString("Password");

				// Đóng gói đối tượng vào user
				User user = new User(userId, email, fullname, gender, course, password);
				userList.add(user);// Thêm vào ds kết quả trả về
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userList;
	}

	// Lấy 1 người dùng theo userid
	public User getUserById(int userId) {
		User user = null;
		String query = "SELEECT *FROM tblUser WHERE UserID =?";
		// Dùng preparedStatement để truyền sữ liệu vào dấu ?
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String email = rs.getString("Email");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				String course = rs.getString("Course");
				String password = rs.getString("Password");

				user = new User(userId, email, fullname, gender, course, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	// Lấy 1 ngươi dùng theo email
	public User getUserByEmail(String email) {
		User user = null;
		String query = "SELECT *FROM tblUser WHERE Email =?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int userId = rs.getInt("UserID");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				String course = rs.getString("Course");
				String password = rs.getString("Password");

				user = new User(userId, email, fullname, gender, course, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// Thêm 1 người dùng mới vào csdl
	public boolean addUser(User user) {
		String query = "INSERT INTO tblUser (Email, Fullname, Gender, Course, Password) VALUES(?,?,?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getFullname());
			stmt.setBoolean(3, user.isGender());
			stmt.setString(4, user.getCourse());
			stmt.setString(5, user.getPassword());

			int rowInserted = stmt.executeUpdate();
			return rowInserted > 0;// Nếu kq>0 thì thêm mới thành công, trả về true
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật thông tin người dùng
	public boolean updateUser(User user) {
		String query = "UPDATE tblUser SET Email =?,Fullname=?,Gender=?,Course=?,Pasword=? WHERE UserID=?";
		try (PreparedStatement stmt=connection.prepareStatement(query)){
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getFullname());
			stmt.setBoolean(3, user.isGender());
			stmt.setString(4, user.getCourse());
			stmt.setString(5, user.getPassword());
			stmt.setInt(6, user.getUserId());
			
			int rowsUpdate = stmt.executeUpdate();
			return rowsUpdate >0;			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Xóa người dùng theo UserId
	public boolean deleteUser(int userId) {
		String query = "DELETE FROM tblUser WHERE UserId =?";
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setInt(1, userId);
			int rowDeleted = stmt.executeUpdate();
			return rowDeleted >0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
