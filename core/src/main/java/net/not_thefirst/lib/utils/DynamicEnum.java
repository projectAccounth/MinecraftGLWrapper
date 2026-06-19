package net.not_thefirst.lib.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * alternative enum impl
 */
public abstract class DynamicEnum<E extends DynamicEnum<E>> {
    
    private static final class Registry {
        private final List<Object> values = new ArrayList<>();
        private final Map<String, Object> byName = new ConcurrentHashMap<>();
    }

    private static final Map<Class<?>, Registry> MASTER_REGISTRY = new ConcurrentHashMap<>();

    private final String name;
    private final int ordinal;

    protected DynamicEnum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    /**
     * Internal factory engine to safely instantiate and track subclass elements.
     */
    protected static synchronized <T extends DynamicEnum<T>> T register(Class<T> clazz, String name, InstanceFactory<T> factory) {
        String upperName = name.toUpperCase().trim();
        Registry registry = MASTER_REGISTRY.computeIfAbsent(clazz, k -> new Registry());

        if (registry.byName.containsKey(upperName)) {
            return clazz.cast(registry.byName.get(upperName));
        }

        int nextOrdinal = registry.values.size();
        T newInstance = factory.create(upperName, nextOrdinal);
        
        registry.values.add(newInstance);
        registry.byName.put(upperName, newInstance);
        
        return newInstance;
    }

    /**
     * Functional interface passing creation capabilities to the base class.
     */
    protected interface InstanceFactory<T> {
        T create(String name, int ordinal);
    }

    public final String name() { return this.name; }
    public final int ordinal() { return this.ordinal; }

    @SuppressWarnings("unchecked")
    public static <T extends DynamicEnum<T>> List<T> values(Class<T> clazz) {
        Registry registry = MASTER_REGISTRY.get(clazz);
        if (registry == null) return Collections.emptyList();
        return (List<T>) (List<?>) Collections.unmodifiableList(registry.values);
    }

    public static <T extends DynamicEnum<T>> T valueOf(Class<T> clazz, String name) {
        Registry registry = MASTER_REGISTRY.get(clazz);
        if (registry == null) {
            throw new IllegalArgumentException("No registration registry found for " + clazz.getSimpleName());
        }
        Object instance = registry.byName.get(name.toUpperCase().trim());
        if (instance == null) {
            throw new IllegalArgumentException("No constant found for name: " + name);
        }
        return clazz.cast(instance);
    }

    @Override
    public final String toString() { return this.name; }

    @Override
    public final boolean equals(Object obj) { return this == obj; }

    @Override
    public final int hashCode() { return System.identityHashCode(this); }
}
