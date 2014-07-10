package net.emaze.maple;

import java.util.ArrayList;
import net.emaze.maple.converters.ToByteConverter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.dysfunctional.dispatching.actions.BinaryAction;
import net.emaze.dysfunctional.dispatching.delegates.Delegate;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
import net.emaze.maple.beans.Beans;
import net.emaze.maple.beans.CachingBeans;
import net.emaze.maple.beans.NonCachingBeans;
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
import net.emaze.maple.proxies.ProxyInspector;
import net.emaze.maple.types.MapleType;
import net.emaze.maple.proxies.ProxyInspectors;

/**
 *
 * @author rferranti
 */
public class ResolvingMapper implements Mapper {

    private final Converters converters;
    private final ProxyInspectors proxyInspectors;

    public ResolvingMapper(Converters converters, ProxyInspectors proxyInspectors) {
        this.converters = converters;
        this.proxyInspectors = proxyInspectors;
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
    public <R, T> List<R> map(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback) {
        final List<R> target = new ArrayList<>();
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.perform(mapped, s);
            target.add(mapped);
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
        final Class<?> sourceClass = proxyInspectors.inspect(source);
        final MapleType sourceType = sourceClass == null ? null : MapleType.forClass(sourceClass);
        final MapleType targetType = MapleType.forClass(targetClass);
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

    public static class Builder {

        private ProxyInspectors proxyInspectors;
        private List<Converter> customConverters;
        private List<Converter> builtinConverters;
        private Set<Class<?>> immutables;

        public static Builder defaults() {
            return new Builder()
                    .withBuiltinConverters(new CachingBeans(new NonCachingBeans()))
                    .withDetectedProxyInspectors();
        }

        public static Builder clean() {
            return new Builder();
        }

        public Builder withBuiltinConverters(Beans beans) {
            this.builtinConverters = Arrays.<Converter>asList(
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
            return this;
        }

        public Builder withCustomConverters(Converter... converters) {
            customConverters = Arrays.asList(converters);
            return this;
        }

        public Builder withImmutables(Class<?>... immutables) {
            this.immutables = new HashSet<>(Arrays.asList(immutables));
            return this;
        }

        public Builder withProxyInspectors(ProxyInspectors proxyInspectors) {
            this.proxyInspectors = proxyInspectors;
            return this;
        }

        public Builder withDetectedProxyInspectors() {
            this.proxyInspectors = ProxyInspectors.detect();
            return this;
        }

        public ResolvingMapper build() {
            final List<Converter> cl = Consumers.all(Multiplexing.flatten(
                    customConverters == null ? Arrays.<Converter>asList() : customConverters,
                    builtinConverters == null ? Arrays.<Converter>asList() : builtinConverters
            ));
            final Set<Class<?>> cs = this.immutables == null ? Collections.<Class<?>>emptySet() : this.immutables;
            final ProxyInspectors pi = this.proxyInspectors == null ? new ProxyInspectors(new ProxyInspector[0]) : this.proxyInspectors;
            return new ResolvingMapper(new Converters(cs, cl), pi);
        }
    }
}
