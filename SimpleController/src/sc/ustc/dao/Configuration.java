package sc.ustc.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Configuration {

	private String path;
	private Map<String,Map<String,String>> table = null;

	public void setPath(String path) {
		this.path = path;
		System.out.println("or_mapping:"+this.path);
	}
	
	//��xml�н��������ݿ���Ϣ
	public Driver getDBMS() throws DocumentException
	{
		File XMLfile = new File(this.getClass().getResource(this.path).getFile());//��ȡxml
		Map dbmsInfo = new HashMap<>();//������ݿ�������صļ�ֵ��
		SAXReader reader = new SAXReader();
		Document document = reader.read(XMLfile);
		Element root = document.getRootElement();
		Element jdbc = root.element("jdbc");//�õ�jdbcԪ��
		List<Element> property_list = jdbc.elements("property");
		for(Element property : property_list)
		{
			//Element name_element = property.element("name").getText();
			//String name = name_element.attributeValue("name");
			String name =property.element("name").getText();
			String value =property.element("value").getText();
			dbmsInfo.put(name,value);		
		}
		System.out.println(dbmsInfo);
		//�������ݿ�������Ϣ
		Driver driver = new Driver();
		driver.setDatabaseName((String) dbmsInfo.get("driver_class"));
		driver.setUrl((String)(dbmsInfo.get("url_path")+"?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false"));
		driver.setData_name((String)dbmsInfo.get("db_username"));
		driver.setData_pass((String)dbmsInfo.get("db_userpassword"));
		driver.setDatabase_select(Integer.parseInt((String) dbmsInfo.get("db_select")));
					
		return driver;
	}
	//��xml�н���������Ϣ
	public Map<String,Map<String,String>> getTable(){
        if (this.table == null){
        	// ��ʼ��table ��ȡor_mapping
            table = new HashMap<>();
            SAXReader reader = new SAXReader();
            try {
                Document document = reader.read(new File(this.getClass().getResource(this.path).getFile()));
                Element root = document.getRootElement();// OR-Mappering
                Element class_element = (Element) root.selectSingleNode("class");
                List<Element> child_ele_list =class_element.elements();
                for(Element child_ele : child_ele_list   ){ // name table id property
                    String child_eleText = child_ele.getName();
                    Map<String,String> column_info = new HashMap<>();
                    switch (child_eleText){
                        case "property": // ��ȡname������Ϊtable��key
                            String property_key="";
                            for(Element property_element : (List<Element>)child_ele.elements()){ //name column type lazy
                                // ��ȡproperty�ڵ�����������ֵ,����name�ڵ�ֵ��Ϊtable��key
                                String property_name = property_element.getName();
                                if ("name".equals(property_name)){
                                    property_key = property_element.getText();
                                }
                                column_info.put(property_name,property_element.getText());
                            }
                            table.put(property_key,column_info);
                            break;
                        case "id":// ʹ��tb_primarykey��Ϊtable��key
                            for(Element property_id : (List<Element>)child_ele.elements()){
                                column_info.put(property_id.getName(),property_id.getText());
                            }
                            table.put("id",column_info);
                            break;
                        default: // ʹ�ýڵ�����Ϊtable��key
                            column_info.put(child_eleText,child_ele.getText());
                            table.put(child_ele.getName(),column_info);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }
	
    /**
     * ��ȡ���������
     * @return
     */
    public  String getTablePK(){
        return getTable().get("id").get("name");
    }

    /**
     * ����ʵ������������ȡ��Ӧ�ı��ֶ���
     * @param entity_attr
     * @return
     */
    public  String getTableColumn(String entity_attr){
        return getTable().get(entity_attr).get("column");
    }

    /**
     * ��ȡ���ݱ������
     * @return
     */
    public String getTableName() {
        return getTable().get("table").get("table");
    }

    /**
     * ��ȡ��Ӧʵ���������
     * @return
     */
    public String getEntityName(){
        return getTable().get("name").get("name");
    }

    /**
     * �ж������Ƿ�������
     * @param entity_attr
     * @return
     */
    public Boolean isLazyLoad(String entity_attr){

        Map<String, String> map = getTable().get(entity_attr);
        if (map != null ) {
            switch (map.get("lazy")){
                case "true":
                    return true;
                case "false":
                    return false;
                    default:
                        return false;
            }
        }
        return false;
    }
    //��ȡԪ����Ϣ
    public List<Property> getProperty() throws DocumentException
    {
    	File XMLfile = new File(this.getClass().getResource(this.path).getFile());//��ȡxml
		List<Property> propertys = new ArrayList<>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(XMLfile);
		Element root = document.getRootElement();
		Element jdbc = root.element("class");//�õ�classԪ��
		List<Element> property_list = jdbc.elements("property");
		for(Element property : property_list)
		{
			String name =property.element("name").getText();
			String column =property.element("column").getText();
			String type =property.element("type").getText();
			String lazy =property.element("lazy").getText();
			Property p = new Property();
			p.setColumn(column);
			p.setLazy(Boolean.parseBoolean(lazy));
			p.setName(name);
			p.setType(type);
			propertys.add(p);
					
		}
		return propertys;
    	
    }
	
	
}
