package org.springframework.dsl.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dsl.lsp.server.config.DslProperties;

/**
 * {@link ConfigurationProperties} for settings under {@code spring.dsl}.
 *
 * @author Janne Valkealahti
 *
 */
@ConfigurationProperties(prefix = "spring.dsl")
public class DslConfigurationProperties extends DslProperties {
}
