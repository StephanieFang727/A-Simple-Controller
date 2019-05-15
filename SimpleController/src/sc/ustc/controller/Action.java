package sc.ustc.controller;

import java.lang.reflect.Method;

import org.dom4j.Element;

/**
 * ����ҵ����
 * ִ����Ӧ���������ؽ��
 * @author Stephanie
 */
public class Action {
	private String name;
	private String classPath;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
	public String handleAction(Element child,String userName,String userPass) {
		String className = child.attributeValue("class");//���������ļ��е�class name
		System.out.println("class:"+className);
		String methodName = child.attributeValue("method");//���������ļ��е�method name
		System.out.println("method:"+methodName);
	
		//ʵ������Ӧ��
		Class clazz;
		String result=null;
		try {
		clazz = Class.forName(className);
		Class[] paratype = {String.class,String.class};
		Object instance = clazz.newInstance();//ʵ��������
		Method interMethod=instance.getClass().getDeclaredMethod(methodName, paratype);
		Object[] para = {userName,userPass};//Ҫ���ݵĲ���
		result=(String) interMethod.invoke(instance, para);
		System.out.println("���ؽ����"+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}