package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class SameImmutablesConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final Class<?> targetClass = targetType.resolve();
        final Class<?> sourceClass = sourceType.resolve();
        return targetClass.isAssignableFrom(sourceClass) && converters.isImmutable(targetClass);
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        return Maybe.just(source);
    }

}
