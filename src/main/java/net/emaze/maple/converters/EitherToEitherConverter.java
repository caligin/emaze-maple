package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class EitherToEitherConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.resolve() == Either.class && targetType.resolve() == Either.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Either<?, ?> m = (Either<?, ?>) source;
        final boolean isRight = m.maybe().hasValue();
        final int genericIndex = isRight ? 1 : 0;
        final ResolvableType elSourceType = sourceType.getGeneric(genericIndex);
        final ResolvableType elTargetType = targetType.getGeneric(genericIndex);
        final Object el = isRight ? m.maybe().value() : m.flip().maybe().value();
        final Maybe<?> maybeConverted = converters.convert(elSourceType, el, elTargetType);
        if (!maybeConverted.hasValue()) {
            return Maybe.nothing();
        }
        final Object convertedEl = maybeConverted.value();
        final Either<?, ?> target = isRight ? Either.right(convertedEl) : Either.left(convertedEl);
        return Maybe.just(target);
    }
}
