package net.emaze.maple;

import net.emaze.maple.converters.ToByteConverter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.maple.beans.Beans;
import net.emaze.maple.beans.CachingBeans;
import net.emaze.maple.beans.NonCachingBeans;
import net.emaze.maple.converters.BeanToBeanConverter;
import net.emaze.maple.converters.DateToLongConverter;
import net.emaze.maple.converters.EitherToEitherConverter;
import net.emaze.maple.converters.IterableToIterableConverter;
import net.emaze.maple.converters.LongToDateConverter;
import net.emaze.maple.converters.MapToMapConverter;
import net.emaze.maple.converters.MaybeToMaybeConverter;
import net.emaze.maple.converters.NullToNullConverter;
import net.emaze.maple.converters.OptionalToOptionalConverter;
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
import org.springframework.core.ResolvableType;
import net.emaze.maple.proxies.ProxyInspectors;

public class ResolvingMapper implements Mapper {

    private final Converters converters;
    private final ProxyInspectors proxyInspectors;

    public ResolvingMapper(Converters converters, ProxyInspectors proxyInspectors) {
        this.converters = converters;
        this.proxyInspectors = proxyInspectors;
    }

    @Override
    public <R> R map(Object source, Class<R> targetClass) {
        final Class<?> sourceClass = proxyInspectors.inspect(source);
        final ResolvableType sourceType = sourceClass == null ? null : ResolvableType.forClass(sourceClass);
        final ResolvableType targetType = ResolvableType.forClass(targetClass);
        return (R) converters.convert(sourceType, source, targetType).get();
    }

    public static class Builder {

        private ProxyInspectors proxyInspectors;
        private List<Converter> customConverters;
        private List<Converter> builtinConverters;
        private Set<Class<?>> immutables = new HashSet<>();

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
                    new OptionalToOptionalConverter(),
                    new MaybeToMaybeConverter(),
                    new EitherToEitherConverter(),
                    new MapToMapConverter(),
                    new IterableToIterableConverter(),
                    new LongToDateConverter(),
                    new DateToLongConverter(),
                    new BeanToBeanConverter(beans)
            );
            return this;
        }

        public Builder withCustomConverters(Converter... converters) {
            customConverters = Arrays.asList(converters);
            return this;
        }

        public Builder withImmutables(Class<?>... immutables) {
            this.immutables.addAll(Arrays.asList(immutables));
            return this;
        }

        public Builder withImmutables(Collection<Class<?>> immutables) {
            this.immutables.addAll(immutables);
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
