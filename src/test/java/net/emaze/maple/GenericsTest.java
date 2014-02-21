package net.emaze.maple;

import java.util.HashSet;
import junit.framework.Assert;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.beans.NonCachingBeans;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class GenericsTest {

    ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());

    public static class Source {

        public Maybe<String> field;
    }

    public static class Target {

        public Maybe<Integer> field;
    }

    @Test
    public void canMapGenericField() {
        final Source src = new Source();
        src.field = Maybe.just("1");
        final Target got = mapper.map(src, Target.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.value());
    }

}
