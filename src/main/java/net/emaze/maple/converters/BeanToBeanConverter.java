package net.emaze.maple.converters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Accessor;
import net.emaze.maple.beans.Beans;
import net.emaze.maple.Converter;
import net.emaze.maple.Converters;
import net.emaze.maple.Mutator;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class BeanToBeanConverter implements Converter {

    private final Beans beans;

    public BeanToBeanConverter(Beans beans) {
        this.beans = beans;
    }

    @Override
    public boolean canConvert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        return beans.constructor(targetType.getRawClass()).hasValue();
    }

    @Override
    public Maybe<?> convert(Converters converters, ResolvableType sourceType, Object source, ResolvableType targetType) {
        try {
            final Constructor ctor = beans.constructor(targetType.getRawClass()).value();
            final Object target = ctor.newInstance();
            final Map<String, Mutator> targetMutators = beans.mutators(targetType.resolve());
            final Map<String, Accessor> sourceAccessors = beans.accessors(sourceType.resolve());
            for (Map.Entry<String, Accessor> entry : sourceAccessors.entrySet()) {
                final String field = entry.getKey();
                if (!targetMutators.containsKey(field)) {
                    continue;
                }
                final Accessor accessor = entry.getValue();
                final Mutator mutator = targetMutators.get(field);
                final Maybe<?> converted = converters.convert(accessor.type(), accessor.access(source), mutator.type());
                for (Object c : converted) {
                    mutator.mutate(target, c);
                }
            }
            return Maybe.just(target);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            return Maybe.nothing();
        }
    }
}
