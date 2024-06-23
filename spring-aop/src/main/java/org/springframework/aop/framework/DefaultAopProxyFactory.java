/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import org.springframework.aop.SpringProxy;

/**
 * Default {@link AopProxyFactory} implementation, creating either a CGLIB proxy
 * or a JDK dynamic proxy.
 *
 * <p>Creates a CGLIB proxy if one the following is true for a given
 * {@link AdvisedSupport} instance:
 * <ul>
 * <li>the {@code optimize} flag is set
 * <li>the {@code proxyTargetClass} flag is set
 * <li>no proxy interfaces have been specified
 * </ul>
 *
 * <p>In general, specify {@code proxyTargetClass} to enforce a CGLIB proxy,
 * or specify one or more interfaces to use a JDK dynamic proxy.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 12.03.2004
 * @see AdvisedSupport#setOptimize
 * @see AdvisedSupport#setProxyTargetClass
 * @see AdvisedSupport#setInterfaces
 */
public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

	private static final long serialVersionUID = 7930414337282325166L;


	@Override
	public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
		/**
		 * 如果目标对象实现了接口，默认情况下会采用JDK的动态代理实现AOP
		 * 如果目标对象实现了接口，可以强制使用CGLIB实现AOP
		 * 如果目标对象没有实现了接口，必须采用CGLIB库，Spring会自动在JDK动态代理 和CGLIB之间转换
		 *
		 * 如何强制使用CGLIB实现AOP?
		 * （1）添加 CGLIB 库，Spring_HOME/cglib/*.jar。
		 * （2）在 Spring 配置文件中加人<aop:aspectj-autoproxy proxy-target-class="true"/>。
		 *
		 * JDK动态代理和CGLIB字节码生成的区别？JDK动态代理只能对实现了接口的类生成代理，而不能针对类。
		 * CGLIB是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法，因为是继承，所以该类或方法最好不要声明成final。#
		 */

		// 如果aop配置文件没有配置属性<aop:aspectj-autoproxy />属性，则返回JdkDynamicAopProxy的实例对象
		if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
			Class<?> targetClass = config.getTargetClass();
			if (targetClass == null) {
				throw new AopConfigException("TargetSource cannot determine target class: " +
						"Either an interface or a target is required for proxy creation.");
			}
			//手动设置创建Cglib代理类后，如果目标bean是一个接口，也要创建jdk代理类
			//如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理
			if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
				return new JdkDynamicAopProxy(config);
			}
			/**
			 * targetClass就是示例中的TestBean，由于TestBean不是接口，并且不是代理类
			 * 所以要返回的ObjenesisCglibAopProxy实例对象，也就是CGLIB代理
			 * 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理。
			 */
			return new ObjenesisCglibAopProxy(config);
		}
		else {
			//默认创建jdk代理
			return new JdkDynamicAopProxy(config);
		}
	}

	/**
	 * Determine whether the supplied {@link AdvisedSupport} has only the
	 * {@link org.springframework.aop.SpringProxy} interface specified
	 * (or no proxy interfaces specified at all).
	 */
	private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
		Class<?>[] ifcs = config.getProxiedInterfaces();
		return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
	}

}
