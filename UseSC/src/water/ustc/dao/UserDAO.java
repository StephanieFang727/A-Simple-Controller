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
	
	//�����ݿ�
	public void openDB()
	{
		conversation.OpenDB();
	}

	//�ر����ݿ�
    public void closeDBConnection() {
        conversation.CloseDB();
    }
    
    //��
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
  	
  	//��
  	@Override
  	public Object query(BaseBean user) {
  		// TODO Auto-generated method stub
  		try {//��result��������ȡ��������װ�ɶ���
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
  	
  	//��
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

  //ɾ
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



// public UserDAO() {//��ʼ�����ݿ����
//		
//		this.setDbSelect(1);
//		driverClassName="com.mysql.cj.jdbc.Driver";//���ݿ�������
//		url="jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";//���ݿ����·��
//		dbUserName="root";//���ݿ��û���
//		dbUserPassword="123456";//���ݿ��û�����	
//		
//		
//		this.setDbSelect(2);
//		driverClassName="org.sqlite.JDBC";//���ݿ�������
//		url="jdbc:sqlite:E:/test/test01.db";//���ݿ����·��	
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
//	 * ��ѯ�û��Ƿ����
//	 */
//	@Override
//	public Object query(String sql) {
//		Connection con=null;
//		Statement stmt = null;
//		ResultSet rs=null;
//		try {
//			con=openDBConnection();//�������ݿ�
//			stmt=con.createStatement();
//			rs=stmt.executeQuery(sql);//���ز�ѯ���
//			if(rs.next()) {//�û�����
//				String userPass=rs.getString("password");//ȡ��Ӧ�û�����
//				//System.out.println(userPass);
//				UserBean user=new UserBean();//�����¶���
//				user.setUserPass(userPass);//��������¶���
//				return user;
//			}else {//�û�������
//				System.out.println("�����ڸ��û���");
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {//�ر�����
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
