package sc.ustc.dao;

public class Driver {

	private String databaseName;//���ݿ���
	private String url;//���ݿ�url
	private String data_name;//���ݿ��û���
	private String data_pass;//���ݿ�����
	private int database_select;//1����mysql,2����sqlite
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getData_name() {
		return data_name;
	}
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	public String getData_pass() {
		return data_pass;
	}
	public void setData_pass(String data_pass) {
		this.data_pass = data_pass;
	}
	public int getDatabase_select() {
		return database_select;
	}
	public void setDatabase_select(int database_select) {
		this.database_select = database_select;
	}
}
