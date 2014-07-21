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
        return accessorsCache.computeIfAbsent(cls, inner::accessors);
    }

    @Override
    public Map<String, Mutator> mutators(Class<?> cls) {
        return mutatorsCache.computeIfAbsent(cls, inner::mutators);
    }

    @Override
    public Maybe<Constructor> constructor(Class<?> cls) {
        return constructorsCache.computeIfAbsent(cls, inner::constructor);
    }
}
