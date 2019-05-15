package water.ustc.action;

import sc.ustc.dao.BaseBean;
import water.ustc.dao.UserDAO;

public class UserBean extends BaseBean{
	
	private Integer userId;
    private String userName;
    private String userPass;

    public UserBean(){}

    public UserBean(String value) {
        super(value);
    }

    public UserBean(String value, String column) {
        super(value, column);
    }

    public boolean signIn() {
    	
        UserDAO userDao = new UserDAO();
        userDao.openDB();
        UserBean user = (UserBean) userDao.query(new UserBean("1"));
        userDao.closeDBConnection();
        return this.userPass.equals(user.getUserPass());
        //return true;
        
    	
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}



//private int userId;
//private String userName;
//private String userPass;
//
//public int getUserId() {
//	return userId;
//}
//
//public void setUserId(int userId) {
//	this.userId = userId;
//}
//
//public String getUserName() {
//	return userName;
//}
//
//public void setUserName(String userName) {
//	this.userName = userName;
//}
//
//public String getUserPass() {
//	return userPass;
//}
//
//public void setUserPass(String userPass) {
//	this.userPass = userPass;
//}
//
//public boolean signIn() {
//	String sql="select * from user where username="+"'"+this.userName+"';";
//	System.out.println(sql);
//	UserBean user= (UserBean) new UserDAO().query(sql);
//	if(user!=null) {
//		if(user.getUserPass().equals(userPass)) {
//			return true;
//		}
//	}
//	return false;	
//}
