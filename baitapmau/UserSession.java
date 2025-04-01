package baitapmau;

public final class UserSession {
	private static UserSession instance;//instance đại diện cho 1 đối tượng usersession
	
	private String email;
	private String fullname;
	
	private UserSession(String email, String fullname) {
		this.email =email;
		this.fullname= fullname;
	}
	
	public static void createInstance(String email, String fullname) {
		if(instance == null) {
			instance = new UserSession(email,fullname);
		}
	}
	
	public static UserSession getInstance() {
		if(instance == null) {
			throw new IllegalStateException("UserSession chưa được tạo ra, cần gọi phương thức createInsatance trước");
		}
		return instance;
	}

	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}

	public  void cleanUserSession() {
		email ="";
		fullname = "";
		instance = null;
	}
}
