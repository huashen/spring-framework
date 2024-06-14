package org.springframework.longhuashen;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * AopTest
 *
 * @author longhuashen
 * @since 2024/06/15
 * <p>
 * 简单aop使用的demo类
 */
public class AopTest {

	private static final String CONTEXT = "test/aspectTest.xml";

	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {CONTEXT}, getClass());
		TestBean bean = (TestBean)context.getBean("test");
		bean.test();
	}
}
