/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.jsonrpc.result.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class HandlerMethod {

	private final Object bean;
	private final BeanFactory beanFactory;
	private final Class<?> beanType;
	private final Method method;
	private final Method bridgedMethod;
	private final MethodParameter[] parameters;

	/**
	 * Instantiates a new handler method.
	 *
	 * @param bean the bean
	 * @param method the method
	 */
	public HandlerMethod(Object bean, Method method) {
		Assert.notNull(bean, "Bean is required");
		Assert.notNull(method, "Method is required");
		this.bean = bean;
		this.beanFactory = null;
		this.beanType = ClassUtils.getUserClass(bean);
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
	}

	/**
	 * Instantiates a new handler method.
	 *
	 * @param beanName the bean name
	 * @param beanFactory the bean factory
	 * @param method the method
	 */
	public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
		Assert.hasText(beanName, "Bean name is required");
		Assert.notNull(beanFactory, "BeanFactory is required");
		Assert.notNull(method, "Method is required");
		this.bean = beanName;
		this.beanFactory = beanFactory;
		Class<?> beanType = beanFactory.getType(beanName);
		this.beanType = (beanType != null ? ClassUtils.getUserClass(beanType) : null);
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
	}


	/**
	 * Instantiates a new handler method.
	 *
	 * @param handlerMethod the handler method
	 */
	protected HandlerMethod(HandlerMethod handlerMethod) {
		Assert.notNull(handlerMethod, "HandlerMethod is required");
		this.bean = handlerMethod.bean;
		this.beanFactory = handlerMethod.beanFactory;
		this.beanType = handlerMethod.beanType;
		this.method = handlerMethod.method;
		this.bridgedMethod = handlerMethod.bridgedMethod;
		this.parameters = handlerMethod.parameters;
	}

	/**
	 * Instantiates a new handler method.
	 *
	 * @param handlerMethod the handler method
	 * @param handler the handler
	 */
	private HandlerMethod(HandlerMethod handlerMethod, Object handler) {
		Assert.notNull(handlerMethod, "HandlerMethod is required");
		Assert.notNull(handler, "Handler object is required");
		this.bean = handler;
		this.beanFactory = handlerMethod.beanFactory;
		this.beanType = handlerMethod.beanType;
		this.method = handlerMethod.method;
		this.bridgedMethod = handlerMethod.bridgedMethod;
		this.parameters = handlerMethod.parameters;
	}

	/**
	 * Return the bean for this handler method.
	 *
	 * @return the bean
	 */
	public Object getBean() {
		return this.bean;
	}

	/**
	 * Return the method for this handler method.
	 *
	 * @return the method
	 */
	public Method getMethod() {
		return this.method;
	}

	/**
	 * Return the HandlerMethod return type.
	 *
	 * @return the return type
	 */
	public MethodParameter getReturnType() {
		return new HandlerMethodParameter(-1);
	}

	/**
	 * This method returns the type of the handler for this handler method.
	 * <p>Note that if the bean type is a CGLIB-generated class, the original
	 * user-defined class is returned.
	 *
	 * @return the bean type
	 */
	public Class<?> getBeanType() {
		return this.beanType;
	}

	/**
	 * Return a single annotation on the underlying method traversing its super methods
	 * if no annotation can be found on the given method itself.
	 * <p>Also supports <em>merged</em> composed annotations with attribute
	 * overrides as of Spring Framework 4.2.2.
	 *
	 * @param <A> the generic type
	 * @param annotationType the type of annotation to introspect the method for
	 * @return the annotation, or {@code null} if none found
	 * @see AnnotatedElementUtils#findMergedAnnotation
	 */
	@Nullable
	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
		return AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
	}

	/**
	 * Return whether the parameter is declared with the given annotation type.
	 *
	 * @param <A> the generic type
	 * @param annotationType the annotation type to look for
	 * @return true, if successful
	 * @see AnnotatedElementUtils#hasAnnotation
	 */
	public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
		return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
	}

	/**
	 * If the bean method is a bridge method, this method returns the bridged
	 * (user-defined) method. Otherwise it returns the same method as {@link #getMethod()}.
	 *
	 * @return the bridged method
	 */
	protected Method getBridgedMethod() {
		return this.bridgedMethod;
	}

	/**
	 * Return the method parameters for this handler method.
	 *
	 * @return the method parameters for this handler method
	 */
	public MethodParameter[] getMethodParameters() {
		return this.parameters;
	}

	/**
	 * Creates the with resolved bean.
	 *
	 * @return the handler method
	 */
	public HandlerMethod createWithResolvedBean() {
		Object handler = this.bean;
		if (this.bean instanceof String) {
			String beanName = (String) this.bean;
			handler = this.beanFactory.getBean(beanName);
		}
		return new HandlerMethod(this, handler);
	}

	/**
	 * A MethodParameter with HandlerMethod-specific behavior.
	 */
	protected class HandlerMethodParameter extends SynthesizingMethodParameter {

		/**
		 * Instantiates a new handler method parameter.
		 *
		 * @param index the index
		 */
		public HandlerMethodParameter(int index) {
			super(HandlerMethod.this.bridgedMethod, index);
		}

		/**
		 * Instantiates a new handler method parameter.
		 *
		 * @param original the original
		 */
		protected HandlerMethodParameter(HandlerMethodParameter original) {
			super(original);
		}

		@Override
		public Class<?> getContainingClass() {
			return HandlerMethod.this.getBeanType();
		}

		@Override
		public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
			return HandlerMethod.this.getMethodAnnotation(annotationType);
		}

		@Override
		public <T extends Annotation> boolean hasMethodAnnotation(Class<T> annotationType) {
			return HandlerMethod.this.hasMethodAnnotation(annotationType);
		}

		@Override
		public HandlerMethodParameter clone() {
			return new HandlerMethodParameter(this);
		}
	}

	/**
	 * Inits the method parameters.
	 *
	 * @return the method parameter[]
	 */
	private MethodParameter[] initMethodParameters() {
		int count = this.bridgedMethod.getParameterCount();
		MethodParameter[] result = new MethodParameter[count];
		for (int i = 0; i < count; i++) {
			HandlerMethodParameter parameter = new HandlerMethodParameter(i);
			GenericTypeResolver.resolveParameterType(parameter, this.beanType);
			result[i] = parameter;
		}
		return result;
	}
}
