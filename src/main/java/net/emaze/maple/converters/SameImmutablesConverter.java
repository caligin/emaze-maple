package net.emaze.maple.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */

public class SameImmutablesConverter implements Converter {


    private Set<Class<?>> immutables = new HashSet<>();

    public SameImmutablesConverter(Set<Class<?>> immutables) {
        this.immutables = immutables;
    }

    @Override
    public Maybe<?> convert(Converters converters,ResolvableType sourceType, Object source, ResolvableType targetType) {
        return Maybe.just(source);
    }

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> targetClass = targetType.getRawClass();
        final Class<?> sourceClass = sourceType.getRawClass();
        return targetClass.isAssignableFrom(sourceClass) && isImmutable(targetClass);
    }

    private boolean isImmutable(Class<?> k) {
        return k.isPrimitive()
                || k.isEnum()
                || k == String.class
                || k == Byte.class
                || k == Character.class
                || k == Short.class
                || k == Integer.class
                || k == Long.class
                || k == Float.class
                || k == Double.class
                || k == BigInteger.class
                || k == BigDecimal.class
                || immutables.contains(k);
    }
}
