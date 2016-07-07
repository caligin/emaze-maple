package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class PairToPairConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.resolve() == Pair.class && targetType.resolve() == Pair.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final ResolvableType fstSourceType = sourceType.getGeneric(0);
        final ResolvableType fstTargetType = targetType.getGeneric(0);
        final ResolvableType sndSourceType = sourceType.getGeneric(1);
        final ResolvableType sndTargetType = targetType.getGeneric(1);
        final Pair<?, ?> p = (Pair<?, ?>) source;
        final Maybe<?> fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Maybe<?> snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        if (fst.isPresent()&& snd.isPresent()) {
            return Maybe.just(Pair.of(fst.get(), snd.get()));
        }
        return Maybe.nothing();
    }

}
