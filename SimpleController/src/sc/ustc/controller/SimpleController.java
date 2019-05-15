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
	private File bean_xml=new File(this.getClass().getResource("/di.xml").getFile());//��ȡxml;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
		resp.setContentType("text/html;charset=UTF-8");
		String url = req.getRequestURL().toString();//��ȡ����URL
		System.out.println(url);
		String userName=req.getParameter("username");//��ȡ���ύ���û���
		String userPass=req.getParameter("password");//��ȡ���ύ������
		//��URL�е���Ϣ�����User
        User user = CreateUser(req);
		System.out.println("�ύ���û�����"+userName);
		System.out.println("�ύ�����룺"+userPass);
		
		
		String action=url.substring(url .lastIndexOf("/")+1,url.length()-3);//��ȡ����action����
		System.out.println(action);
		
		//���������ļ�controller.xml
		SAXReader reader = new SAXReader();
		Document document;
			try {
				document = reader.read(new File(this.getClass().getResource("/controller.xml").getFile()));
				Element root = document.getRootElement().element("controller");//<controller>
			    List<Element> childElements = root.elements();// action		  
			    boolean matchActionName=false;//�ж����������������ļ��е�action name�Ƿ�ƥ��
				for (Element child : childElements) {//<action>
					
					//�ж��������Ƿ�ƥ��
					matchActionName=checkActionName(child,action);
					if(matchActionName) {
					
						//�鿴di.xml��������Actionͬ����bean�ڵ�
						boolean isBeanMatched= Findbean(action);
						if(isBeanMatched)//��ƥ�䵽ͬ����bean
						{
				        	//�õ���beanԪ��
				        	Element bean=null;
				        	try {
								bean = DiXmlUtils.getBeanElement(this.bean_xml, action);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	//�õ�bean��Ӧ������
				        	String bean_class = DiXmlUtils.getElementValue(bean, "class");
							//����Ƿ�������
				        	boolean isRef = FindBeanRef(bean);
				        	if(isRef)//���������
				        	{
				        		//�ҵ�������bean
				        		Element bean_ref;
				        		try {
									bean_ref = getBeanRef(bean);//�õ�������bean
									String bean_ref_class = DiXmlUtils.getElementValue(bean_ref, "class");//�õ�������bean������
									//ʹ��java��ʡ����ȡLoginAction����
									Object ActionInstance =UseIntrospector(bean_class,bean_ref_class);
									//����ִ��handleLogin
									Class[] parameterTypes={User.class};//������
									String methodName = child.attributeValue("method");//���������ļ��е�method name
						            Method method = ActionInstance.getClass().getDeclaredMethod(methodName , parameterTypes);//��ȡ����
						            Object[] parameters = {user};//Ҫ���ݵĲ���
						            String result = (String) method.invoke(ActionInstance, parameters);//ʵ�����ִ࣬����Ӧ���������ؽ��
						            HandleResult(result,child,req,resp);//�����ؽ�����Ӧresult nameƥ��,ִ����Ӧ��ת	
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        	}
				        	else//������
				        	{
				        		//����class��ִ����Ӧ��method,Ȼ�󷵻ؽ����result
				        		 String result=instantClass(child,userName,userPass);//ʵ�����ִ࣬����Ӧ���������ؽ��
								 HandleResult(result,child,req,resp);//�����ؽ�����Ӧresult nameƥ��,ִ����Ӧ��ת	
				        	}
				        	
						}
				        else //û�ҵ��Ļ���ֱ�ӷ���,�ַ�����
				        {
				        	//����class��ִ����Ӧ��method,Ȼ�󷵻ؽ����result
				        	 String result=instantClass(child,userName,userPass);//ʵ�����ִ࣬����Ӧ���������ؽ��
							 HandleResult(result,child,req,resp);//�����ؽ�����Ӧresult nameƥ��,ִ����Ӧ��ת	
				        }
						
		
						/*
						//�ж��Ƿ�������������
					    boolean existInterceptor=false;
						existInterceptor = checkInterceptor(child);
						System.out.println("������������:"+existInterceptor);
						
						if(existInterceptor) {//����������
						
							Element interref= child.element("interceptro-ref");
							String interrefName = interref.attributeValue("name");
							//ƥ��intercepter name
							Element interceptor = document.getRootElement().element("interceptor");
							String interName = interceptor.attributeValue("name");
							if(interrefName.equals(interName)) {
								System.out.println("������ƥ��ɹ���");
								
								//��������
								Action action2=new Action();//����ҵ����
								InterceptorCglib cglib=new InterceptorCglib();//����������
								Action actionCglib = (Action) cglib.getInstance(action2);//������̬���������
								String result=actionCglib.handleAction(child,userName,userPass);//��̬�����������÷���
								HandleResult(result,child,req,resp);///�����ؽ�����Ӧresult nameƥ��,ִ����Ӧ��ת
								
								break;											
							}else {
								System.out.println("������ƥ�䲻�ɹ���");
								break;
							}	
						}else {//������������
							 String result=instantClass(child,userName,userPass);//ʵ�����ִ࣬����Ӧ���������ؽ��
							 HandleResult(result,child,req,resp);//�����ؽ�����Ӧresult nameƥ��,ִ����Ӧ��ת	
							 break;
						}*/
						break;
					}else {
					System.out.println("����ʶ���action����");
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
	        out.println("<body>��ӭʹ��SimpleController</body>");
	        out.println("</html>");
	        out.flush();
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	/**
	 * //ʹ��java��ʡ������LoginAction
	 * @param beanInstance
	 * @param beanRefInstance
	 * @throws Exception 
	 */
	private Object UseIntrospector(String beanclass, String beanRefclass) throws Exception {
		// TODO Auto-generated method stub
		Class<?> cl = Class.forName(beanclass);
		// ��bean�Ͻ�����ʡ
		BeanInfo beaninfo = Introspector.getBeanInfo(cl, Object.class);
		PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
		//ʵ����һ��beanclass
		Object beanInstance = getReflectClass(beanclass);
		System.out.print(beanclass+"��������:");
		for (PropertyDescriptor pr : pro) {
			System.out.print(pr.getName() + " ");
		}
		System.out.println("");
		for (PropertyDescriptor pr : pro) {
			// ��ȡbeal��set����
			Method writeme = pr.getWriteMethod();
			if (pr.getName().equals("ub")) {
				// ִ�з���
				//ʵ������������bean
				Object beanRefInstance = getReflectClass(beanRefclass);
				writeme.invoke(beanInstance, beanRefInstance);
			} 
		}
		Method method = beanInstance.getClass().getDeclaredMethod("Print");//��ȡ����
        method.invoke(beanInstance);//ִ�з���
        return beanInstance;
		
		
	}
	//�ҵ�������bean
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
	//�ж��Ƿ�������
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
	 * �鿴di.xml��������Actionͬ����bean
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
	 * ����URL,�õ��û���Ϣ
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
	 * ���乹��
	 * return ����ʵ��
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Object getReflectClass(String class_name) throws Exception
	{
		Class clazz = Class.forName(class_name); // �����������Class
		Object instance =  clazz.newInstance();//��ȡʵ������
		return instance;
	}
	

   /**
    * ʵ�����ִ࣬����Ӧ���������ؽ��
    */
	private String instantClass(Element child,String userName,String userPass) {
		System.out.println(userName);
		System.out.println(userPass);
		
		String className = child.attributeValue("class");//���������ļ��е�class name
		System.out.println("class:"+className);
		String methodName = child.attributeValue("method");//���������ļ��е�method name
		System.out.println("method:"+methodName);
	    //ʵ������
		Class clazz;
		String result=null;
		try {
		clazz = Class.forName(className);
		Class[] paratype = {User.class};
		Object instance = clazz.newInstance();//ʵ��������
		Method interMethod=instance.getClass().getDeclaredMethod(methodName, paratype);
		User user = new User();
		user.setUserName(userName);
		user.setPassWord(userPass);
		Object[] para = {user};//Ҫ���ݵĲ���
		result=(String) interMethod.invoke(instance, para);
		System.out.println("���ؽ����"+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*
		//ʵ������Ӧ��
		Class clazz;
		String result=null;
		try {
			clazz = Class.forName(className);
			Object obj=clazz.newInstance();
			Method m=clazz.getDeclaredMethod(methodName);
			result=(String) m.invoke(obj); //ִ�ж�Ӧ����������������
			System.out.println("���ؽ����"+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	*/
		return result;
		
	}
 
	/**
	 * �ж����������������ļ��е�action name�Ƿ�ƥ��
	 * 
	 */
	private boolean checkActionName(Element child, String action) {
	// TODO Auto-generated method stub
		
		String actionName=child.attributeValue("name");//���������ļ��е�action name
		
		if(action.equals(actionName)) {//ƥ��
			return true;
		}else {
			return false;
		}
	
	}

	/**
	 * ���ݷ���õ���result��XML���result����ƥ�䣬ִ��forward or redirect
	 * 
	 */
	private void HandleResult(String result, Element child, HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		List<Element> childElement2=child.elements("result");//<result>
		boolean flag2=false; //�жϷ��ؽ����result name�Ƿ�ƥ��
		for(Element child2:childElement2) {//<result>
			if(result.equals(child2.attributeValue("name"))) {//���ؽ����result nameƥ��
				flag2=true;
				String typeName=child2.attributeValue("type");//������Ӧtypeֵ
				System.out.println("type:"+typeName);
				String resultPath=child2.attributeValue("value");//������Ӧvalueֵ
				System.out.println("value:"+resultPath);
				
				//action ���� result ����Դ��׺Ϊ��*_view.xml��ʱ��ת��Ϊhtmlҳ�����
				if(resultPath.endsWith("_view.xml")) {
	        		String xmlPath = this.getServletContext().getRealPath("/pages/success_view.xml");//��ȡ���������غ��·��
	        		String xslPath = this.getServletContext().getRealPath("/pages/success.xsl");
	        		//System.out.println(xmlPath);
	        		//System.out.println(xslPath);
	        		try {
	        			String content = translateToHtml(xmlPath,xslPath).toString();//��xml�ļ�ת��Ϊhtml
	        			PrintWriter out=resp.getWriter();
		        		out.println(content);
		        		out.flush();
		        		out.close();
					} catch (Exception e1) {
						System.out.println("xml�ļ�ת��ʧ�ܣ�");
					}
	        		
	        	}else {

					if(typeName.equals("forward")) {//ֱ��ת��
					
						RequestDispatcher requestDispatcher = req.getRequestDispatcher(resultPath);
						try {
							requestDispatcher.forward(req, resp);
						} catch (ServletException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }else if(typeName.equals("redirect")) {//�ض���
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
		if(flag2==false) {//���ؽ����result name��ƥ��
			System.out.println("û���������Դ");
		}	
	}
	/**
	 * ����xsl�ļ�����xml�ļ���̬ת��Ϊhtmlҳ��
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
	 * ���action�Ƿ�������������
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
