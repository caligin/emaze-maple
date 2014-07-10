package net.emaze.maple.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class MapToMapConverter implements Converter {

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return Map.class.isAssignableFrom(targetType.resolve()) && source instanceof Map;
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        final Map<Object, Object> r = createMap(targetType.getRawClass());
        final ResolvableType sourceKeyType = sourceType.getGeneric(0);
        final ResolvableType sourceValueType = sourceType.getGeneric(1);
        final ResolvableType targetKeyType = targetType.getGeneric(0);
        final ResolvableType targetValueType = targetType.getGeneric(1);
        for (Map.Entry<Object, Object> sourceEntry : ((Map<Object, Object>) source).entrySet()) {
            final Maybe<?> k = converters.convert(sourceKeyType, sourceEntry.getKey(), targetKeyType);
            final Maybe<?> v = converters.convert(sourceValueType, sourceEntry.getValue(), targetValueType);
            if (!k.hasValue() || !v.hasValue()) {
                return Maybe.nothing();
            }
            r.put(k.value(), v.value());
        }
        return Maybe.just(r);
    }

    private Map<Object, Object> createMap(Class<?> targetClass){
        if(SortedMap.class.isAssignableFrom(targetClass)){
            return new TreeMap<>();
        }
        return new ConcurrentHashMap<>();
    }
}
