package sc.ustc.dao.layload;

import java.util.*;

import net.sf.cglib.proxy.LazyLoader;
import sc.ustc.dao.Property;

/**
 * ʵ�������ز�ѯ
 * @author Stephanie
 *
 */
public class SqlQueryLazyLoader implements LazyLoader {

	private List<Property> lazyinfo;//���Զ���
	
	public SqlQueryLazyLoader() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object loadObject() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("��ʼ������");
		List<String> info=new ArrayList<>();
		for(Property p:lazyinfo)
		{
			info.add(p.getName());
		}
		Proxybean pb = new Proxybean(info);
		return pb;
	}
	
	public void setLazyinfo(List<Property> lazyinfo) {
		this.lazyinfo = lazyinfo;
	}
}
