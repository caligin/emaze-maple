package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class PairToPairConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.getRawClass() == Pair.class && targetType.getRawClass() == Pair.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final ResolvableType fstSourceType = sourceType.getGeneric(0);
        final ResolvableType fstTargetType = targetType.getGeneric(0);
        final ResolvableType sndSourceType = sourceType.getGeneric(1);
        final ResolvableType sndTargetType = targetType.getGeneric(1);
        final Pair<?, ?> p = (Pair<?, ?>) source;
        final Object fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Object snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        return Maybe.just(Pair.of(fst, snd));
    }

}
