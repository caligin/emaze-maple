package net.emaze.maple.beans;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Accessor;
import net.emaze.maple.Mutator;

/**
 *
 * @author rferranti
 */
public class CachingBeans implements Beans {

    private final Map<Class<?>, Map<String, Accessor>> accessorsCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<String, Mutator>> mutatorsCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, Maybe<Constructor>> constructorsCache = new ConcurrentHashMap<>();
    private final Beans inner;

    public CachingBeans(Beans inner) {
        this.inner = inner;
    }

    @Override
    public Map<String, Accessor> accessors(Class<?> cls) {
        if (accessorsCache.containsKey(cls)) {
            return accessorsCache.get(cls);
        }
        final Map<String, Accessor> accessors = inner.accessors(cls);
        accessorsCache.put(cls, accessors);
        return accessors;
    }

    @Override
    public Map<String, Mutator> mutators(Class<?> cls) {
        if (mutatorsCache.containsKey(cls)) {
            return mutatorsCache.get(cls);
        }
        final Map<String, Mutator> accessors = inner.mutators(cls);
        mutatorsCache.put(cls, accessors);
        return accessors;
    }

    @Override
    public Maybe<Constructor> constructor(Class<?> cls) {
        if (constructorsCache.containsKey(cls)) {
            return constructorsCache.get(cls);
        }
        final Maybe<Constructor> beanConstructor = inner.constructor(cls);
        constructorsCache.put(cls, beanConstructor);
        return beanConstructor;
    }
}
