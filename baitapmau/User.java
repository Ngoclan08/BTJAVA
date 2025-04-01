package baitapmau;

public class User {
	private int userId;
	private String email;
	private String fullname;
	private boolean gender;
	private String course;
	private String password;

	//Constructor tạo đối tượng khi user thêm mới 
	public User(String email, String fullname, boolean gender, String course, String password) {
		this.email = email;
		this.fullname = fullname;
		this.gender = gender;
		this.course = course;
		this.password = password;
	}
	
	//Constructor khi user lấy từ csdll lên tâng model(trong userdao)
	public User(int userId, String email, String fullname, boolean gender, String course, String password) {
		this.userId =userId;
		this.email = email;
		this.fullname = fullname;
		this.gender = gender;
		this.course = course;
		this.password = password;
	}

	//Getter and setter
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
