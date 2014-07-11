package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class LongToDateConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> sourceClass = sourceType.resolve();
        final Class<?> targetClass = sourceType.resolve();
        return (sourceClass == Long.class || sourceClass == long.class) && (targetClass == java.util.Date.class || targetClass == java.sql.Date.class);
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        if (source == null) {
            return Maybe.just(null);
        }
        if (targetType.resolve() == java.util.Date.class) {
            return Maybe.just(new java.util.Date((Long) source));
        }
        return Maybe.just(new java.sql.Date((Long) source));
    }

}
