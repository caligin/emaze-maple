package net.emaze.maple.converters;



import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class ToLongConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Class<?> targetClass = targetType.getRawClass();
        return targetClass == Long.class || targetClass == long.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        if(source instanceof String){
            return Maybe.just(Long.parseLong(source.toString()));
        }
        if(source instanceof Number){
            return Maybe.just(((Number) source).longValue());
        }
        return Maybe.nothing();
    }

    
}
