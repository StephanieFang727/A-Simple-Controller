package sc.ustc.controller;

public class User {

	private int userId;//�û�id 
	private String userName;//�û��˺�
	private String userPass;//�û�����
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return userPass;
	}
	public void setPassWord(String passWord) {
		this.userPass = passWord;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
