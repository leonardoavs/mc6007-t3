package com.mc6007.t1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Mc 6007 T 1.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
