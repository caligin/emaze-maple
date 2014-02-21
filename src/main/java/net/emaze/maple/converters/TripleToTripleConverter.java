package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Triple;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class TripleToTripleConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.getRawClass() == Triple.class && targetType.getRawClass() == Triple.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final ResolvableType fstSourceType = sourceType.getGeneric(0);
        final ResolvableType fstTargetType = targetType.getGeneric(0);
        final ResolvableType sndSourceType = sourceType.getGeneric(1);
        final ResolvableType sndTargetType = targetType.getGeneric(1);
        final ResolvableType trdSourceType = sourceType.getGeneric(2);
        final ResolvableType trdTargetType = targetType.getGeneric(2);
        final Triple<?, ?, ?> p = (Triple<?, ?, ?>) source;
        final Object fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Object snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        final Object trd = converters.convert(trdSourceType, p.third(), trdTargetType);
        return Maybe.just(Triple.of(fst, snd, trd));
    }
}
