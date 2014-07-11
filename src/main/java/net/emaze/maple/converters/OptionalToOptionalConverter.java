package net.emaze.maple.converters;

import java.util.Optional;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class OptionalToOptionalConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.resolve() == Optional.class && targetType.resolve() == Optional.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Optional<?> m = (Optional<?>) source;
        if (!m.isPresent()) {
            return Maybe.just(Optional.empty());
        }
        final ResolvableType elSourceType = sourceType.getGeneric(0);
        final ResolvableType elTargetType = targetType.getGeneric(0);
        final Maybe<?> maybeConverted = converters.convert(elSourceType, m.get(), elTargetType);
        return maybeConverted.hasValue() ? Maybe.just(Optional.of(maybeConverted.value())) : Maybe.nothing();
    }
}
