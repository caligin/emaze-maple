package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Triple;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class TripleToTripleConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        return sourceType.resolve() == Triple.class && targetType.resolve() == Triple.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final MapleType fstSourceType = sourceType.getGeneric(0);
        final MapleType fstTargetType = targetType.getGeneric(0);
        final MapleType sndSourceType = sourceType.getGeneric(1);
        final MapleType sndTargetType = targetType.getGeneric(1);
        final MapleType trdSourceType = sourceType.getGeneric(2);
        final MapleType trdTargetType = targetType.getGeneric(2);
        final Triple<?, ?, ?> p = (Triple<?, ?, ?>) source;
        final Maybe<?> fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Maybe<?> snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        final Maybe<?> trd = converters.convert(trdSourceType, p.third(), trdTargetType);
        if (fst.hasValue() && snd.hasValue() && trd.hasValue()) {
            return Maybe.just(Triple.of(fst.value(), snd.value(), trd.value()));
        }
        return Maybe.nothing();
    }
}
