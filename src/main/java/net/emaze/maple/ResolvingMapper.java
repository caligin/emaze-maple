package net.emaze.maple;

import net.emaze.maple.converters.ToByteConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.dysfunctional.dispatching.actions.BinaryAction;
import net.emaze.dysfunctional.dispatching.delegates.Delegate;
import net.emaze.dysfunctional.iterations.ArrayIterable;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.beans.Beans;
import net.emaze.maple.converters.BeanToBeanConverter;
import net.emaze.maple.converters.EitherToEitherConverter;
import net.emaze.maple.converters.IterableToIterableConverter;
import net.emaze.maple.converters.MapToMapConverter;
import net.emaze.maple.converters.MaybeToMaybeConverter;
import net.emaze.maple.converters.NullToNullConverter;
import net.emaze.maple.converters.PairToPairConverter;
import net.emaze.maple.converters.SameImmutablesConverter;
import net.emaze.maple.converters.ToDoubleConverter;
import net.emaze.maple.converters.ToFloatConverter;
import net.emaze.maple.converters.ToIntConverter;
import net.emaze.maple.converters.ToLongConverter;
import net.emaze.maple.converters.ToShortConverter;
import net.emaze.maple.converters.ToStringConverter;
import net.emaze.maple.converters.TripleToTripleConverter;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class ResolvingMapper implements Mapper {

    private final Converters converters;

    public ResolvingMapper(Converters converters) {
        this.converters = converters;
    }

    public static ResolvingMapper create(Converters converters) {
        return new ResolvingMapper(converters);
    }

    public static ResolvingMapper create(Beans beans, Set<Class<?>> immutables, Converter... custom) {
        final List<Converter> builtins = Arrays.<Converter>asList(
                new NullToNullConverter(),
                new SameImmutablesConverter(immutables),
                new ToByteConverter(),
                new ToShortConverter(),
                new ToIntConverter(),
                new ToLongConverter(),
                new ToFloatConverter(),
                new ToDoubleConverter(),
                new ToStringConverter(),
                new PairToPairConverter(),
                new TripleToTripleConverter(),
                new MaybeToMaybeConverter(),
                new EitherToEitherConverter(),
                new MapToMapConverter(),
                new IterableToIterableConverter(),
                new BeanToBeanConverter(beans)
        );
        final List<Converter> converters = Consumers.all(Multiplexing.flatten(new ArrayIterable<>(custom), builtins));
        return new ResolvingMapper(new Converters(converters));
    }

    @Override
    public <R, T> List<R> list(Iterable<T> source, Class<R> elementClass) {
        final List<R> result = new ArrayList<>();
        for (T s : source) {
            result.add(map(s, elementClass));
        }
        return result;
    }

    @Override
    public <R, T> List<R> list(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback) {
        final List<R> result = new ArrayList<>();
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.perform(mapped, s);
            result.add(mapped);
        }
        return result;
    }

    @Override
    public <R, T> Set<R> set(Iterable<T> source, Class<R> elementClass) {
        final Set<R> result = new HashSet<>();
        for (T s : source) {
            result.add(map(s, elementClass));
        }
        return result;
    }

    @Override
    public <R, T> Set<R> set(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback) {
        final Set<R> result = new HashSet<>();
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.perform(mapped, s);
            result.add(mapped);
        }
        return result;
    }

    @Override
    public <RV, K, V> Map<K, RV> entries(Map<K, V> source, Class<RV> elementClass) {
        final Map<K, RV> result = new HashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            result.put(entry.getKey(), map(entry.getValue(), elementClass));
        }
        return result;
    }

    @Override
    public <RV, K, V> Map<K, RV> entries(Map<K, V> source, Class<RV> elementClass, BinaryAction<RV, V> callback) {
        final Map<K, RV> result = new HashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            final V value = entry.getValue();
            final RV mapped = map(value, elementClass);
            callback.perform(mapped, value);
            result.put(entry.getKey(), mapped);
        }
        return result;
    }

    @Override
    public <R, T> Maybe<R> maybe(Maybe<T> source, final Class<R> elementClass) {
        return source.fmap(new Delegate<R, T>() {

            @Override
            public R perform(T t) {
                return map(t, elementClass);
            }
        });

    }

    @Override
    public <R> R map(Object source, Class<R> targetClass) {
        final ResolvableType sourceType = source == null ? null : ResolvableType.forClass(source.getClass());
        final ResolvableType targetType = ResolvableType.forClass(targetClass);
        return (R) converters.convert(sourceType, source, targetType).value();
    }
}
