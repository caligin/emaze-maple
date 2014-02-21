package net.emaze.maple.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class IterableToIterableConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return Iterable.class.isAssignableFrom(sourceType.resolve()) && Iterable.class.isAssignableFrom(targetType.resolve());
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> sourceClass = sourceType.resolve();
        final Class<?> targetClass = targetType.resolve();
        final Maybe<Collection<Object>> maybeCollection = createCollection(targetClass);
        if (!maybeCollection.hasValue()) {
            return Maybe.nothing();
        }
        final Collection<Object> collection = maybeCollection.value();
        final ResolvableType targetElementType = targetType.getGeneric(0);
        final ResolvableType sourceElementType = sourceType.getGeneric(0);
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
        if (List.class.isAssignableFrom(targetClass)) {
            return Maybe.<Collection<Object>>just(new ArrayList<>());
        }
        //TODO: set, queue, linkedlist ecc
        return Maybe.nothing();
    }

}
