package water.ustc.action;

import sc.ustc.controller.User;

/**
 * ��½���ܵ�У��
 * ʹ��pojo��
 * @author admin
 *
 */
public class LoginAction {
	
	private UserBean ub;

	public String handleLogin(User user)
	{
		System.out.println("login:"+user.getUserName());
		System.out.println("login:"+user.getPassWord());
		this.ub.setUserName(user.getUserName());
		this.ub.setUserPass(user.getPassWord());
		boolean islogined =false;
		System.out.println("username:"+user.getUserName()+",userpass:"+user.getPassWord());
		islogined=this.ub.signIn();
		if(islogined)
			return "success";
		else
			return "failure";
	}
	
	public void Print()
	{
		if(this.ub!=null)
			System.out.println("��ֵ");
		else
			System.out.println("����ʧ��!");
	}

	public UserBean getUb() {
		System.out.println("��Userbean");
		return ub;
	}

	public void setUb(UserBean ub) {
		this.ub = ub;
	}	
}
