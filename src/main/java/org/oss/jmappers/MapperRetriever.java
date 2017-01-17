package org.oss.jmappers;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface MapperRetriever {
    void addMappers(BiConsumer<MappingClasses, Function> consumer, Object mapper);
}
