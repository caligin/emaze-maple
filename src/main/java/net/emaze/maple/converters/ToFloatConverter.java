package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class ToFloatConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> targetClass = targetType.resolve();
        return targetClass == Float.class || targetClass == float.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        if (source instanceof CharSequence) {
            return Maybe.just(Float.parseFloat(source.toString()));
        }
        if (source instanceof Number) {
            return Maybe.just(((Number) source).floatValue());
        }
        return Maybe.nothing();
    }

}
