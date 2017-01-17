package org.oss.jmappers;

import java.lang.reflect.Method;
import java.util.Objects;

class MappingClasses {
    Class<?> from;
    Class<?> to;

    public MappingClasses(Class<?> from, Class<?> to) {
        this.from = from;
        this.to = to;
    }

    public MappingClasses(Method method) {
        this(method.getParameterTypes()[0], method.getReturnType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingClasses that = (MappingClasses) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
