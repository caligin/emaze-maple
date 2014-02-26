package net.emaze.maple.converters;



import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class ToIntConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        final Class<?> targetClass = targetType.resolve();
        return targetClass == Integer.class || targetClass == int.class;
    }

    @Override
    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType) {
        if(source instanceof String){
            return Maybe.just(Integer.parseInt(source.toString()));
        }
        if(source instanceof Number){
            return Maybe.just(((Number) source).intValue());
        }
        return Maybe.nothing();
    }

    
}
