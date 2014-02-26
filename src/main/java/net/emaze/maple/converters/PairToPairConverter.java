package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class PairToPairConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        return sourceType.resolve() == Pair.class && targetType.resolve() == Pair.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final MapleType fstSourceType = sourceType.getGeneric(0);
        final MapleType fstTargetType = targetType.getGeneric(0);
        final MapleType sndSourceType = sourceType.getGeneric(1);
        final MapleType sndTargetType = targetType.getGeneric(1);
        final Pair<?, ?> p = (Pair<?, ?>) source;
        final Maybe<?> fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Maybe<?> snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        if (fst.hasValue() && snd.hasValue()) {
            return Maybe.just(Pair.of(fst.value(), snd.value()));
        }
        return Maybe.nothing();
    }

}
