package sc.ustc.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	protected String driverClassName;//数据库驱动类名
	protected String url;//数据库访问路径
	protected String dbUserName;//数据库用户名
	protected String dbUserPassword;//数据库用户密码
	protected int dbSelect;//选择数据库参数
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserPassword() {
		return dbUserPassword;
	}

	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}

	public int getDbSelect() {
		return dbSelect;
	}

	public void setDbSelect(int dbSelect) {
		this.dbSelect = dbSelect;
	}

	protected Connection openDBConnection() throws Exception {//负责打开数据库连接
		Class.forName(driverClassName);
		Connection con=null;
		switch (dbSelect) {
		case 1://连接MySql数据库
			con=DriverManager.getConnection(url, dbUserName, dbUserPassword);
			System.out.println("sqlite数据库连接成功！");
			break;

		case 2://连接sqlite数据库
			con=DriverManager.getConnection(url);
			System.out.println("sqlite数据库连接成功！");
			break;
		}
		
		return con;
	}
	
	protected boolean closeDBConnection(Connection con) throws Exception {//负责关闭数据库连接
		if(con!=null) {
			con.close();
			System.out.println("数据库连接关闭！");
			return true;
		}
		return false;
	}
	
	protected abstract Object query(BaseBean T) ;//负责执行sql语句，并返回结果对象
	
	protected abstract boolean insert(BaseBean T);//负责执行sql语句，并返回执行结果
	
	protected abstract boolean update(BaseBean T);//负责执行sql语句，并返回执行结果
	
	protected abstract boolean delete(BaseBean T);//负责执行sql语句，并返回执行结果
	
	
}
