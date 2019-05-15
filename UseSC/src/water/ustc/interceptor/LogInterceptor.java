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
		System.out.println("执行了predo！");
	 	this.actionName=actionName;
	 	this.startTime=startTime;
	 	System.out.println("action name:"+this.actionName);
	 	System.out.println("startTime"+this.startTime);
	}
	
	public void afterAction(String endTime,String result) {
		System.out.println("执行了afterdo！");
		this.endTime=endTime;
		this.result=result;
		System.out.println("endTime:"+this.endTime);
		System.out.println("result:"+this.result);
		
		//写入xml文件
	 	String xmlPath="E:/test/log.xml";
	 	checkLogFile(xmlPath);
	 	addNewLog(xmlPath);
	}
	
	/**
	 * 检查log.xml文件是否存在
	 * 
	 */
	public void checkLogFile(String path) {
		File file = new File(path);
		
		if(file.exists()) {
			return;
		}
		
		Document document = DocumentHelper.createDocument();//创建文件
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
	 * 将信息追加至日志文件log.xml
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
