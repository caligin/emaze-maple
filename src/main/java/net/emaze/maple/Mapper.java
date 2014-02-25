package net.emaze.maple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.emaze.dysfunctional.dispatching.actions.BinaryAction;
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

    <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass);

    <RV, K, V> Map<K, RV> map(Map<K, V> source, Class<RV> elementClass, BinaryAction<RV, V> callback);

    <R, T> List<R> map(Iterable<T> source, Class<R> elementClass);
    <R, T> List<R> map(Iterable<T> source, Class<R> elementClass, BinaryAction<R, T> callback);

    <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass);

    <R, T, C extends Collection<R>> C map(Iterable<T> source, C target, Class<R> elementClass, BinaryAction<R, T> callback);

    <R, T> Maybe<R> map(Maybe<T> source, Class<R> elementClass);

    <RL, RR, SL, SR> Either<RL, RR> map(Either<SL, SR> source, Class<RL> leftClass, Class<RR> rightClass);

    <R1, R2, T1, T2> Pair<R1, R2> map(Pair<T1, T2> source, Class<R1> firstClass, Class<R2> secondClass);

    <R1, R2, R3, T1, T2, T3> Triple<R1, R2, R3> map(Triple<T1, T2, T3> source, Class<R1> firstClass, Class<R2> secondClass, Class<R3> thirdClass);

}
