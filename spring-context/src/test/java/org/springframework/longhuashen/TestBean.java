package org.springframework.longhuashen;

/**
 * TestBean
 *
 * @author longhuashen
 * @since 2024/06/15
 *
 * 自定义bean，测试aop用的简单bean
 */
public class TestBean {

	private String testStr = "testStr";

	public String getTestStr() {
		return testStr;
	}

	public void setTestStr(String testStr) {
		this.testStr = testStr;
	}

	public void test() {
		System.out.println("龙猫 test spring源码");
	}
}
