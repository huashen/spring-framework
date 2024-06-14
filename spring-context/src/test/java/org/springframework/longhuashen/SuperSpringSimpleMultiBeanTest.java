package org.springframework.longhuashen;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringSimpleMultiBeanTest
 *
 * @author longhuashen
 * @since 23/11/11
 */
public class SuperSpringSimpleMultiBeanTest {

    /**
     ①首先执行的是构造函数
     ②然后执行的BeanNameAware这个接口中的方法
     ③然后执行的是BeanFactoryAware这个接口中的方法
     ④执行InitializingBean接口中的afterPropertiesSet的方法
     ⑤执行我们在xml中定义的init-method这个方法
     ⑥最后执行的是BeanFactoryPostProcessor这个方法
     */
    private static final String CONTEXT = "test/aspectTest.xml";

    @Test
    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {CONTEXT}, getClass());
		TestBean bean = (TestBean)context.getBean("test");
		bean.test();
    }
}
