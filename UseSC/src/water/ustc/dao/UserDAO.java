package water.ustc.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import sc.ustc.dao.BaseBean;
import sc.ustc.dao.BaseDAO;
import sc.ustc.dao.Conversation;
import water.ustc.action.UserBean;

public class UserDAO extends BaseDAO{
	
	private Conversation conversation;
	
	
	public UserDAO() {
		// TODO Auto-generated constructor stub
		this.conversation = new Conversation();
	}
	
	//打开数据库
	public void openDB()
	{
		conversation.OpenDB();
	}

	//关闭数据库
    public void closeDBConnection() {
        conversation.CloseDB();
    }
    
    //插
  	@Override
  	public boolean insert(BaseBean user) {
  		// TODO Auto-generated method stub
  		try {
  			return conversation.insertObject(user);
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return false;
  	}
  	
  	//查
  	@Override
  	public Object query(BaseBean user) {
  		// TODO Auto-generated method stub
  		try {//将result的数据提取出来，封装成对象
  			List<String> result=conversation.getObject(user);
  			UserBean u = new UserBean();
  			u.setUserName(result.get(0));
  			u.setUserPass(result.get(1));
  			System.out.println("UserDAO:"+u.getUserName()+","+u.getUserPass());
  			return u;
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return null;
  	}
  	
  	//改
  	@Override
  	public boolean update(BaseBean user) {
  		// TODO Auto-generated method stub
  		try {
  			return conversation.updateObject(user);
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return false;
  	}

  //删
  	@Override
  	public boolean delete(BaseBean user) {
  		// TODO Auto-generated method stub
  		try {
  			return conversation.deleteObject(user);
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return false;
  	}
	
	

	
}



// public UserDAO() {//初始化数据库参数
//		
//		this.setDbSelect(1);
//		driverClassName="com.mysql.cj.jdbc.Driver";//数据库驱动类
//		url="jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";//数据库访问路径
//		dbUserName="root";//数据库用户名
//		dbUserPassword="123456";//数据库用户密码	
//		
//		
//		this.setDbSelect(2);
//		driverClassName="org.sqlite.JDBC";//数据库驱动类
//		url="jdbc:sqlite:E:/test/test01.db";//数据库访问路径	
//		
//	}
//	
//	@Override
//	public boolean delete(String arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean insert(String arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	/**
//	 * 查询用户是否存在
//	 */
//	@Override
//	public Object query(String sql) {
//		Connection con=null;
//		Statement stmt = null;
//		ResultSet rs=null;
//		try {
//			con=openDBConnection();//连接数据库
//			stmt=con.createStatement();
//			rs=stmt.executeQuery(sql);//返回查询结果
//			if(rs.next()) {//用户存在
//				String userPass=rs.getString("password");//取对应用户密码
//				//System.out.println(userPass);
//				UserBean user=new UserBean();//构造新对象
//				user.setUserPass(userPass);//把密码给新对象
//				return user;
//			}else {//用户不存在
//				System.out.println("不存在该用户！");
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {//关闭连接
//			try {
//				if(rs != null) rs.close();
//				if(stmt != null) stmt.close();
//				closeDBConnection(con);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//	
//	@Override
//	public boolean update(String arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
