package net.emaze.maple;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.emaze.dysfunctional.dispatching.actions.BinaryAction;
import net.emaze.dysfunctional.options.Maybe;

/**
 *
 * @author rferranti
 */
public interface Mapper {

    <R> R map(Object source, Class<R> targetClass);

    <R, T> Maybe<R> maybe(Maybe<T> source, Class<R> elementClass);
    
    <R, T> List<R> list(Iterable<T> source, Class<R> elementClass);

    <R, T> List<R> list(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback);

    <R, T> Set<R> set(Iterable<T> source, Class<R> elementClass);

    <R, T> Set<R> set(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback);

    <RV, K, V> Map<K, RV> entries(Map<K, V> source, Class<RV> elementClass);

    <RV, K, V> Map<K, RV> entries(Map<K, V> source, Class<RV> elementClass, BinaryAction<RV, V> callback);
}
