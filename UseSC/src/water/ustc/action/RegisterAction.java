package water.ustc.action;

public class RegisterAction {
	/**
	 * ����ҳ����������Ĳ���username,password
	 */
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
	/**
	 * ������Ϣ��У��
	 */
	public String handleRegister(String userName,String userPass){
		//if(null!=username && null!=password){
			//��¼�ɹ�
			return "success";
		//}else{
			//��¼ʧ��
			//return "failure";
		//}
	}
}
