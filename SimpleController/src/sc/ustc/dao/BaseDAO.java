package sc.ustc.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	protected String driverClassName;//���ݿ���������
	protected String url;//���ݿ����·��
	protected String dbUserName;//���ݿ��û���
	protected String dbUserPassword;//���ݿ��û�����
	protected int dbSelect;//ѡ�����ݿ����
	
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

	protected Connection openDBConnection() throws Exception {//��������ݿ�����
		Class.forName(driverClassName);
		Connection con=null;
		switch (dbSelect) {
		case 1://����MySql���ݿ�
			con=DriverManager.getConnection(url, dbUserName, dbUserPassword);
			System.out.println("sqlite���ݿ����ӳɹ���");
			break;

		case 2://����sqlite���ݿ�
			con=DriverManager.getConnection(url);
			System.out.println("sqlite���ݿ����ӳɹ���");
			break;
		}
		
		return con;
	}
	
	protected boolean closeDBConnection(Connection con) throws Exception {//����ر����ݿ�����
		if(con!=null) {
			con.close();
			System.out.println("���ݿ����ӹرգ�");
			return true;
		}
		return false;
	}
	
	protected abstract Object query(BaseBean T) ;//����ִ��sql��䣬�����ؽ������
	
	protected abstract boolean insert(BaseBean T);//����ִ��sql��䣬������ִ�н��
	
	protected abstract boolean update(BaseBean T);//����ִ��sql��䣬������ִ�н��
	
	protected abstract boolean delete(BaseBean T);//����ִ��sql��䣬������ִ�н��
	
	
}
