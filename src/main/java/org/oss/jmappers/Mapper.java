package org.oss.jmappers;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotation for a mapper class; a mapper class is a class that has at least one mapping method.
 *
 * A mapping method is either apply(), from java.util.Function (meaning the mapper must implement
 * Function interface) or is annotated with @MappingMethod.
 *
 * A mapper is automatically a Spring Bean (provided the package is configured to be searched by Spring)
 *
 * @see MappingMethod
 * @see MapperTemplate
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Mapper {
}
