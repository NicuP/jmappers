package org.oss.jmappers;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

class AnnotationMapperRetriever implements MapperRetriever {
    @Override
    public void addMappers(BiConsumer<MappingClasses, Function> consumer, Object mapper) {
        for (Method method : mapper.getClass().getMethods()) {
            if (method.isAnnotationPresent(MappingMethod.class)) {
                MappingClasses mappingClasses = new MappingClasses(method);
                Function function = param -> ReflectionUtils.invokeMethod(method, mapper, param);
                consumer.accept(mappingClasses, function);
            }
        }
    }

}
