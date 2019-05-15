package sc.ustc.dao.layload;

import java.util.*;

import net.sf.cglib.proxy.LazyLoader;
import sc.ustc.dao.Property;

/**
 * 实现懒加载查询
 * @author Stephanie
 *
 */
public class SqlQueryLazyLoader implements LazyLoader {

	private List<Property> lazyinfo;//属性对象
	
	public SqlQueryLazyLoader() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object loadObject() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("开始懒加载");
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
