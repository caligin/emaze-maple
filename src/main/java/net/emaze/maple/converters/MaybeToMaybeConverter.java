package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class MaybeToMaybeConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        return sourceType.resolve() == Maybe.class && targetType.resolve() == Maybe.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final Maybe<?> m = (Maybe<?>) source;
        if (!m.hasValue()) {
            return Maybe.just(Maybe.nothing());
        }
        final MapleType elSourceType = sourceType.getGeneric(0);
        final MapleType elTargetType = targetType.getGeneric(0);
        final Maybe<?> maybeConverted = converters.convert(elSourceType, m.value(), elTargetType);
        return maybeConverted.hasValue() ? Maybe.just(maybeConverted) : Maybe.nothing();
    }
}
