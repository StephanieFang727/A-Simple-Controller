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
 * 实现代理类
 * @author Stephanie
 *
 */
public class InterceptorCglib implements MethodInterceptor {
	private Object target;
	
	public Object getInstance(Object target) {  
        this.target = target;  //给业务对象赋值
        Enhancer enhancer = new Enhancer(); //创建加强器，用来创建动态代理类
        enhancer.setSuperclass(this.target.getClass());  //为加强器指定要代理的业务类（即：为下面生成的代理类指定父类）
        //设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦
        enhancer.setCallback(this); 
       // 创建动态代理类对象并返回  
       return enhancer.create(); 
    }
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

		SAXReader reader = new SAXReader();
		Document controllerXml= reader.read(this.getClass().getResource("/controller.xml").getFile());
		Element interceptor = controllerXml.getRootElement().element("interceptor");//<interceptor>
		
		String className = interceptor.attributeValue("class");//解析拦截器的class属性
		System.out.println("class:"+className);  
		String premethod = interceptor.attributeValue("predo");//解析拦截器的predo方法
		System.out.println("predo:"+premethod);
		String aftmethod = interceptor.attributeValue("afterdo");//解析拦截器的afterdo方法
		System.out.println("afterdo:"+aftmethod);
		
		Class clazz = Class.forName(className);
		Class[] paratype = {String.class,String.class};
		Object instance = clazz.newInstance();//实例化对象
		Method interMethod=null;
		//执行predo方法
		interMethod= instance.getClass().getDeclaredMethod(premethod, paratype);
		//获取开始时间
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String startTime = df.format(new Date(System.currentTimeMillis()));
		Object[] para = {((Element) args[0]).attributeValue("name"),startTime};//要传递的参数
		interMethod.invoke(instance, para);
		
		//调用业务类（父类）方法
		String result=(String)proxy.invokeSuper(obj, args);//实例化类，执行相应方法并返回结果
				
        //执行afterAction()
		//获取结束时间
		String endTime = df.format(new Date(System.currentTimeMillis()));
		interMethod= instance.getClass().getDeclaredMethod(aftmethod, paratype);
		Object[] para2 = {endTime,result};//要传递的参数
		interMethod.invoke(instance, para2);
		
		return result;
		
	}
	  
}
