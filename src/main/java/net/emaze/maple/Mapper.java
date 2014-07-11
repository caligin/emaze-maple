package net.emaze.maple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.emaze.dysfunctional.Applications;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;

/**
 *
 * @author rferranti
 */
public interface Mapper {

    <R> R map(Object source, Class<R> targetClass);

    default <R, T> List<R> map(Iterable<T> source, Class<R> elementClass) {
        final List<R> target = new ArrayList<>();
        for (T s : source) {
            target.add(map(s, elementClass));
        }
        return target;
    }

    default <R, T> List<R> map(Iterable<T> source, Class<R> elementClass, BiConsumer<R, T> callback) {
        final List<R> target = new ArrayList<>();
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.accept(mapped, s);
            target.add(mapped);
        }
        return target;
    }

    default <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass) {
        for (T s : source) {
            target.add(map(s, elementClass));
        }
        return target;
    }

    default <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass, BiConsumer<R, T> callback) {
        for (T s : source) {
            final R mapped = map(s, elementClass);
            callback.accept(mapped, s);
            target.add(mapped);
        }
        return target;
    }

    default <R, T> Iterator<R> map(Iterator<T> source, Class<R> elementClass) {
        return Applications.transform(source, (s) -> map(s, elementClass));
    }

    default <R, T> Iterator<R> map(Iterator<T> source, Class<R> elementClass, BiConsumer<R, T> callback) {
        return Applications.transform(source, (s) -> {
            final R t = map(s, elementClass);
            callback.accept(t, s);
            return t;
        });
    }

    default <R, T> Stream<R> map(Stream<T> source, Class<R> elementClass) {
        return source.map((s) -> map(s, elementClass));
    }

    default <R, T> Stream<R> map(Stream<T> source, Class<R> elementClass, BiConsumer<R, T> callback) {
        return source.map((s) -> {
            R t = map(s, elementClass);
            callback.accept(t, s);
            return t;
        });
    }

    default <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass) {
        final Map<K, RV> result = new HashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            result.put(entry.getKey(), map(entry.getValue(), elementClass));
        }
        return result;
    }

    default <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass, BiConsumer<RV, V> callback) {
        final Map<K, RV> result = new HashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            final V value = entry.getValue();
            final RV mapped = map(value, elementClass);
            callback.accept(mapped, value);
            result.put(entry.getKey(), mapped);
        }
        return result;
    }

    default <RL, RR, SL, SR> Either<RL, RR> map(Either<SL, SR> source, final Class<RL> leftClass, final Class<RR> rightClass) {
        if (source.maybe().hasValue()) {
            return Either.<RL, RR>right(source.maybe().fmap((r) -> map(r, rightClass)).value());
        }
        return Either.<RL, RR>left(source.flip().maybe().fmap((l) -> map(l, leftClass)).value());
    }

    default <R1, R2, T1, T2> Pair<R1, R2> map(Pair<T1, T2> source, Class<R1> firstClass, Class<R2> secondClass) {
        return source.fmap((f) -> map(f, firstClass), (s) -> map(s, secondClass));
    }

    default <R1, R2, R3, T1, T2, T3> Triple<R1, R2, R3> map(Triple<T1, T2, T3> source, Class<R1> firstClass, Class<R2> secondClass, Class<R3> thirdClass) {
        return source.fmap((f) -> map(f, firstClass), (s) -> map(s, secondClass), (t) -> map(t, thirdClass));
    }

    default <R, T> Maybe<R> map(Maybe<T> source, final Class<R> elementClass) {
        return source.fmap((s) -> map(s, elementClass));
    }

    default <R, T> Optional<R> map(Optional<T> source, Class<R> elementClass) {
        return source.map((s) -> map(s, elementClass));
    }

}
