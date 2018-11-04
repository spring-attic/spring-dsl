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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
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
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;

/**
 * A builder used to create {@link ObjectMapper} instances with a fluent API.
 *
 * <p>It customizes Jackson's default properties with the following ones:
 * <ul>
 * <li>{@link MapperFeature#DEFAULT_VIEW_INCLUSION} is disabled</li>
 * <li>{@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} is disabled</li>
 * </ul>
 *
 * <p>It also automatically registers the following well-known modules if they are
 * detected on the classpath:
 * <ul>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-jdk8">jackson-datatype-jdk8</a>:
 * support for other Java 8 types like {@link java.util.Optional}</li>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-jsr310">jackson-datatype-jsr310</a>:
 * support for Java 8 Date & Time API types</li>
 * <li><a href="https://github.com/FasterXML/jackson-datatype-joda">jackson-datatype-joda</a>:
 * support for Joda-Time types</li>
 * </ul>
 *
 * <p>Compatible with Jackson 2.6 and higher, as of Spring 4.3.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @author Tadaya Tsuyukubo
 * @author Eddú Meléndez
 * @author Janne Valkealahti
 * @author Janne Valkealahti
 * @see #build()
 * @see #configure(ObjectMapper)
 * @see JsonRpcJackson2ObjectMapperFactoryBean
 */
public class JsonRpcJackson2ObjectMapperBuilder {

	private final Map<Class<?>, Class<?>> mixIns = new HashMap<>();

	private final Map<Class<?>, JsonSerializer<?>> serializers = new LinkedHashMap<>();

	private final Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();

	private final Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilities = new HashMap<>();

	private final Map<Object, Boolean> features = new HashMap<>();

	@Nullable
	private JsonFactory factory;

	@Nullable
	private DateFormat dateFormat;

	@Nullable
	private Locale locale;

	@Nullable
	private TimeZone timeZone;

	@Nullable
	private AnnotationIntrospector annotationIntrospector;

	@Nullable
	private PropertyNamingStrategy propertyNamingStrategy;

	@Nullable
	private TypeResolverBuilder<?> defaultTyping;

	@Nullable
	private JsonInclude.Include serializationInclusion;

	@Nullable
	private FilterProvider filters;

	@Nullable
	private List<Module> modules;

	@Nullable
	private Class<? extends Module>[] moduleClasses;

	private boolean findModulesViaServiceLoader = false;

	private boolean findWellKnownModules = true;

	private ClassLoader moduleClassLoader = getClass().getClassLoader();

	@Nullable
	private HandlerInstantiator handlerInstantiator;

	@Nullable
	private ApplicationContext applicationContext;

	/**
	 * Define the {@link JsonFactory} to be used to create the {@link ObjectMapper}
	 * instance.
	 *
	 * @param factory the factory
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder factory(JsonFactory factory) {
		this.factory = factory;
		return this;
	}

	/**
	 * Define the format for date/time with the given {@link DateFormat}.
	 * <p>Note: Setting this property makes the exposed {@link ObjectMapper}
	 * non-thread-safe, according to Jackson's thread safety rules.
	 *
	 * @param dateFormat the date format
	 * @return the jackson 2 object mapper builder
	 * @see #simpleDateFormat(String)
	 */
	public JsonRpcJackson2ObjectMapperBuilder dateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	/**
	 * Define the date/time format with a {@link SimpleDateFormat}.
	 * <p>Note: Setting this property makes the exposed {@link ObjectMapper}
	 * non-thread-safe, according to Jackson's thread safety rules.
	 *
	 * @param format the format
	 * @return the jackson 2 object mapper builder
	 * @see #dateFormat(DateFormat)
	 */
	public JsonRpcJackson2ObjectMapperBuilder simpleDateFormat(String format) {
		this.dateFormat = new SimpleDateFormat(format);
		return this;
	}

	/**
	 * Override the default {@link Locale} to use for formatting.
	 * Default value used is {@link Locale#getDefault()}.
	 *
	 * @param locale the locale
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder locale(Locale locale) {
		this.locale = locale;
		return this;
	}

	/**
	 * Override the default {@link Locale} to use for formatting.
	 * Default value used is {@link Locale#getDefault()}.
	 *
	 * @param localeString the locale ID as a String representation
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder locale(String localeString) {
		this.locale = StringUtils.parseLocale(localeString);
		return this;
	}

	/**
	 * Override the default {@link TimeZone} to use for formatting.
	 * Default value used is UTC (NOT local timezone).
	 *
	 * @param timeZone the time zone
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder timeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		return this;
	}

	/**
	 * Override the default {@link TimeZone} to use for formatting.
	 * Default value used is UTC (NOT local timezone).
	 *
	 * @param timeZoneString the zone ID as a String representation
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder timeZone(String timeZoneString) {
		this.timeZone = StringUtils.parseTimeZoneString(timeZoneString);
		return this;
	}

	/**
	 * Set an {@link AnnotationIntrospector} for both serialization and deserialization.
	 *
	 * @param annotationIntrospector the annotation introspector
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder annotationIntrospector(AnnotationIntrospector annotationIntrospector) {
		this.annotationIntrospector = annotationIntrospector;
		return this;
	}

	/**
	 * Specify a {@link com.fasterxml.jackson.databind.PropertyNamingStrategy} to
	 * configure the {@link ObjectMapper} with.
	 *
	 * @param propertyNamingStrategy the property naming strategy
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder propertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
		this.propertyNamingStrategy = propertyNamingStrategy;
		return this;
	}

	/**
	 * Specify a {@link TypeResolverBuilder} to use for Jackson's default typing.
	 *
	 * @param typeResolverBuilder the type resolver builder
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder defaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
		this.defaultTyping = typeResolverBuilder;
		return this;
	}

	/**
	 * Set a custom inclusion strategy for serialization.
	 * @see com.fasterxml.jackson.annotation.JsonInclude.Include
	 */
	public JsonRpcJackson2ObjectMapperBuilder serializationInclusion(JsonInclude.Include serializationInclusion) {
		this.serializationInclusion = serializationInclusion;
		return this;
	}

	/**
	 * Set the global filters to use in order to support {@link JsonFilter @JsonFilter} annotated POJO.
	 *
	 * @param filters the filters
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder filters(FilterProvider filters) {
		this.filters = filters;
		return this;
	}

	/**
	 * Add mix-in annotations to use for augmenting specified class or interface.
	 *
	 * @param target class (or interface) whose annotations to effectively override
	 * @param mixinSource class (or interface) whose annotations are to be "added"
	 * to target's annotations as value
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.databind.ObjectMapper#addMixInAnnotations(Class, Class)
	 */
	public JsonRpcJackson2ObjectMapperBuilder mixIn(Class<?> target, Class<?> mixinSource) {
		this.mixIns.put(target, mixinSource);
		return this;
	}

	/**
	 * Add mix-in annotations to use for augmenting specified class or interface.
	 *
	 * @param mixIns a Map of entries with target classes (or interface) whose annotations
	 * to effectively override as key and mix-in classes (or interface) whose
	 * annotations are to be "added" to target's annotations as value.
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.databind.ObjectMapper#addMixInAnnotations(Class, Class)
	 */
	public JsonRpcJackson2ObjectMapperBuilder mixIns(Map<Class<?>, Class<?>> mixIns) {
		this.mixIns.putAll(mixIns);
		return this;
	}

	/**
	 * Configure custom serializers. Each serializer is registered for the type
	 * returned by {@link JsonSerializer#handledType()}, which must not be {@code null}.
	 *
	 * @param serializers the serializers
	 * @return the jackson 2 object mapper builder
	 * @see #serializersByType(Map)
	 */
	public JsonRpcJackson2ObjectMapperBuilder serializers(JsonSerializer<?>... serializers) {
		for (JsonSerializer<?> serializer : serializers) {
			Class<?> handledType = serializer.handledType();
			if (handledType == null || handledType == Object.class) {
				throw new IllegalArgumentException("Unknown handled type in " + serializer.getClass().getName());
			}
			this.serializers.put(serializer.handledType(), serializer);
		}
		return this;
	}

	/**
	 * Configure a custom serializer for the given type.
	 *
	 * @param type the type
	 * @param serializer the serializer
	 * @return the jackson 2 object mapper builder
	 * @see #serializers(JsonSerializer...)
	 */
	public JsonRpcJackson2ObjectMapperBuilder serializerByType(Class<?> type, JsonSerializer<?> serializer) {
		this.serializers.put(type, serializer);
		return this;
	}

	/**
	 * Configure custom serializers for the given types.
	 *
	 * @param serializers the serializers
	 * @return the jackson 2 object mapper builder
	 * @see #serializers(JsonSerializer...)
	 */
	public JsonRpcJackson2ObjectMapperBuilder serializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
		this.serializers.putAll(serializers);
		return this;
	}

	/**
	 * Configure custom deserializers. Each deserializer is registered for the type
	 * returned by {@link JsonDeserializer#handledType()}, which must not be {@code null}.
	 *
	 * @param deserializers the deserializers
	 * @return the jackson 2 object mapper builder
	 * @see #deserializersByType(Map)
	 */
	public JsonRpcJackson2ObjectMapperBuilder deserializers(JsonDeserializer<?>... deserializers) {
		for (JsonDeserializer<?> deserializer : deserializers) {
			Class<?> handledType = deserializer.handledType();
			if (handledType == null || handledType == Object.class) {
				throw new IllegalArgumentException("Unknown handled type in " + deserializer.getClass().getName());
			}
			this.deserializers.put(deserializer.handledType(), deserializer);
		}
		return this;
	}

	/**
	 * Configure a custom deserializer for the given type.
	 *
	 * @param type the type
	 * @param deserializer the deserializer
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder deserializerByType(Class<?> type, JsonDeserializer<?> deserializer) {
		this.deserializers.put(type, deserializer);
		return this;
	}

	/**
	 * Configure custom deserializers for the given types.
	 *
	 * @param deserializers the deserializers
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder deserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
		this.deserializers.putAll(deserializers);
		return this;
	}

	/**
	 * Shortcut for {@link MapperFeature#AUTO_DETECT_FIELDS} option.
	 *
	 * @param autoDetectFields the auto detect fields
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder autoDetectFields(boolean autoDetectFields) {
		this.features.put(MapperFeature.AUTO_DETECT_FIELDS, autoDetectFields);
		return this;
	}

	/**
	 * Shortcut for {@link MapperFeature#AUTO_DETECT_SETTERS}/
	 * {@link MapperFeature#AUTO_DETECT_GETTERS}/{@link MapperFeature#AUTO_DETECT_IS_GETTERS}
	 * options.
	 *
	 * @param autoDetectGettersSetters the auto detect getters setters
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder autoDetectGettersSetters(boolean autoDetectGettersSetters) {
		this.features.put(MapperFeature.AUTO_DETECT_GETTERS, autoDetectGettersSetters);
		this.features.put(MapperFeature.AUTO_DETECT_SETTERS, autoDetectGettersSetters);
		this.features.put(MapperFeature.AUTO_DETECT_IS_GETTERS, autoDetectGettersSetters);
		return this;
	}

	/**
	 * Shortcut for {@link MapperFeature#DEFAULT_VIEW_INCLUSION} option.
	 *
	 * @param defaultViewInclusion the default view inclusion
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder defaultViewInclusion(boolean defaultViewInclusion) {
		this.features.put(MapperFeature.DEFAULT_VIEW_INCLUSION, defaultViewInclusion);
		return this;
	}

	/**
	 * Shortcut for {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} option.
	 *
	 * @param failOnUnknownProperties the fail on unknown properties
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder failOnUnknownProperties(boolean failOnUnknownProperties) {
		this.features.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		return this;
	}

	/**
	 * Shortcut for {@link SerializationFeature#FAIL_ON_EMPTY_BEANS} option.
	 *
	 * @param failOnEmptyBeans the fail on empty beans
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder failOnEmptyBeans(boolean failOnEmptyBeans) {
		this.features.put(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans);
		return this;
	}

	/**
	 * Shortcut for {@link SerializationFeature#INDENT_OUTPUT} option.
	 *
	 * @param indentOutput the indent output
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder indentOutput(boolean indentOutput) {
		this.features.put(SerializationFeature.INDENT_OUTPUT, indentOutput);
		return this;
	}

	/**
	 * Specify visibility to limit what kind of properties are auto-detected.
	 *
	 * @param accessor the accessor
	 * @param visibility the visibility
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.annotation.PropertyAccessor
	 * @see com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
	 */
	public JsonRpcJackson2ObjectMapperBuilder visibility(PropertyAccessor accessor, JsonAutoDetect.Visibility visibility) {
		this.visibilities.put(accessor, visibility);
		return this;
	}

	/**
	 * Specify features to enable.
	 *
	 * @param featuresToEnable the features to enable
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.core.JsonParser.Feature
	 * @see com.fasterxml.jackson.core.JsonGenerator.Feature
	 * @see com.fasterxml.jackson.databind.SerializationFeature
	 * @see com.fasterxml.jackson.databind.DeserializationFeature
	 * @see com.fasterxml.jackson.databind.MapperFeature
	 */
	public JsonRpcJackson2ObjectMapperBuilder featuresToEnable(Object... featuresToEnable) {
		for (Object feature : featuresToEnable) {
			this.features.put(feature, Boolean.TRUE);
		}
		return this;
	}

	/**
	 * Specify features to disable.
	 *
	 * @param featuresToDisable the features to disable
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.core.JsonParser.Feature
	 * @see com.fasterxml.jackson.core.JsonGenerator.Feature
	 * @see com.fasterxml.jackson.databind.SerializationFeature
	 * @see com.fasterxml.jackson.databind.DeserializationFeature
	 * @see com.fasterxml.jackson.databind.MapperFeature
	 */
	public JsonRpcJackson2ObjectMapperBuilder featuresToDisable(Object... featuresToDisable) {
		for (Object feature : featuresToDisable) {
			this.features.put(feature, Boolean.FALSE);
		}
		return this;
	}

	/**
	 * Specify one or more modules to be registered with the {@link ObjectMapper}.
	 * <p>Note: If this is set, no finding of modules is going to happen - not by
	 * Jackson, and not by Spring either (see {@link #findModulesViaServiceLoader}).
	 * As a consequence, specifying an empty list here will suppress any kind of
	 * module detection.
	 * <p>Specify either this or {@link #modulesToInstall}, not both.
	 *
	 * @param modules the modules
	 * @return the jackson 2 object mapper builder
	 * @see #modules(List)
	 * @see com.fasterxml.jackson.databind.Module
	 */
	public JsonRpcJackson2ObjectMapperBuilder modules(Module... modules) {
		return modules(Arrays.asList(modules));
	}

	/**
	 * Set a complete list of modules to be registered with the {@link ObjectMapper}.
	 * <p>Note: If this is set, no finding of modules is going to happen - not by
	 * Jackson, and not by Spring either (see {@link #findModulesViaServiceLoader}).
	 * As a consequence, specifying an empty list here will suppress any kind of
	 * module detection.
	 * <p>Specify either this or {@link #modulesToInstall}, not both.
	 *
	 * @param modules the modules
	 * @return the jackson 2 object mapper builder
	 * @see #modules(Module...)
	 * @see com.fasterxml.jackson.databind.Module
	 */
	public JsonRpcJackson2ObjectMapperBuilder modules(List<Module> modules) {
		this.modules = new LinkedList<>(modules);
		this.findModulesViaServiceLoader = false;
		this.findWellKnownModules = false;
		return this;
	}

	/**
	 * Specify one or more modules to be registered with the {@link ObjectMapper}.
	 * <p>Modules specified here will be registered after
	 * Spring's autodetection of JSR-310 and Joda-Time, or Jackson's
	 * finding of modules (see {@link #findModulesViaServiceLoader}),
	 * allowing to eventually override their configuration.
	 * <p>Specify either this or {@link #modules}, not both.
	 *
	 * @param modules the modules
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.databind.Module
	 */
	public JsonRpcJackson2ObjectMapperBuilder modulesToInstall(Module... modules) {
		this.modules = Arrays.asList(modules);
		this.findWellKnownModules = true;
		return this;
	}

	/**
	 * Specify one or more modules by class to be registered with
	 * the {@link ObjectMapper}.
	 * <p>Modules specified here will be registered after
	 * Spring's autodetection of JSR-310 and Joda-Time, or Jackson's
	 * finding of modules (see {@link #findModulesViaServiceLoader}),
	 * allowing to eventually override their configuration.
	 * <p>Specify either this or {@link #modules}, not both.
	 *
	 * @param modules the modules
	 * @return the jackson 2 object mapper builder
	 * @see #modulesToInstall(Module...)
	 * @see com.fasterxml.jackson.databind.Module
	 */
	@SuppressWarnings("unchecked")
	public JsonRpcJackson2ObjectMapperBuilder modulesToInstall(Class<? extends Module>... modules) {
		this.moduleClasses = modules;
		this.findWellKnownModules = true;
		return this;
	}

	/**
	 * Set whether to let Jackson find available modules via the JDK ServiceLoader,
	 * based on META-INF metadata in the classpath.
	 * <p>If this mode is not set, Spring's Jackson2ObjectMapperBuilder itself
	 * will try to find the JSR-310 and Joda-Time support modules on the classpath -
	 * provided that Java 8 and Joda-Time themselves are available, respectively.
	 *
	 * @param findModules the find modules
	 * @return the jackson 2 object mapper builder
	 * @see com.fasterxml.jackson.databind.ObjectMapper#findModules()
	 */
	public JsonRpcJackson2ObjectMapperBuilder findModulesViaServiceLoader(boolean findModules) {
		this.findModulesViaServiceLoader = findModules;
		return this;
	}

	/**
	 * Set the ClassLoader to use for loading Jackson extension modules.
	 *
	 * @param moduleClassLoader the module class loader
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder moduleClassLoader(ClassLoader moduleClassLoader) {
		this.moduleClassLoader = moduleClassLoader;
		return this;
	}

	/**
	 * Customize the construction of Jackson handlers ({@link JsonSerializer}, {@link JsonDeserializer},
	 * {@link KeyDeserializer}, {@code TypeResolverBuilder} and {@code TypeIdResolver}).
	 *
	 * @param handlerInstantiator the handler instantiator
	 * @return the jackson 2 object mapper builder
	 * @see JsonRpcJackson2ObjectMapperBuilder#applicationContext(ApplicationContext)
	 */
	public JsonRpcJackson2ObjectMapperBuilder handlerInstantiator(HandlerInstantiator handlerInstantiator) {
		this.handlerInstantiator = handlerInstantiator;
		return this;
	}

	/**
	 * Set the Spring {@link ApplicationContext} in order to autowire Jackson handlers ({@link JsonSerializer},
	 * {@link JsonDeserializer}, {@link KeyDeserializer}, {@code TypeResolverBuilder} and {@code TypeIdResolver}).
	 *
	 * @param applicationContext the application context
	 * @return the jackson 2 object mapper builder
	 */
	public JsonRpcJackson2ObjectMapperBuilder applicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		return this;
	}


	/**
	 * Build a new {@link ObjectMapper} instance.
	 * <p>Each build operation produces an independent {@link ObjectMapper} instance.
	 * The builder's settings can get modified, with a subsequent build operation
	 * then producing a new {@link ObjectMapper} based on the most recent settings.
	 *
	 * @return the newly built ObjectMapper
	 */
	@SuppressWarnings("unchecked")
	public <T extends ObjectMapper> T build() {
		ObjectMapper mapper = (this.factory != null ? new ObjectMapper(this.factory) : new ObjectMapper());
		configure(mapper);
		return (T) mapper;
	}

	/**
	 * Configure an existing {@link ObjectMapper} instance with this builder's
	 * settings. This can be applied to any number of {@code ObjectMappers}.
	 *
	 * @param objectMapper the ObjectMapper to configure
	 */
	public void configure(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");

		if (this.findModulesViaServiceLoader) {
			objectMapper.registerModules(ObjectMapper.findModules(this.moduleClassLoader));
		}
		else if (this.findWellKnownModules) {
			registerWellKnownModulesIfAvailable(objectMapper);
		}

		if (this.modules != null) {
			objectMapper.registerModules(this.modules);
		}
		if (this.moduleClasses != null) {
			for (Class<? extends Module> module : this.moduleClasses) {
				objectMapper.registerModule(BeanUtils.instantiateClass(module));
			}
		}

		if (this.dateFormat != null) {
			objectMapper.setDateFormat(this.dateFormat);
		}
		if (this.locale != null) {
			objectMapper.setLocale(this.locale);
		}
		if (this.timeZone != null) {
			objectMapper.setTimeZone(this.timeZone);
		}

		if (this.annotationIntrospector != null) {
			objectMapper.setAnnotationIntrospector(this.annotationIntrospector);
		}
		if (this.propertyNamingStrategy != null) {
			objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
		}
		if (this.defaultTyping != null) {
			objectMapper.setDefaultTyping(this.defaultTyping);
		}
		if (this.serializationInclusion != null) {
			objectMapper.setSerializationInclusion(this.serializationInclusion);
		}

		if (this.filters != null) {
			objectMapper.setFilterProvider(this.filters);
		}

		this.mixIns.forEach(objectMapper::addMixIn);

		if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
			SimpleModule module = new SimpleModule();
			addSerializers(module);
			addDeserializers(module);
			objectMapper.registerModule(module);
		}

		this.visibilities.forEach(objectMapper::setVisibility);

		customizeDefaultFeatures(objectMapper);
		this.features.forEach((feature, enabled) -> configureFeature(objectMapper, feature, enabled));

		if (this.handlerInstantiator != null) {
			objectMapper.setHandlerInstantiator(this.handlerInstantiator);
		}
	}

	private void customizeDefaultFeatures(ObjectMapper objectMapper) {
		if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
			configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		}
		if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
			configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void addSerializers(SimpleModule module) {
		this.serializers.forEach((type, serializer) ->
				module.addSerializer((Class<? extends T>) type, (JsonSerializer<T>) serializer));
	}

	@SuppressWarnings("unchecked")
	private <T> void addDeserializers(SimpleModule module) {
		this.deserializers.forEach((type, deserializer) ->
				module.addDeserializer((Class<T>) type, (JsonDeserializer<? extends T>) deserializer));
	}

	private void configureFeature(ObjectMapper objectMapper, Object feature, boolean enabled) {
		if (feature instanceof JsonParser.Feature) {
			objectMapper.configure((JsonParser.Feature) feature, enabled);
		}
		else if (feature instanceof JsonGenerator.Feature) {
			objectMapper.configure((JsonGenerator.Feature) feature, enabled);
		}
		else if (feature instanceof SerializationFeature) {
			objectMapper.configure((SerializationFeature) feature, enabled);
		}
		else if (feature instanceof DeserializationFeature) {
			objectMapper.configure((DeserializationFeature) feature, enabled);
		}
		else if (feature instanceof MapperFeature) {
			objectMapper.configure((MapperFeature) feature, enabled);
		}
		else {
			throw new FatalBeanException("Unknown feature class: " + feature.getClass().getName());
		}
	}

	@SuppressWarnings("unchecked")
	private void registerWellKnownModulesIfAvailable(ObjectMapper objectMapper) {
		try {
			Class<? extends Module> jdk8Module = (Class<? extends Module>)
					ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", this.moduleClassLoader);
			objectMapper.registerModule(BeanUtils.instantiateClass(jdk8Module));
		}
		catch (ClassNotFoundException ex) {
			// jackson-datatype-jdk8 not available
		}

		try {
			Class<? extends Module> javaTimeModule = (Class<? extends Module>)
					ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", this.moduleClassLoader);
			objectMapper.registerModule(BeanUtils.instantiateClass(javaTimeModule));
		}
		catch (ClassNotFoundException ex) {
			// jackson-datatype-jsr310 not available
		}

		// Joda-Time present?
		if (ClassUtils.isPresent("org.joda.time.LocalDate", this.moduleClassLoader)) {
			try {
				Class<? extends Module> jodaModule = (Class<? extends Module>)
						ClassUtils.forName("com.fasterxml.jackson.datatype.joda.JodaModule", this.moduleClassLoader);
				objectMapper.registerModule(BeanUtils.instantiateClass(jodaModule));
			}
			catch (ClassNotFoundException ex) {
				// jackson-datatype-joda not available
			}
		}
	}

	/**
	 * Obtain a {@link JsonRpcJackson2ObjectMapperBuilder} instance in order to
	 * build a regular JSON {@link ObjectMapper} instance.
	 *
	 * @return the jackson 2 object mapper builder
	 */
	public static JsonRpcJackson2ObjectMapperBuilder json() {
		return new JsonRpcJackson2ObjectMapperBuilder();
	}
}
