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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.dsl.jsonrpc.JsonRpcHandlerMapping;
import org.springframework.dsl.jsonrpc.ServerJsonRpcExchange;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import reactor.core.publisher.Mono;

/**
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractHandlerMethodMapping<T> extends ApplicationObjectSupport
		implements JsonRpcHandlerMapping, InitializingBean {

	private static final Log log = LogFactory.getLog(AbstractHandlerMethodMapping.class);
	private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";
	private final MappingRegistry mappingRegistry = new MappingRegistry();

	@Override
	public Mono<Object> getHandler(ServerJsonRpcExchange exchange) {
		return getHandlerInternal(exchange).map(handler -> {
			return handler;
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initHandlerMethods();
	}

	public void registerMapping(T mapping, Object handler, Method method) {
		mappingRegistry.register(mapping, handler, method);
	}

	public void unregisterMapping(T mapping) {
		mappingRegistry.unregister(mapping);
	}

	public MappingRegistry getMappingRegistry() {
		return mappingRegistry;
	}

	public Mono<HandlerMethod> getHandlerInternal(ServerJsonRpcExchange exchange) {
		HandlerMethod handlerMethod = null;

		try {
			handlerMethod = lookupHandlerMethod(exchange);
		} catch (Exception ex) {
			return Mono.error(ex);
		}

		if (handlerMethod != null) {
			handlerMethod = handlerMethod.createWithResolvedBean();
		}
		return Mono.justOrEmpty(handlerMethod);
	}

	@Nullable
	protected HandlerMethod lookupHandlerMethod(ServerJsonRpcExchange exchange)
			throws Exception {
		List<Match> matches = new ArrayList<>();
		addMatchingMappings(mappingRegistry.getMappings().keySet(), matches, exchange);

		if (!matches.isEmpty()) {
			Comparator<Match> comparator = new MatchComparator(getMappingComparator(exchange));
			Collections.sort(matches, comparator);
			if (log.isTraceEnabled()) {
				log.trace("Found " + matches.size() + " matching mapping(s) for [" +
						exchange.getRequest() + "] : " + matches);
			}
			Match bestMatch = matches.get(0);
			if (matches.size() > 1) {
				Match secondBestMatch = matches.get(1);
				if (comparator.compare(bestMatch, secondBestMatch) == 0) {
					Method m1 = bestMatch.handlerMethod.getMethod();
					Method m2 = secondBestMatch.handlerMethod.getMethod();
					throw new IllegalStateException("Ambiguous handler methods mapped for JSONRCP method '" +
							exchange.getRequest().getMethod() + "': {" + m1 + ", " + m2 + "}");
				}
			}
			handleMatch(bestMatch.mapping, bestMatch.handlerMethod, exchange);
			return bestMatch.handlerMethod;
		} else {
			return handleNoMatch(mappingRegistry.getMappings().keySet(), exchange);
		}
	}

	protected HandlerMethod handleNoMatch(Set<T> mappings, ServerJsonRpcExchange exchange) throws Exception {
		return null;
	}

	protected abstract Comparator<T> getMappingComparator(ServerJsonRpcExchange exchange);

	protected abstract T getMatchingMapping(T mapping, ServerJsonRpcExchange exchange);

	/**
	 * Handle match.
	 *
	 * @param info the info
	 * @param handlerMethod the handler method
	 * @param exchange the exchange
	 */
	protected void handleMatch(T info, HandlerMethod handlerMethod,
			ServerJsonRpcExchange exchange) {

//		PathContainer lookupPath = exchange.getRequest().getPath().pathWithinApplication();
//
//		PathPattern bestPattern;
//		Map<String, String> uriVariables;
//		Map<String, MultiValueMap<String, String>> matrixVariables;
//
//		Set<PathPattern> patterns = info.getPatternsCondition().getPatterns();
//		if (patterns.isEmpty()) {
////			bestPattern = getPathPatternParser().parse(lookupPath.value());
////			uriVariables = Collections.emptyMap();
////			matrixVariables = Collections.emptyMap();
//		}
//		else {
//			bestPattern = patterns.iterator().next();
//			PathPattern.PathMatchInfo result = bestPattern.matchAndExtract(lookupPath);
//			Assert.notNull(result, () ->
//					"Expected bestPattern: " + bestPattern + " to match lookupPath " + lookupPath);
//			uriVariables = result.getUriVariables();
//			matrixVariables = result.getMatrixVariables();
//		}

//		exchange.getAttributes().put(BEST_MATCHING_HANDLER_ATTRIBUTE, handlerMethod);
//		exchange.getAttributes().put(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
//		exchange.getAttributes().put(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriVariables);
//		exchange.getAttributes().put(MATRIX_VARIABLES_ATTRIBUTE, matrixVariables);
//
//		if (!info.getProducesCondition().getProducibleMediaTypes().isEmpty()) {
//			Set<MediaType> mediaTypes = info.getProducesCondition().getProducibleMediaTypes();
//			exchange.getAttributes().put(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
//		}
	}

	private void addMatchingMappings(Collection<T> mappings, List<Match> matches, ServerJsonRpcExchange exchange) {
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, exchange);
			if (match != null) {
				matches.add(new Match(match, mappingRegistry.getMappings().get(mapping)));
			}
		}
	}

	protected void initHandlerMethods() {
		String[] beanNames = obtainApplicationContext().getBeanNamesForType(Object.class);

		for (String beanName : beanNames) {
			if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
				Class<?> beanType = null;
				try {
					beanType = obtainApplicationContext().getType(beanName);
				} catch (Throwable ex) {
					// An unresolvable bean type, probably from a lazy bean - let's ignore it.
					if (logger.isDebugEnabled()) {
						logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
					}
				}
				if (beanType != null && isHandler(beanType)) {
					detectHandlerMethods(beanName);
				}
			}
		}
	}

	protected abstract boolean isHandler(Class<?> beanType);

	protected void detectHandlerMethods(final Object handler) {
		Class<?> handlerType = (handler instanceof String ?
				obtainApplicationContext().getType((String) handler) : handler.getClass());

		if (handlerType != null) {
			final Class<?> userType = ClassUtils.getUserClass(handlerType);
			Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
					(MethodIntrospector.MetadataLookup<T>) method -> getMappingForMethod(method, userType));
			if (log.isDebugEnabled()) {
				log.debug(methods.size() + " request handler methods found on " + userType + ": " + methods);
			}
			methods.forEach((key, mapping) -> {
				Method invocableMethod = AopUtils.selectInvocableMethod(key, userType);
				registerHandlerMethod(handler, invocableMethod, mapping);
			});
		}
	}

	protected void registerHandlerMethod(Object handler, Method method, T mapping) {
		this.mappingRegistry.register(mapping, handler, method);
	}

	protected HandlerMethod createHandlerMethod(Object handler, Method method) {
		HandlerMethod handlerMethod;
		if (handler instanceof String) {
			String beanName = (String) handler;
			handlerMethod = new HandlerMethod(beanName,
					obtainApplicationContext().getAutowireCapableBeanFactory(), method);
		} else {
			handlerMethod = new HandlerMethod(handler, method);
		}
		return handlerMethod;
	}

	protected abstract T getMappingForMethod(Method method, Class<?> handlerType);

	class MappingRegistry {

		private final Map<T, MappingRegistration<T>> registry = new HashMap<>();
		private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<>();
		private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

		public Map<T, HandlerMethod> getMappings() {
			return this.mappingLookup;
		}

		public void acquireReadLock() {
			this.readWriteLock.readLock().lock();
		}

		public void releaseReadLock() {
			this.readWriteLock.readLock().unlock();
		}

		public void register(T mapping, Object handler, Method method) {
			this.readWriteLock.writeLock().lock();
			try {
				HandlerMethod handlerMethod = createHandlerMethod(handler, method);
				assertUniqueMethodMapping(handlerMethod, mapping);

				if (logger.isInfoEnabled()) {
					logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
				}
				this.mappingLookup.put(mapping, handlerMethod);
				this.registry.put(mapping, new MappingRegistration<>(mapping, handlerMethod));
			}
			finally {
				this.readWriteLock.writeLock().unlock();
			}
		}

		private void assertUniqueMethodMapping(HandlerMethod newHandlerMethod, T mapping) {
			HandlerMethod handlerMethod = this.mappingLookup.get(mapping);
			if (handlerMethod != null && !handlerMethod.equals(newHandlerMethod)) {
				throw new IllegalStateException(
						"Ambiguous mapping. Cannot map '" + newHandlerMethod.getBean() + "' method \n" +
								newHandlerMethod + "\nto " + mapping + ": There is already '" +
								handlerMethod.getBean() + "' bean method\n" + handlerMethod + " mapped.");
			}
		}

		public void unregister(T mapping) {
			this.readWriteLock.writeLock().lock();
			try {
				MappingRegistration<T> definition = this.registry.remove(mapping);
				if (definition == null) {
					return;
				}

				this.mappingLookup.remove(definition.getMapping());
			}
			finally {
				this.readWriteLock.writeLock().unlock();
			}
		}

	}

	private static class MappingRegistration<T> {

		private final T mapping;
		private final HandlerMethod handlerMethod;

		public MappingRegistration(T mapping, HandlerMethod handlerMethod) {
			Assert.notNull(mapping, "Mapping must not be null");
			Assert.notNull(handlerMethod, "HandlerMethod must not be null");
			this.mapping = mapping;
			this.handlerMethod = handlerMethod;
		}

		public T getMapping() {
			return this.mapping;
		}

		public HandlerMethod getHandlerMethod() {
			return this.handlerMethod;
		}
	}

	/**
	 * A thin wrapper around a matched HandlerMethod and its mapping, for the purpose of
	 * comparing the best match with a comparator in the context of the current request.
	 */
	private class Match {

		private final T mapping;
		private final HandlerMethod handlerMethod;

		public Match(T mapping, HandlerMethod handlerMethod) {
			this.mapping = mapping;
			this.handlerMethod = handlerMethod;
		}

		@Override
		public String toString() {
			return this.mapping.toString();
		}
	}

	private class MatchComparator implements Comparator<Match> {

		private final Comparator<T> comparator;

		public MatchComparator(Comparator<T> comparator) {
			this.comparator = comparator;
		}

		@Override
		public int compare(Match match1, Match match2) {
			return this.comparator.compare(match1.mapping, match2.mapping);
		}
	}
}
