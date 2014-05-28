package data;

public class User {
	public static final int SOC = 1, ANIM = 2, ADMIN = 0;
	
	private String login;
	private int id;
	private int type;
	private String password;

	public User(String login, int id, int type, String password) {
		this.login = login;
		this.id = id;
		this.type = type;
		this.password = password;

	}

	public String getLogin() {
		return this.login;
	}

	public int getId() {
		return this.id;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getPassword(){
		return this.password;
	}
}
