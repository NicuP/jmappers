package org.oss.jmappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

@Component
public class MapperTemplate {

    @Autowired
    private ApplicationContext applicationContext;
    private final Map<MappingClasses, Function> mappers = new ConcurrentHashMap<>();
    private final List<MapperRetriever> mapperRetrievers = new CopyOnWriteArrayList<>();

    public MapperTemplate() {
        mapperRetrievers.add(new AnnotationMapperRetriever());
        mapperRetrievers.add(new FunctionMapperRetriever());
    }

    @SuppressWarnings("unchecked")
    public <T> T map(Object source, Class<?> destinationClass) {
        Class<?> sourceClass = source.getClass();
        MappingClasses mappingClasses = new MappingClasses(sourceClass, destinationClass);
        Function function = mappers.get(mappingClasses);
        if (function == null) {
            throw new IllegalArgumentException("Mapper from source class '" + sourceClass + "" +
                    "' to destination class '" + destinationClass + "' not found");
        }
        return (T) function.apply(source);
    }

    public <FROM, TO> void registerMapper(Class<FROM> from, Class<TO> to, Function<FROM, TO> function) {
        mappers.put(new MappingClasses(from, to), function);
    }

    public void unregisterMapper(Class<?> from, Class<?> to) {
        mappers.remove(new MappingClasses(from, to));
    }

    @SuppressWarnings("unchecked")
    public <FROM, TO> Optional<Function<FROM, TO>> getMapper(Class<FROM> from, Class<TO> to) {
        return Optional.ofNullable(mappers.get(new MappingClasses(from, to)));
    }

    @EventListener(ContextRefreshedEvent.class)
    void contextRefreshedEvent() {
        Map<String, Object> mappersByName = applicationContext.getBeansWithAnnotation(Mapper.class);
        populateMappers(mappersByName.values());
    }

    private void populateMappers(Collection<Object> mapperBeans) {
        for (Object mapperBean : mapperBeans) {
            int sizeBefore = mappers.size();
            for (MapperRetriever mapperRetriever : mapperRetrievers) {
                mapperRetriever.addMappers(mappers::put, mapperBean);
            }
            if (sizeBefore == mappers.size()) {
                throw new IllegalStateException("Unable to configure mapper; bean class '" + mapperBean.getClass() +
                        "' is annotated with '" + Mapper.class + "' but does not have neither @MappingMethod annotation" +
                        "nor implements Function interface");
            }
        }
    }



}
