package sc.ustc.controller;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SimpleController extends HttpServlet {
	private File bean_xml=new File(this.getClass().getResource("/di.xml").getFile());//获取xml;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
		resp.setContentType("text/html;charset=UTF-8");
		String url = req.getRequestURL().toString();//获取请求URL
		System.out.println(url);
		String userName=req.getParameter("username");//获取表单提交的用户名
		String userPass=req.getParameter("password");//获取表单提交的密码
		//将URL中的信息构造成User
        User user = CreateUser(req);
		System.out.println("提交的用户名："+userName);
		System.out.println("提交的密码："+userPass);
		
		
		String action=url.substring(url .lastIndexOf("/")+1,url.length()-3);//截取请求action名称
		System.out.println(action);
		
		//解析配置文件controller.xml
		SAXReader reader = new SAXReader();
		Document document;
			try {
				document = reader.read(new File(this.getClass().getResource("/controller.xml").getFile()));
				Element root = document.getRootElement().element("controller");//<controller>
			    List<Element> childElements = root.elements();// action		  
			    boolean matchActionName=false;//判断请求名称与配置文件中的action name是否匹配
				for (Element child : childElements) {//<action>
					
					//判断请求名是否匹配
					matchActionName=checkActionName(child,action);
					if(matchActionName) {
					
						//查看di.xml中有无与Action同名的bean节点
						boolean isBeanMatched= Findbean(action);
						if(isBeanMatched)//若匹配到同名的bean
						{
				        	//得到该bean元素
				        	Element bean=null;
				        	try {
								bean = DiXmlUtils.getBeanElement(this.bean_xml, action);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	//得到bean对应的类名
				        	String bean_class = DiXmlUtils.getElementValue(bean, "class");
							//检测是否有依赖
				        	boolean isRef = FindBeanRef(bean);
				        	if(isRef)//如果有依赖
				        	{
				        		//找到依赖的bean
				        		Element bean_ref;
				        		try {
									bean_ref = getBeanRef(bean);//得到依赖的bean
									String bean_ref_class = DiXmlUtils.getElementValue(bean_ref, "class");//得到依赖的bean的类名
									//使用java内省来获取LoginAction对象
									Object ActionInstance =UseIntrospector(bean_class,bean_ref_class);
									//反射执行handleLogin
									Class[] parameterTypes={User.class};//参数类
									String methodName = child.attributeValue("method");//解析配置文件中的method name
						            Method method = ActionInstance.getClass().getDeclaredMethod(methodName , parameterTypes);//获取方法
						            Object[] parameters = {user};//要传递的参数
						            String result = (String) method.invoke(ActionInstance, parameters);//实例化类，执行相应方法并返回结果
						            HandleResult(result,child,req,resp);//将返回结果与对应result name匹配,执行相应跳转	
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        	}
				        	else//无依赖
				        	{
				        		//根据class，执行相应的method,然后返回结果给result
				        		 String result=instantClass(child,userName,userPass);//实例化类，执行相应方法并返回结果
								 HandleResult(result,child,req,resp);//将返回结果与对应result name匹配,执行相应跳转	
				        	}
				        	
						}
				        else //没找到的话，直接反射,分发请求
				        {
				        	//根据class，执行相应的method,然后返回结果给result
				        	 String result=instantClass(child,userName,userPass);//实例化类，执行相应方法并返回结果
							 HandleResult(result,child,req,resp);//将返回结果与对应result name匹配,执行相应跳转	
				        }
						
		
						/*
						//判断是否配置了拦截器
					    boolean existInterceptor=false;
						existInterceptor = checkInterceptor(child);
						System.out.println("配置了拦截器:"+existInterceptor);
						
						if(existInterceptor) {//存在拦截器
						
							Element interref= child.element("interceptro-ref");
							String interrefName = interref.attributeValue("name");
							//匹配intercepter name
							Element interceptor = document.getRootElement().element("interceptor");
							String interName = interceptor.attributeValue("name");
							if(interrefName.equals(interName)) {
								System.out.println("拦截器匹配成功！");
								
								//启动代理
								Action action2=new Action();//创建业务类
								InterceptorCglib cglib=new InterceptorCglib();//创建代理类
								Action actionCglib = (Action) cglib.getInstance(action2);//创建动态代理类对象
								String result=actionCglib.handleAction(child,userName,userPass);//动态代理类对象调用方法
								HandleResult(result,child,req,resp);///将返回结果与对应result name匹配,执行相应跳转
								
								break;											
							}else {
								System.out.println("拦截器匹配不成功！");
								break;
							}	
						}else {//不存在拦截器
							 String result=instantClass(child,userName,userPass);//实例化类，执行相应方法并返回结果
							 HandleResult(result,child,req,resp);//将返回结果与对应result name匹配,执行相应跳转	
							 break;
						}*/
						break;
					}else {
					System.out.println("不可识别的action请求！");
				}
			}
			}catch (DocumentException e1) {
				e1.printStackTrace();
		}
	
			
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.println("<html>");
	        out.println("<head>");
	        out.println("<title>SimpleController</title>");
	        out.println("</head>");
	        out.println("<body>欢迎使用SimpleController</body>");
	        out.println("</html>");
	        out.flush();
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	/**
	 * //使用java内省来启动LoginAction
	 * @param beanInstance
	 * @param beanRefInstance
	 * @throws Exception 
	 */
	private Object UseIntrospector(String beanclass, String beanRefclass) throws Exception {
		// TODO Auto-generated method stub
		Class<?> cl = Class.forName(beanclass);
		// 在bean上进行内省
		BeanInfo beaninfo = Introspector.getBeanInfo(cl, Object.class);
		PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
		//实例化一个beanclass
		Object beanInstance = getReflectClass(beanclass);
		System.out.print(beanclass+"的属性有:");
		for (PropertyDescriptor pr : pro) {
			System.out.print(pr.getName() + " ");
		}
		System.out.println("");
		for (PropertyDescriptor pr : pro) {
			// 获取beal的set方法
			Method writeme = pr.getWriteMethod();
			if (pr.getName().equals("ub")) {
				// 执行方法
				//实例化被依赖的bean
				Object beanRefInstance = getReflectClass(beanRefclass);
				writeme.invoke(beanInstance, beanRefInstance);
			} 
		}
		Method method = beanInstance.getClass().getDeclaredMethod("Print");//获取方法
        method.invoke(beanInstance);//执行方法
        return beanInstance;
		
		
	}
	//找到依赖的bean
	private Element getBeanRef(Element bean) throws Exception {
		// TODO Auto-generated method stub
		List<Element> beanRef_list = bean.elements("field");
		List<String> beanNames = new ArrayList<>();
		for(Element e : beanRef_list)
		{
			String beanName = DiXmlUtils.getElementValue(e, "bean-ref");
			Element REfbean = DiXmlUtils.getBeanbyName(this.bean_xml, beanName);
			return REfbean;
		}
		return null;
	}
	//判断是否有依赖
	private boolean FindBeanRef(Element bean) {
		// TODO Auto-generated method stub
		boolean flag=false;
		List<Element> beanRef_list = bean.elements("field");
		if(beanRef_list.isEmpty())
			flag=false;
		else
			flag=true;
		return flag;
	}
	/**
	 * 查看di.xml中有无与Action同名的bean
	 * @param action
	 * @return
	 * @throws DocumentException 
	 */
	private boolean Findbean(String action)  {
		// TODO Auto-generated method stub
		List<String> actions = new ArrayList<>();
		boolean flag=false;
		try {
			actions = DiXmlUtils.getBeanName(this.bean_xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String name:actions)
		{
			if(action.equals(name))
				flag= true;
			else
				flag= false;
		}
		return flag;	
	}
	/**
	 * 解析URL,得到用户信息
	 * @param requset
	 * @return
	 */
	private User CreateUser(HttpServletRequest req) {
		// TODO Auto-generated method stub
		User user = new User();
		String id_str = (req.getParameter("id"));
        if (id_str != null) {
            user.setUserId(Integer.parseInt(id_str));
        }
        String username = req.getParameter("username");
        if (username != null) {
            user.setUserName(username);
        }
        String password = req.getParameter("password");
        if (password != null) {
            user.setPassWord(password);
        }
		return user;
	}
	
	
	/**
	 * 反射构造
	 * return 对象实例
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Object getReflectClass(String class_name) throws Exception
	{
		Class clazz = Class.forName(class_name); // 根据类名获得Class
		Object instance =  clazz.newInstance();//获取实例对象
		return instance;
	}
	

   /**
    * 实例化类，执行相应方法并返回结果
    */
	private String instantClass(Element child,String userName,String userPass) {
		System.out.println(userName);
		System.out.println(userPass);
		
		String className = child.attributeValue("class");//解析配置文件中的class name
		System.out.println("class:"+className);
		String methodName = child.attributeValue("method");//解析配置文件中的method name
		System.out.println("method:"+methodName);
	    //实例化类
		Class clazz;
		String result=null;
		try {
		clazz = Class.forName(className);
		Class[] paratype = {User.class};
		Object instance = clazz.newInstance();//实例化对象
		Method interMethod=instance.getClass().getDeclaredMethod(methodName, paratype);
		User user = new User();
		user.setUserName(userName);
		user.setPassWord(userPass);
		Object[] para = {user};//要传递的参数
		result=(String) interMethod.invoke(instance, para);
		System.out.println("返回结果："+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*
		//实例化对应类
		Class clazz;
		String result=null;
		try {
			clazz = Class.forName(className);
			Object obj=clazz.newInstance();
			Method m=clazz.getDeclaredMethod(methodName);
			result=(String) m.invoke(obj); //执行对应方法，返回请求结果
			System.out.println("返回结果："+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	*/
		return result;
		
	}
 
	/**
	 * 判断请求名称与配置文件中的action name是否匹配
	 * 
	 */
	private boolean checkActionName(Element child, String action) {
	// TODO Auto-generated method stub
		
		String actionName=child.attributeValue("name");//解析配置文件中的action name
		
		if(action.equals(actionName)) {//匹配
			return true;
		}else {
			return false;
		}
	
	}

	/**
	 * 根据反射得到的result与XML里的result进行匹配，执行forward or redirect
	 * 
	 */
	private void HandleResult(String result, Element child, HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		List<Element> childElement2=child.elements("result");//<result>
		boolean flag2=false; //判断返回结果与result name是否匹配
		for(Element child2:childElement2) {//<result>
			if(result.equals(child2.attributeValue("name"))) {//返回结果与result name匹配
				flag2=true;
				String typeName=child2.attributeValue("type");//解析对应type值
				System.out.println("type:"+typeName);
				String resultPath=child2.attributeValue("value");//解析对应value值
				System.out.println("value:"+resultPath);
				
				//action 返回 result 的资源后缀为“*_view.xml”时，转化为html页面输出
				if(resultPath.endsWith("_view.xml")) {
	        		String xmlPath = this.getServletContext().getRealPath("/pages/success_view.xml");//获取服务器加载后的路径
	        		String xslPath = this.getServletContext().getRealPath("/pages/success.xsl");
	        		//System.out.println(xmlPath);
	        		//System.out.println(xslPath);
	        		try {
	        			String content = translateToHtml(xmlPath,xslPath).toString();//将xml文件转化为html
	        			PrintWriter out=resp.getWriter();
		        		out.println(content);
		        		out.flush();
		        		out.close();
					} catch (Exception e1) {
						System.out.println("xml文件转化失败！");
					}
	        		
	        	}else {

					if(typeName.equals("forward")) {//直接转发
					
						RequestDispatcher requestDispatcher = req.getRequestDispatcher(resultPath);
						try {
							requestDispatcher.forward(req, resp);
						} catch (ServletException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }else if(typeName.equals("redirect")) {//重定向
				    	try {
							resp.sendRedirect(req.getContextPath() + resultPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				    break;
				 }
			  }
		}
		if(flag2==false) {//返回结果与result name不匹配
			System.out.println("没有请求的资源");
		}	
	}
	/**
	 * 根据xsl文件，将xml文件动态转化为html页面
	 * 
	 */
	private  ByteArrayOutputStream translateToHtml(String xmlPath, String xslPath)  throws Exception{
		TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        FileInputStream fis=new FileInputStream(xslPath);
        FileInputStream fis1=new FileInputStream(xmlPath);
        StreamSource sourceXsl = new StreamSource(fis);
        StreamSource sourceXml = new StreamSource(fis1);
    
        transformer = factory.newTransformer(sourceXsl);
         
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult output = new StreamResult(baos);

        transformer.transform(sourceXml,output);
        String str = baos.toString();
        System.out.println(str);
        return baos;

	}

	/**
	 * 检查action是否配置了拦截器
	 * 
	 */
    public boolean checkInterceptor(Element action) {
    	List<Element> childElements = action.elements("interceptro-ref");
    	if(childElements.isEmpty()) {
    		return false;
    	}else
    		return true;
    }
    
}
