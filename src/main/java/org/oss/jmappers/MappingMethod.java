package org.oss.jmappers;

import java.lang.annotation.*;

/**
 * Annotation for functions that perform the mapping; should be placed on methods in classes
 * annotated with @Mapper
 *
 * The methods annotated with @MappingMethod should return something and only accept one parameter
 *
 * @see Mapper
 * @see MapperTemplate
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MappingMethod {
}
