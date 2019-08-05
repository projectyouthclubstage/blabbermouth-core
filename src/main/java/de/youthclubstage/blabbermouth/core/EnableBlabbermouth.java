package de.youthclubstage.blabbermouth.core;

import de.youthclubstage.blabbermouth.core.CoreConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CoreConfiguration.class)
@Configuration
public @interface EnableBlabbermouth {
}
