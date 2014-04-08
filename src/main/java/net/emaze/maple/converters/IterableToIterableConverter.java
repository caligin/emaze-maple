package net.emaze.maple.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class IterableToIterableConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        return Iterable.class.isAssignableFrom(sourceType.resolve()) && Iterable.class.isAssignableFrom(targetType.resolve());
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final Class<?> sourceClass = sourceType.resolve();
        final Class<?> targetClass = targetType.resolve();
        final Maybe<Collection<Object>> maybeCollection = createCollection(targetClass);
        if (!maybeCollection.hasValue()) {
            return Maybe.nothing();
        }
        final Collection<Object> collection = maybeCollection.value();
        final MapleType targetElementType = targetType.getGeneric(0);
        final MapleType sourceElementType = sourceType.getGeneric(0);
        for (Object object : (Iterable<?>) source) {
            final Maybe<?> el = converters.convert(sourceElementType, object, targetElementType);
            if (!el.hasValue()) {
                return Maybe.nothing();
            }
            collection.add(el.value());
        }
        return Maybe.just(collection);
    }

    private static Maybe<Collection<Object>> createCollection(Class<?> targetClass) {
        if (Deque.class.isAssignableFrom(targetClass)) {
            return Maybe.<Collection<Object>>just(new LinkedList<>());
        }
        if (List.class.isAssignableFrom(targetClass)) {
            return Maybe.<Collection<Object>>just(new ArrayList<>());
        }
        if (SortedSet.class.isAssignableFrom(targetClass)) {
            return Maybe.<Collection<Object>>just(new TreeSet<>());
        }
        if (Set.class.isAssignableFrom(targetClass)) {
            return Maybe.<Collection<Object>>just(new HashSet<>());
        }
        //TODO: missing collections
        return Maybe.nothing();
    }

}
