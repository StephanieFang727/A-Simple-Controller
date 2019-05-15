package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import net.sf.cglib.proxy.Enhancer;
import sc.ustc.dao.layload.Proxybean;
import sc.ustc.dao.layload.SqlQueryLazyLoader;

public class Conversation {

	private String xml_path="/or_mapping.xml";//or_mapping.xml��·��
	private Configuration configuration;
	private Connection conn= null;
	private Driver driver;
	private String table_name = null;//����
	private String table_pk = null;//��ֵ
	private String table_username = null;//�û���
	private String table_password = null;//����
	private String objectname = null;//����
	private List<Property> propertys;//���Զ���
	
	//���캯��
	public Conversation() {
		// TODO Auto-generated constructor stub
		Configuration configuration = new Configuration();
		configuration.setPath(this.xml_path);
		setConfiguration(configuration);
	}
	
	//��ȡConfiguration����ʼ�����ݿ����Ӻͱ�����
	public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        try {
        	driver= this.configuration.getDBMS();
			initTable();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	//�õ��������
	private void initTable(){
		
        if (this.table_name == null){
            this.table_name = configuration.getTableName();
        }
        if (this.table_pk == null){
            this.table_pk = configuration.getTablePK();
        }
        if (this.table_username == null){
            this.table_username = configuration.getTableColumn("username");
        }
        if (this.table_password == null){
            this.table_password = configuration.getTableColumn("password");
        }
        if(this.objectname==null) {
        	this.objectname=configuration.getEntityName();
        }
        try {
			this.propertys=configuration.getProperty();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	/**
	 * �����ݿ�
	 * @return connn
	 */
	public void OpenDB()
	{
		Statement stat = null;//״̬
		// ע������
	    try {
			Class.forName(this.driver.getDatabaseName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("��������ʧ�ܣ�");
		}
	    // ��������
	    try {
	    	if(this.driver.getDatabase_select()==1)
	    		this.conn = (Connection) DriverManager.getConnection(this.driver.getUrl(),this.driver.getData_name(),this.driver.getData_pass());
	    	if(this.driver.getDatabase_select()==2)
	    		this.conn = (Connection) DriverManager.getConnection(this.driver.getUrl());
	    	System.out.println("���ӳɹ���");
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("��ȡ���ݿ�����ʧ�ܣ�");
		}
	}
	
	/**
	 * �ر����ݿ�
	 * @return
	 */
	public boolean CloseDB()
	{
		try {
			this.conn.close();//�ر�����
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("���ݿ����ӶϿ�ʧ�ܣ�");
			return false;
		}	
	}
	
	/**
	 * ��ѯ
	 * ���ض�Ӧ���Խ���б�
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public <T extends BaseBean> List<String> getObject(T user) throws SQLException
	{
		List<String> result = new ArrayList<>();
		String userid = null,username = null,passWord=null;
		BaseBean obj = new BaseBean();
		//�����Էֳ����࣬һ�಻�����ء�һ��������
		List<Property> lazyload = new ArrayList<>();
		List<Property> notlazyload = new ArrayList<>();
		for(Property p : this.propertys)
		{
			if(p.isLazy())
				lazyload.add(p);
			else
				notlazyload.add(p);
		}
		//�õ�idֵ
		String value = user.getValue();
		//������
		OpenDB();
		//����userid��ѯ���������Ϣ
		if(!notlazyload.isEmpty())
		{
			//���ڷ������ز��֣�ֱ�ӹ����ѯ���
			String info=null;
			for(Property p:notlazyload)
			{
				info = info+","+p.getName();
			}
			info =info.substring(5, info.length());
			//�����ѯ���
			String sql="select "+info+" from "+this.table_name+" where UserId = '"+value+"';";
			//���в�ѯ
			ResultSet rs =  (ResultSet) query(sql);
			// �����ѯ���
		    while(rs.next()) 
		    {
		    	String str;
		    	for(Property p:notlazyload)
		    	{
		    		str = rs.getString(p.getName());
		    		result.add(str);
		    	}
		    }
		    System.out.println("��������:"+result);
		}
		
	    if(!lazyload.isEmpty())
	    {
	    	//���������أ������������ش������
	    	Proxybean p = createLazyloadquery(lazyload);
	    	String info=null;
			for(String s:p.getLazyinfo())//��ȡ����������
			{
				info = info+","+s;
			}
			info =info.substring(5, info.length());
			//�����ѯ���
			String sql="select "+info+" from "+this.table_name+" where UserId = '"+value+"';";
			//���в�ѯ
			ResultSet rs =  (ResultSet) query(sql);
			// �����ѯ���
		    while(rs.next()) 
		    {
		    	String str;
		    	for(String s:p.getLazyinfo())
		    	{
		    		str = rs.getString(s);
		    		result.add(str);
		    	}
		    }
		    System.out.println("������:"+result);
	    }
	    CloseDB();
	    return result;
	}
	

	/**
	 * ɾ��
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public <T extends BaseBean> boolean deleteObject(T user) throws SQLException
	{
		return false;
		
	}
	
	
	/**
	 * ����
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public <T extends BaseBean> boolean insertObject(T user) throws SQLException
	{
		return false;
		
	}
	
	/**
	 * �޸�
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public <T extends BaseBean> boolean updateObject(T user) throws SQLException
	{
		return false;
		
	}
	
	
	
	public Object query(String sql) {
		// TODO Auto-generated method stub
		//Statement stat = null;
		try {
			Statement stat = (Statement) this.conn.createStatement();
			return stat.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ���������ز�ѯ
	 * @param lazy_p
	 * @return
	 */
	public Proxybean createLazyloadquery(List<Property> lazy_p)
	{
		 Enhancer enhancer=new Enhancer();  
	     enhancer.setSuperclass(Proxybean.class);  
	     SqlQueryLazyLoader sl = new SqlQueryLazyLoader();
	     sl.setLazyinfo(lazy_p);
	     return (Proxybean)enhancer.create(Proxybean.class,sl);  
	}

}
