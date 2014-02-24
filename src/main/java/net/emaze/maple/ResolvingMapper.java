package net.emaze.maple;

import java.util.ArrayList;
import net.emaze.maple.converters.ToByteConverter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.dysfunctional.dispatching.actions.BinaryAction;
import net.emaze.dysfunctional.dispatching.delegates.Delegate;
import net.emaze.dysfunctional.iterations.ArrayIterable;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
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
                new SameImmutablesConverter(),
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
        return new ResolvingMapper(new Converters(immutables, converters));
    }

    @Override
    public <R, T> List<R> map(Iterable<T> source, Class<R> elementClass) {
        final List<R> target = new ArrayList<>();
        for (T s : source) {
            target.add(map(s, elementClass));
        }
        return target;
    }

    @Override
    public <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass) {
        for (T s : source) {
            target.add(map(s, elementClass));
        }
        return target;
    }

    @Override
    public <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass, BinaryAction<R, T> callback) {
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.perform(mapped, s);
            target.add(mapped);
        }
        return target;
    }

    @Override
    public <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass) {
        final Map<K, RV> result = new HashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            result.put(entry.getKey(), map(entry.getValue(), elementClass));
        }
        return result;
    }

    @Override
    public <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass, BinaryAction<RV, V> callback) {
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
    public <RL, RR, SL, SR> Either<RL, RR> map(Either<SL, SR> source, final Class<RL> leftClass, final Class<RR> rightClass) {
        if (source.maybe().hasValue()) {
            return Either.<RL, RR>right(source.maybe().fmap(new MapTo<RR, SR>(rightClass)).value());
        }
        return Either.<RL, RR>left(source.flip().maybe().fmap(new MapTo<RL, SL>(leftClass)).value());
    }

    @Override
    public <R1, R2, T1, T2> Pair<R1, R2> map(Pair<T1, T2> source, Class<R1> firstClass, Class<R2> secondClass) {
        return source.fmap(new MapTo<R1, T1>(firstClass), new MapTo<R2, T2>(secondClass));
    }

    @Override
    public <R1, R2, R3, T1, T2, T3> Triple<R1, R2, R3> map(Triple<T1, T2, T3> source, Class<R1> firstClass, Class<R2> secondClass, Class<R3> thirdClass) {
        return source.fmap(new MapTo<R1, T1>(firstClass), new MapTo<R2, T2>(secondClass), new MapTo<R3, T3>(thirdClass));
    }

    @Override
    public <R, T> Maybe<R> map(Maybe<T> source, final Class<R> elementClass) {
        return source.fmap(new MapTo<R, T>(elementClass));
    }

    @Override
    public <R> R map(Object source, Class<R> targetClass) {
        final ResolvableType sourceType = source == null ? null : ResolvableType.forClass(source.getClass());
        final ResolvableType targetType = ResolvableType.forClass(targetClass);
        return (R) converters.convert(sourceType, source, targetType).value();
    }

    public class MapTo<R, T> implements Delegate<R, T> {

        private final Class<R> cls;

        public MapTo(Class<R> cls) {
            this.cls = cls;
        }

        @Override
        public R perform(T t) {
            return map(t, cls);
        }

    }
}
