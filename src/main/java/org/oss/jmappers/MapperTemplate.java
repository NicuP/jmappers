package org.oss.jmappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class MapperTemplate {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<MappingClasses, Function> mappers = new ConcurrentHashMap<MappingClasses, Function>();

    public <T> T map(Object source, Class<?> destinationType) {
        return null;
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

        }
    }



}
