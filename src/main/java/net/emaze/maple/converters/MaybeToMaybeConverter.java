package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class MaybeToMaybeConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.resolve() == Maybe.class && targetType.resolve() == Maybe.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Maybe<?> m = (Maybe<?>) source;
        if (!m.isPresent()) {
            return Maybe.just(Maybe.nothing());
        }
        final ResolvableType elSourceType = sourceType.getGeneric(0);
        final ResolvableType elTargetType = targetType.getGeneric(0);
        final Maybe<?> maybeConverted = converters.convert(elSourceType, m.get(), elTargetType);
        return maybeConverted.isPresent()? Maybe.just(maybeConverted) : Maybe.nothing();
    }
}
