package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class SameImmutablesConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> targetClass = targetType.resolve();
        final Class<?> sourceClass = sourceType.resolve();
        return targetClass.isAssignableFrom(sourceClass) && converters.isImmutable(targetClass);
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return Maybe.just(source);
    }

}
