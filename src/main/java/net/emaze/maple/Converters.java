package net.emaze.maple;

import net.emaze.dysfunctional.options.Maybe;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class Converters {

    private final Iterable<Converter> converters;

    public Converters(Iterable<Converter> converters) {
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

}
