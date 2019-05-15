package sc.ustc.dao.layload;

import java.util.*;

/**
 * 代理bean
 * @author Stephanie
 *
 */
public class Proxybean {

	private List<String> lazyinfo;//存放要查询的信息
	public Proxybean(List<String> info) {
		// TODO Auto-generated constructor stub
		this.lazyinfo=info;
	}
	public List<String> getLazyinfo() {
		return lazyinfo;
	}
	public Proxybean() {
		// TODO Auto-generated constructor stub
	}
}
