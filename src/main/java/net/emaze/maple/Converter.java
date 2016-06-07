package net.emaze.maple;

import net.emaze.dysfunctional.options.Maybe;
import org.springframework.core.ResolvableType;


public interface Converter {

    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType);

    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType);
}
