package sc.ustc.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * ʵ�ִ�����
 * @author Stephanie
 *
 */
public class InterceptorCglib implements MethodInterceptor {
	private Object target;
	
	public Object getInstance(Object target) {  
        this.target = target;  //��ҵ�����ֵ
        Enhancer enhancer = new Enhancer(); //������ǿ��������������̬������
        enhancer.setSuperclass(this.target.getClass());  //Ϊ��ǿ��ָ��Ҫ�����ҵ���ࣨ����Ϊ�������ɵĴ�����ָ�����ࣩ
        //���ûص������ڴ����������з����ĵ��ã��������CallBack����Callback����Ҫʵ��intercept()����������
        enhancer.setCallback(this); 
       // ������̬��������󲢷���  
       return enhancer.create(); 
    }
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

		SAXReader reader = new SAXReader();
		Document controllerXml= reader.read(this.getClass().getResource("/controller.xml").getFile());
		Element interceptor = controllerXml.getRootElement().element("interceptor");//<interceptor>
		
		String className = interceptor.attributeValue("class");//������������class����
		System.out.println("class:"+className);  
		String premethod = interceptor.attributeValue("predo");//������������predo����
		System.out.println("predo:"+premethod);
		String aftmethod = interceptor.attributeValue("afterdo");//������������afterdo����
		System.out.println("afterdo:"+aftmethod);
		
		Class clazz = Class.forName(className);
		Class[] paratype = {String.class,String.class};
		Object instance = clazz.newInstance();//ʵ��������
		Method interMethod=null;
		//ִ��predo����
		interMethod= instance.getClass().getDeclaredMethod(premethod, paratype);
		//��ȡ��ʼʱ��
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String startTime = df.format(new Date(System.currentTimeMillis()));
		Object[] para = {((Element) args[0]).attributeValue("name"),startTime};//Ҫ���ݵĲ���
		interMethod.invoke(instance, para);
		
		//����ҵ���ࣨ���ࣩ����
		String result=(String)proxy.invokeSuper(obj, args);//ʵ�����ִ࣬����Ӧ���������ؽ��
				
        //ִ��afterAction()
		//��ȡ����ʱ��
		String endTime = df.format(new Date(System.currentTimeMillis()));
		interMethod= instance.getClass().getDeclaredMethod(aftmethod, paratype);
		Object[] para2 = {endTime,result};//Ҫ���ݵĲ���
		interMethod.invoke(instance, para2);
		
		return result;
		
	}
	  
}
