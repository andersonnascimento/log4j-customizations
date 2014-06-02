package junit.test;


import org.junit.BeforeClass;
import org.junit.Test;

import cn.itcast.aop.JDKProxyFactory;
import cn.itcast.service.PersonService;
import cn.itcast.service.impl.PersonServiceBean;

public class AOPTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test 
	public void proxyTest(){
		JDKProxyFactory factory = new JDKProxyFactory();
		PersonService service = (PersonService)factory.createProxyInstance(new PersonServiceBean("user"));
		service.satve("kk");
	}
}
