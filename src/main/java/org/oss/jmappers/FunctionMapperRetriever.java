package org.oss.jmappers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

class FunctionMapperRetriever implements MapperRetriever {

    @Override
    public void addMappers(BiConsumer<MappingClasses, Function> consumer, Object mapper) {
        if (!(mapper instanceof Function)) {
            return;
        }
        Function function = (Function) mapper;
        MappingClasses mappingClasses = extractMappingMethod(function);
        consumer.accept(mappingClasses, function);
    }

    private MappingClasses extractMappingMethod(Function function) {
        return Arrays.stream(function.getClass().getMethods())
                .filter(this::isFunctionMethod)
                .map(MappingClasses::new)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private boolean isFunctionMethod(Method method) {
        return method.getName().equals("applyTo") && method.getParameterCount() == 1
                && !method.isSynthetic();
    }
}
