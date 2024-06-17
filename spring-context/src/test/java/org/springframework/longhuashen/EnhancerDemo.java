package org.springframework.longhuashen;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * EnhancerDemo
 *
 * @author longhuashen
 * @since 2024/06/17
 */
public class EnhancerDemo {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(EnhancerDemo.class);
		//设置拦截器，来拦截代理类的方法
		enhancer.setCallback(new MethodInterceptorImpl());
		EnhancerDemo demo = (EnhancerDemo) enhancer.create();
		demo.test();
		System.out.println(demo);
	}

	public void test() {
		System.out.println("EnhancerDemo test()");
	}

	private static class MethodInterceptorImpl implements MethodInterceptor {
		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			System.err.println("Before invoke " + method);
			Object result = methodProxy.invokeSuper(o, objects);
			System.err.println("After invoke" + method);
			return result;
		}
	}
}
