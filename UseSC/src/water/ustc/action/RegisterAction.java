package water.ustc.action;

public class RegisterAction {
	/**
	 * 接收页面请求过来的参数username,password
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
	 * 输入信息简单校验
	 */
	public String handleRegister(String userName,String userPass){
		//if(null!=username && null!=password){
			//登录成功
			return "success";
		//}else{
			//登录失败
			//return "failure";
		//}
	}
}
