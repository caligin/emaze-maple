package net.emaze.maple;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import net.emaze.dysfunctional.options.Maybe;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class Converters {

    private final Iterable<Converter> converters;
    private final Set<Class<?>> immutables;

    public Converters(Set<Class<?>> immutables, Iterable<Converter> converters) {
        this.immutables = immutables;
        this.converters = converters;
    }

    public Maybe<?> convert(ResolvableType sourceType, Object source, ResolvableType targetType) {
        for (Converter converter : converters) {
            if (converter.canConvert(this, sourceType, source, targetType)) {
                return converter.convert(this, sourceType, source, targetType);
            }
        }
        return Maybe.nothing();
    }

    public boolean isImmutable(Class<?> cls) {
        return cls.isPrimitive()
                || cls.isEnum()
                || cls == String.class
                || cls == Byte.class
                || cls == Character.class
                || cls == Short.class
                || cls == Integer.class
                || cls == Long.class
                || cls == Float.class
                || cls == Double.class
                || cls == BigInteger.class
                || cls == BigDecimal.class
                || immutables.contains(cls);
    }

}
