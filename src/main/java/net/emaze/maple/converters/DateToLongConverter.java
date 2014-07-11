package net.emaze.maple.converters;

import java.util.Date;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class DateToLongConverter implements Converter {

    private static final ResolvableType DATE_TYPE = ResolvableType.forClass(Date.class);

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> targetClass = targetType.resolve();
        return (targetClass == Long.class || targetClass == long.class) && DATE_TYPE.isAssignableFrom(sourceType);
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Long value = source == null ? (targetType.resolve() == long.class ? 0l : null) : ((Date) source).getTime();
        return Maybe.just(value);
    }

}
