package sc.ustc.dao.layload;

import java.util.*;

/**
 * ����bean
 * @author Stephanie
 *
 */
public class Proxybean {

	private List<String> lazyinfo;//���Ҫ��ѯ����Ϣ
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
