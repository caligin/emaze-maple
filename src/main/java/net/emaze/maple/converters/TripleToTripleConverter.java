package net.emaze.maple.converters;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Triple;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;


public class TripleToTripleConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return sourceType.resolve() == Triple.class && targetType.resolve() == Triple.class;
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
        final Maybe<?> fst = converters.convert(fstSourceType, p.first(), fstTargetType);
        final Maybe<?> snd = converters.convert(sndSourceType, p.second(), sndTargetType);
        final Maybe<?> trd = converters.convert(trdSourceType, p.third(), trdTargetType);
        if (fst.isPresent()&& snd.isPresent()&& trd.isPresent()) {
            return Maybe.just(Triple.of(fst.get(), snd.get(), trd.get()));
        }
        return Maybe.nothing();
    }
}
