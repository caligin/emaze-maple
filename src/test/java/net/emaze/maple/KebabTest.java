package net.emaze.maple;

import java.util.HashSet;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Triple;
import net.emaze.maple.beans.NonCachingBeans;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class KebabTest {

    private final ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());

    public static class Kebab<T1, T2, T3, T4> {

        public Triple<T1, T2, Maybe<Either<T3, T4>>> kebab;

    }

    public static class StringsKebab extends Kebab<String, String, String, String> {
    }

    public static class PrimitivesKebab extends Kebab<Byte, Short, Integer, Long> {
    }

    @Test
    public void kebabToPrimitives() {
        final StringsKebab src = new StringsKebab();
        src.kebab = Triple.of("1", "2", Maybe.just(Either.<String, String>right("3")));
        final PrimitivesKebab got = mapper.map(src, PrimitivesKebab.class);
        Triple<Byte, Short, Maybe<Either<Integer, Long>>> expected = Triple.of((byte) 1, (short) 2, Maybe.just(Either.<Integer, Long>right(3l)));
        Assert.assertEquals(expected, got.kebab);
    }

    @Test
    public void kebabToStrings() {
        final PrimitivesKebab src = new PrimitivesKebab();
        src.kebab = Triple.of((byte) 1, (short) 2, Maybe.just(Either.<Integer, Long>right(3l)));
        final StringsKebab got = mapper.map(src, StringsKebab.class);
        Triple<String, String, Maybe<Either<String, String>>> expected = Triple.of("1", "2", Maybe.just(Either.<String, String>right("3")));
        Assert.assertEquals(expected, got.kebab);
    }
}
