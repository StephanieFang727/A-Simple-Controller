package water.ustc.interceptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
    
public class LogInterceptor {
	private String actionName;
	private String startTime;
	private String endTime;
	private String result;
	
	public void preAction(String actionName,String startTime) {
		System.out.println("ִ����predo��");
	 	this.actionName=actionName;
	 	this.startTime=startTime;
	 	System.out.println("action name:"+this.actionName);
	 	System.out.println("startTime"+this.startTime);
	}
	
	public void afterAction(String endTime,String result) {
		System.out.println("ִ����afterdo��");
		this.endTime=endTime;
		this.result=result;
		System.out.println("endTime:"+this.endTime);
		System.out.println("result:"+this.result);
		
		//д��xml�ļ�
	 	String xmlPath="E:/test/log.xml";
	 	checkLogFile(xmlPath);
	 	addNewLog(xmlPath);
	}
	
	/**
	 * ���log.xml�ļ��Ƿ����
	 * 
	 */
	public void checkLogFile(String path) {
		File file = new File(path);
		
		if(file.exists()) {
			return;
		}
		
		Document document = DocumentHelper.createDocument();//�����ļ�
		Element root=document.addElement("log");
		document.setRootElement(root);
		
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		
		XMLWriter writer;
		try {
			writer=new XMLWriter(new FileOutputStream(new File(path)),format);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * ����Ϣ׷������־�ļ�log.xml
	 * 
	 */
	public void addNewLog(String path) {
		 SAXReader reader = new SAXReader();
		 XMLWriter writer;
         try {
			Document document = reader.read(new File(path));
			Element root=document.getRootElement();;
			
			Element action=root.addElement("action");
			Element name=action.addElement("name");
			name.setText(this.actionName);
			Element sTime=action.addElement("s-time");
			sTime.setText(this.startTime);
			Element eTime=action.addElement("e-time");
			eTime.setText(this.endTime);
			Element result=action.addElement("result");
			result.setText(this.result);
			
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			writer=new XMLWriter(new FileOutputStream(new File(path)),format);
			writer.write(document);
			writer.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
