package sc.ustc.controller;

import java.lang.reflect.Method;

import org.dom4j.Element;

/**
 * 定义业务类
 * 执行相应方法并返回结果
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
		String className = child.attributeValue("class");//解析配置文件中的class name
		System.out.println("class:"+className);
		String methodName = child.attributeValue("method");//解析配置文件中的method name
		System.out.println("method:"+methodName);
	
		//实例化对应类
		Class clazz;
		String result=null;
		try {
		clazz = Class.forName(className);
		Class[] paratype = {String.class,String.class};
		Object instance = clazz.newInstance();//实例化对象
		Method interMethod=instance.getClass().getDeclaredMethod(methodName, paratype);
		Object[] para = {userName,userPass};//要传递的参数
		result=(String) interMethod.invoke(instance, para);
		System.out.println("返回结果："+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}