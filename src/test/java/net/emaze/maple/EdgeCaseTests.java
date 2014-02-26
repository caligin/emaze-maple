package net.emaze.maple;

import java.util.HashSet;
import net.emaze.maple.beans.NonCachingBeans;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class EdgeCaseTests {

    public static class Wrapper<T> {

        public T value;
    }

    public static class Source {

        public Wrapper<Integer> wrapped;
    }

    public static class Target {

        public Wrapper<Integer> wrapped;
    }

    @Test
    public void test3() {
        final ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());
        final Source src = new Source();
        src.wrapped = new Wrapper<>();
        src.wrapped.value = 12;
        Target got = mapper.map(src, Target.class);
    }
}
