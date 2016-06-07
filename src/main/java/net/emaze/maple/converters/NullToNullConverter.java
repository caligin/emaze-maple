package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class NullToNullConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return source == null;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return targetType.resolve().isPrimitive() ? Maybe.nothing() : Maybe.just(null);
    }

}
