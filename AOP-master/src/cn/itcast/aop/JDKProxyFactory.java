package cn.itcast.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.itcast.service.impl.PersonServiceBean;

public class JDKProxyFactory implements InvocationHandler {
	private Object targetObject;
	public Object createProxyInstance(Object targetObject){
		this.targetObject = targetObject;
		return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		PersonServiceBean bean = (PersonServiceBean)this.targetObject;
		Object result = null;
		if(bean.getUser()!= null){
			 result = method.invoke(targetObject, args);
		}
		return result;
	}
}
