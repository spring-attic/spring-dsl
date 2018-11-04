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
package org.springframework.dsl.jsonrpc.jackson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.FilterProvider;

/**
 * A {@link FactoryBean} for creating a Jackson 2.x {@link ObjectMapper} with setters
 * to enable or disable Jackson features.
 *
 * <p>It customizes Jackson defaults properties with the following ones:
 * <ul>
 * <li>{@link MapperFeature#DEFAULT_VIEW_INCLUSION} is disabled</li>
 * <li>{@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} is disabled</li>
 * </ul>
 *
 * <p>It also automatically registers the following well-known modules if they are
 * detected on the classpath:
 * <ul>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-jdk7">jackson-datatype-jdk7</a>:
 * support for Java 7 types like {@link java.nio.file.Path}</li>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-jdk8">jackson-datatype-jdk8</a>:
 * support for other Java 8 types like {@link java.util.Optional}</li>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-jsr310">jackson-datatype-jsr310</a>:
 * support for Java 8 Date & Time API types</li>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-joda">jackson-datatype-joda</a>:
 * support for Joda-Time types</li>
 * </ul>
 *
 * <p>In case you want to configure Jackson's {@link ObjectMapper} with a custom {@link Module},
 * you can register one or more such Modules by class name via {@link #setModulesToInstall}.
 *
 * <p>Compatible with Jackson 2.6 and higher.
 *
 * @author <a href="mailto:dmitry.katsubo@gmail.com">Dmitry Katsubo</a>
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @author Juergen Hoeller
 * @author Tadaya Tsuyukubo
 * @author Sebastien Deleuze
 *
 */
public class JsonRpcJackson2ObjectMapperFactoryBean implements FactoryBean<ObjectMapper>, BeanClassLoaderAware,
		ApplicationContextAware, InitializingBean {

	private final JsonRpcJackson2ObjectMapperBuilder builder = new JsonRpcJackson2ObjectMapperBuilder();

	@Nullable
	private ObjectMapper objectMapper;


	/**
	 * Set the {@link ObjectMapper} instance to use. If not set, the {@link ObjectMapper}
	 * will be created using its default constructor.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Define the {@link JsonFactory} to be used to create the {@link ObjectMapper}
	 * instance.
	 *
	 * @param factory the json factory
	 */
	public void setFactory(JsonFactory factory) {
		this.builder.factory(factory);
	}

	/**
	 * Define the format for date/time with the given {@link DateFormat}.
	 * <p>Note: Setting this property makes the exposed {@link ObjectMapper}
	 * non-thread-safe, according to Jackson's thread safety rules.
	 * @see #setSimpleDateFormat(String)
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.builder.dateFormat(dateFormat);
	}

	/**
	 * Define the date/time format with a {@link SimpleDateFormat}.
	 * <p>Note: Setting this property makes the exposed {@link ObjectMapper}
	 * non-thread-safe, according to Jackson's thread safety rules.
	 * @see #setDateFormat(DateFormat)
	 */
	public void setSimpleDateFormat(String format) {
		this.builder.simpleDateFormat(format);
	}

	/**
	 * Override the default {@link Locale} to use for formatting.
	 * Default value used is {@link Locale#getDefault()}.
	 */
	public void setLocale(Locale locale) {
		this.builder.locale(locale);
	}

	/**
	 * Override the default {@link TimeZone} to use for formatting.
	 * Default value used is UTC (NOT local timezone).
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.builder.timeZone(timeZone);
	}

	/**
	 * Set an {@link AnnotationIntrospector} for both serialization and deserialization.
	 */
	public void setAnnotationIntrospector(AnnotationIntrospector annotationIntrospector) {
		this.builder.annotationIntrospector(annotationIntrospector);
	}

	/**
	 * Specify a {@link com.fasterxml.jackson.databind.PropertyNamingStrategy} to
	 * configure the {@link ObjectMapper} with.
	 */
	public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
		this.builder.propertyNamingStrategy(propertyNamingStrategy);
	}

	/**
	 * Specify a {@link TypeResolverBuilder} to use for Jackson's default typing.
	 */
	public void setDefaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
		this.builder.defaultTyping(typeResolverBuilder);
	}

	/**
	 * Set a custom inclusion strategy for serialization.
	 * @see com.fasterxml.jackson.annotation.JsonInclude.Include
	 */
	public void setSerializationInclusion(JsonInclude.Include serializationInclusion) {
		this.builder.serializationInclusion(serializationInclusion);
	}

	/**
	 * Set the global filters to use in order to support {@link JsonFilter @JsonFilter} annotated POJO.
	 * @see JsonRpcJackson2ObjectMapperBuilder#filters(FilterProvider)
	 */
	public void setFilters(FilterProvider filters) {
		this.builder.filters(filters);
	}

	/**
	 * Add mix-in annotations to use for augmenting specified class or interface.
	 * @param mixIns a Map of entries with target classes (or interface) whose annotations
	 * to effectively override as key and mix-in classes (or interface) whose
	 * annotations are to be "added" to target's annotations as value.
	 * @see com.fasterxml.jackson.databind.ObjectMapper#addMixInAnnotations(Class, Class)
	 */
	public void setMixIns(Map<Class<?>, Class<?>> mixIns) {
		this.builder.mixIns(mixIns);
	}

	/**
	 * Configure custom serializers. Each serializer is registered for the type
	 * returned by {@link JsonSerializer#handledType()}, which must not be {@code null}.
	 * @see #setSerializersByType(Map)
	 */
	public void setSerializers(JsonSerializer<?>... serializers) {
		this.builder.serializers(serializers);
	}

	/**
	 * Configure custom serializers for the given types.
	 * @see #setSerializers(JsonSerializer...)
	 */
	public void setSerializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
		this.builder.serializersByType(serializers);
	}

	/**
	 * Configure custom deserializers. Each deserializer is registered for the type
	 * returned by {@link JsonDeserializer#handledType()}, which must not be {@code null}.
	 * @see #setDeserializersByType(Map)
	 */
	public void setDeserializers(JsonDeserializer<?>... deserializers) {
		this.builder.deserializers(deserializers);
	}

	/**
	 * Configure custom deserializers for the given types.
	 */
	public void setDeserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
		this.builder.deserializersByType(deserializers);
	}

	/**
	 * Shortcut for {@link MapperFeature#AUTO_DETECT_FIELDS} option.
	 */
	public void setAutoDetectFields(boolean autoDetectFields) {
		this.builder.autoDetectFields(autoDetectFields);
	}

	/**
	 * Shortcut for {@link MapperFeature#AUTO_DETECT_SETTERS}/
	 * {@link MapperFeature#AUTO_DETECT_GETTERS}/{@link MapperFeature#AUTO_DETECT_IS_GETTERS}
	 * options.
	 */
	public void setAutoDetectGettersSetters(boolean autoDetectGettersSetters) {
		this.builder.autoDetectGettersSetters(autoDetectGettersSetters);
	}

	/**
	 * Shortcut for {@link MapperFeature#DEFAULT_VIEW_INCLUSION} option.
	 * @since 4.1
	 */
	public void setDefaultViewInclusion(boolean defaultViewInclusion) {
		this.builder.defaultViewInclusion(defaultViewInclusion);
	}

	/**
	 * Shortcut for {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} option.
	 */
	public void setFailOnUnknownProperties(boolean failOnUnknownProperties) {
		this.builder.failOnUnknownProperties(failOnUnknownProperties);
	}

	/**
	 * Shortcut for {@link SerializationFeature#FAIL_ON_EMPTY_BEANS} option.
	 */
	public void setFailOnEmptyBeans(boolean failOnEmptyBeans) {
		this.builder.failOnEmptyBeans(failOnEmptyBeans);
	}

	/**
	 * Shortcut for {@link SerializationFeature#INDENT_OUTPUT} option.
	 */
	public void setIndentOutput(boolean indentOutput) {
		this.builder.indentOutput(indentOutput);
	}

	/**
	 * Specify features to enable.
	 * @see com.fasterxml.jackson.core.JsonParser.Feature
	 * @see com.fasterxml.jackson.core.JsonGenerator.Feature
	 * @see com.fasterxml.jackson.databind.SerializationFeature
	 * @see com.fasterxml.jackson.databind.DeserializationFeature
	 * @see com.fasterxml.jackson.databind.MapperFeature
	 */
	public void setFeaturesToEnable(Object... featuresToEnable) {
		this.builder.featuresToEnable(featuresToEnable);
	}

	/**
	 * Specify features to disable.
	 * @see com.fasterxml.jackson.core.JsonParser.Feature
	 * @see com.fasterxml.jackson.core.JsonGenerator.Feature
	 * @see com.fasterxml.jackson.databind.SerializationFeature
	 * @see com.fasterxml.jackson.databind.DeserializationFeature
	 * @see com.fasterxml.jackson.databind.MapperFeature
	 */
	public void setFeaturesToDisable(Object... featuresToDisable) {
		this.builder.featuresToDisable(featuresToDisable);
	}

	/**
	 * Set a complete list of modules to be registered with the {@link ObjectMapper}.
	 * <p>Note: If this is set, no finding of modules is going to happen - not by
	 * Jackson, and not by Spring either (see {@link #setFindModulesViaServiceLoader}).
	 * As a consequence, specifying an empty list here will suppress any kind of
	 * module detection.
	 * <p>Specify either this or {@link #setModulesToInstall}, not both.
	 * @see com.fasterxml.jackson.databind.Module
	 */
	public void setModules(List<Module> modules) {
		this.builder.modules(modules);
	}

	/**
	 * Specify one or more modules by class (or class name in XML)
	 * to be registered with the {@link ObjectMapper}.
	 * <p>Modules specified here will be registered after
	 * Spring's autodetection of JSR-310 and Joda-Time, or Jackson's
	 * finding of modules (see {@link #setFindModulesViaServiceLoader}),
	 * allowing to eventually override their configuration.
	 * <p>Specify either this or {@link #setModules}, not both.
	 * @see com.fasterxml.jackson.databind.Module
	 */
	@SuppressWarnings("unchecked")
	public void setModulesToInstall(Class<? extends Module>... modules) {
		this.builder.modulesToInstall(modules);
	}

	/**
	 * Set whether to let Jackson find available modules via the JDK ServiceLoader,
	 * based on META-INF metadata in the classpath. Requires Jackson 2.2 or higher.
	 * <p>If this mode is not set, Spring's Jackson2ObjectMapperFactoryBean itself
	 * will try to find the JSR-310 and Joda-Time support modules on the classpath -
	 * provided that Java 8 and Joda-Time themselves are available, respectively.
	 * @see com.fasterxml.jackson.databind.ObjectMapper#findModules()
	 */
	public void setFindModulesViaServiceLoader(boolean findModules) {
		this.builder.findModulesViaServiceLoader(findModules);
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.builder.moduleClassLoader(beanClassLoader);
	}

	/**
	 * Customize the construction of Jackson handlers
	 * ({@link JsonSerializer}, {@link JsonDeserializer}, {@link KeyDeserializer},
	 * {@code TypeResolverBuilder} and {@code TypeIdResolver}).
	 * @see JsonRpcJackson2ObjectMapperFactoryBean#setApplicationContext(ApplicationContext)
	 */
	public void setHandlerInstantiator(HandlerInstantiator handlerInstantiator) {
		this.builder.handlerInstantiator(handlerInstantiator);
	}

	/**
	 * Set the builder {@link ApplicationContext} in order to autowire Jackson handlers
	 * ({@link JsonSerializer}, {@link JsonDeserializer}, {@link KeyDeserializer},
	 * {@code TypeResolverBuilder} and {@code TypeIdResolver}).
	 * @see JsonRpcJackson2ObjectMapperBuilder#applicationContext(ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.builder.applicationContext(applicationContext);
	}


	@Override
	public void afterPropertiesSet() {
		if (this.objectMapper != null) {
			this.builder.configure(this.objectMapper);
		}
		else {
			this.objectMapper = this.builder.build();
		}
	}

	/**
	 * Return the singleton ObjectMapper.
	 */
	@Override
	@Nullable
	public ObjectMapper getObject() {
		return this.objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return (this.objectMapper != null ? this.objectMapper.getClass() : null);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
