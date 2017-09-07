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

/**
 * Generic class with the purpose of decoupling mapping between DTOs and entities (or any generic
 * POJOs). The mappers can be configured automatically, using Spring or manually by calling this class.
 *
 * Mapper classes should be annotated with @Mapper, so they are picked up by Spring.
 *
 * The client classes should only call map method from this class, and shouldn't be aware of the mappers themselves.
 *
 * @see Mapper
 * @see MappingMethod
 */
@Component
public class MapperTemplate {

    private ApplicationContext applicationContext;
    private final Map<MappingClasses, Function> mappers = new ConcurrentHashMap<>();
    private final List<MapperRetriever> mapperRetrievers = new CopyOnWriteArrayList<>();

    @Autowired
    public MapperTemplate(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        mapperRetrievers.add(new AnnotationMapperRetriever());
        mapperRetrievers.add(new FunctionMapperRetriever());
    }

    /**
     * Maps the source into the specified destination class, provided there is a mapper defined
     * from source to destination
     *
     * @param source the source to be mapped
     * @param destinationClass the type of the bean to be mapped to
     * @return the mapped object
     */
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

    /**
     * Manually register a new mapper; to be used only when Spring not available
     *
     * @param from the type of the origin class
     * @param to the type of the destination class
     * @param function the mapping function from FROM to TO
     */
    public <FROM, TO> void registerMapper(Class<FROM> from, Class<TO> to, Function<FROM, TO> function) {
        mappers.put(new MappingClasses(from, to), function);
    }

    /**
     * Unregisters the mapper for the specified classes
     */
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
