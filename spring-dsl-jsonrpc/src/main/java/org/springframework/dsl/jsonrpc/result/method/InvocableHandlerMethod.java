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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerResult;
import org.springframework.dsl.jsonrpc.JsonRpcOutputMessage;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import reactor.core.publisher.Mono;

/**
 * Extension of {@link HandlerMethod} that invokes the underlying method with
 * argument values resolved from the current JSONRPC request through a list of
 * {@link JsonRpcHandlerMethodArgumentResolver}.
 *
 * @author Janne Valkealahti
 *
 */
public class InvocableHandlerMethod extends HandlerMethod {

	private static final Logger log = LoggerFactory.getLogger(InvocableHandlerMethod.class);
	private static final Mono<Object[]> EMPTY_ARGS = Mono.just(new Object[0]);
	private static final Object NO_ARG_VALUE = new Object();
	private List<JsonRpcHandlerMethodArgumentResolver> resolvers = new ArrayList<>();
	private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	private ReactiveAdapterRegistry reactiveAdapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	/**
	 * Instantiates a new invocable handler method.
	 *
	 * @param handlerMethod the handler method
	 */
	public InvocableHandlerMethod(HandlerMethod handlerMethod) {
		super(handlerMethod);
	}

	/**
	 * Instantiates a new invocable handler method.
	 *
	 * @param bean the bean
	 * @param method the method
	 */
	public InvocableHandlerMethod(Object bean, Method method) {
		super(bean, method);
	}

	/**
	 * Configure the argument resolvers to use to use for resolving method
	 * argument values against a {@code ServerCoapExchange}.
	 *
	 * @param resolvers the argument resolvers
	 */
	public void setArgumentResolvers(List<JsonRpcHandlerMethodArgumentResolver> resolvers) {
		this.resolvers.clear();
		this.resolvers.addAll(resolvers);
	}

	/**
	 * Return the configured argument resolvers.
	 *
	 * @return the configured argument resolvers
	 */
	public List<JsonRpcHandlerMethodArgumentResolver> getResolvers() {
		return this.resolvers;
	}

	/**
	 * Invoke.
	 *
	 * @param exchange the exchange
	 * @param providedArgs the provided args
	 * @return the mono
	 */
	public Mono<JsonRpcHandlerResult> invoke(ServerJsonRpcExchange exchange, Object... providedArgs) {
		return resolveArguments(exchange, providedArgs).flatMap(args -> {
			try {
				Object value = doInvoke(args);

				MethodParameter returnType = getReturnType();
				ReactiveAdapter adapter = this.reactiveAdapterRegistry.getAdapter(returnType.getParameterType());
				boolean asyncVoid = isAsyncVoidReturnType(returnType, adapter);
				if ((value == null || asyncVoid) && isResponseHandled(args, exchange)) {
					log.debug("Response fully handled in controller method");
					return asyncVoid ? Mono.from(adapter.toPublisher(value)) : Mono.empty();
				}

				JsonRpcHandlerResult result = new JsonRpcHandlerResult(this, value, getReturnType());
				return Mono.just(result);
			}
			catch (InvocationTargetException ex) {
				return Mono.error(ex.getTargetException());
			}
			catch (Throwable ex) {
				return Mono.error(new IllegalStateException(getInvocationErrorMessage(new String[0])));
			}
		});
	}

	/**
	 * Resolve arguments.
	 *
	 * @param exchange the exchange
	 * @param providedArgs the provided args
	 * @return the mono
	 */
	private Mono<Object[]> resolveArguments(ServerJsonRpcExchange exchange, Object... providedArgs) {

		if (ObjectUtils.isEmpty(getMethodParameters())) {
			return EMPTY_ARGS;
		}
		try {
			List<Mono<Object>> argMonos = Stream.of(getMethodParameters())
					.map(param -> {
						param.initParameterNameDiscovery(this.parameterNameDiscoverer);
						return findProvidedArgument(param, providedArgs)
								.map(Mono::just)
								.orElseGet(() -> {
									JsonRpcHandlerMethodArgumentResolver resolver = findResolver(param);
									return resolveArg(resolver, param, exchange);
								});

					})
					.collect(Collectors.toList());

			// Create Mono with array of resolved values...
			return Mono.zip(argMonos, argValues ->
					Stream.of(argValues).map(o -> o != NO_ARG_VALUE ? o : null).toArray());
		}
		catch (Throwable ex) {
			return Mono.error(ex);
		}
	}

	/**
	 * Find provided argument.
	 *
	 * @param parameter the parameter
	 * @param providedArgs the provided args
	 * @return the optional
	 */
	private Optional<Object> findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
		if (ObjectUtils.isEmpty(providedArgs)) {
			return Optional.empty();
		}
		return Arrays.stream(providedArgs)
				.filter(arg -> parameter.getParameterType().isInstance(arg))
				.findFirst();
	}

	/**
	 * Find resolver.
	 *
	 * @param param the param
	 * @return the lsp handler method argument resolver
	 */
	private JsonRpcHandlerMethodArgumentResolver findResolver(MethodParameter param) {
		return this.resolvers.stream()
				.filter(r -> r.supportsParameter(param))
				.findFirst()
				.orElseThrow(() -> getArgumentError("No suitable resolver for", param, null));
	}

	/**
	 * Resolve arg.
	 *
	 * @param resolver the resolver
	 * @param parameter the parameter
	 * @param exchange the exchange
	 * @return the mono
	 */
	private Mono<Object> resolveArg(JsonRpcHandlerMethodArgumentResolver resolver, MethodParameter parameter,
			ServerJsonRpcExchange exchange) {

		try {
			return resolver.resolveArgument(parameter, exchange)
					.defaultIfEmpty(NO_ARG_VALUE)
					.doOnError(cause -> {
						if (log.isDebugEnabled()) {
							log.debug(getDetailedErrorMessage("Failed to resolve", parameter), cause);
						}
					});
		}
		catch (Exception ex) {
			throw getArgumentError("Failed to resolve", parameter, ex);
		}
	}

	/**
	 * Gets the argument error.
	 *
	 * @param text the text
	 * @param parameter the parameter
	 * @param ex the ex
	 * @return the argument error
	 */
	private IllegalStateException getArgumentError(String text, MethodParameter parameter, @Nullable Throwable ex) {
		return new IllegalStateException(getDetailedErrorMessage(text, parameter), ex);
	}

	/**
	 * Gets the detailed error message.
	 *
	 * @param text the text
	 * @param param the param
	 * @return the detailed error message
	 */
	private String getDetailedErrorMessage(String text, MethodParameter param) {
		return text + " argument " + param.getParameterIndex() + " of type '" +
				param.getParameterType().getName() + "' on " + getBridgedMethod().toGenericString();
	}

	private boolean isAsyncVoidReturnType(MethodParameter returnType,
			@Nullable ReactiveAdapter reactiveAdapter) {

		if (reactiveAdapter != null && reactiveAdapter.supportsEmpty()) {
			if (reactiveAdapter.isNoValue()) {
				return true;
			}
			Type parameterType = returnType.getGenericParameterType();
			if (parameterType instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) parameterType;
				if (type.getActualTypeArguments().length == 1) {
					return Void.class.equals(type.getActualTypeArguments()[0]);
				}
			}
		}
		return false;
	}

	/**
	 * Do invoke.
	 *
	 * @param args the args
	 * @return the object
	 * @throws Exception the exception
	 */
	private Object doInvoke(Object[] args) throws Exception {
		if (log.isTraceEnabled()) {
			log.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
					"' with arguments " + Arrays.toString(args));
		}
		ReflectionUtils.makeAccessible(getBridgedMethod());
		Object returnValue = getBridgedMethod().invoke(getBean(), args);
		if (log.isTraceEnabled()) {
			log.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
					"] returned [" + returnValue + "]");
		}
		return returnValue;
	}

	/**
	 * Gets the invocation error message.
	 *
	 * @param args the args
	 * @return the invocation error message
	 */
	private String getInvocationErrorMessage(Object[] args) {
		String argumentDetails = IntStream.range(0, args.length)
				.mapToObj(i -> (args[i] != null ?
						"[" + i + "][type=" + args[i].getClass().getName() + "][value=" + args[i] + "]" :
						"[" + i + "][null]"))
				.collect(Collectors.joining(",", " ", " "));
		return "Failed to invoke handler method with resolved arguments:" + argumentDetails +
				"on " + getBridgedMethod().toGenericString();
	}

	private boolean isResponseHandled(Object[] args, ServerJsonRpcExchange exchange) {
//		if (getResponseStatus() != null || exchange.isNotModified()) {
//			return true;
//		}
		for (Object arg : args) {
			if (arg instanceof JsonRpcOutputMessage || arg instanceof ServerJsonRpcExchange) {
				return true;
			}
		}
		return false;
	}

}
