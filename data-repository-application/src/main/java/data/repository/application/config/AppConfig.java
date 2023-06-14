package data.repository.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type App config.
 */
@Configuration
@EnableJpaRepositories(basePackages = "data.repository.application.repository")
public class AppConfig {
}
