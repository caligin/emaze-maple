package net.emaze.maple;

import java.util.HashSet;
import junit.framework.Assert;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.beans.NonCachingBeans;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class GenericsTest {

    ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());

    public static class SourceWithField {

        public Maybe<String> field;
    }

    public static class TargetWithField {

        public Maybe<Integer> field;
    }

    public static class SourceWithAccessor {

        private Maybe<String> field;

        public Maybe<String> getField() {
            return field;
        }

    }

    public static class TargetWithMutator {

        private Maybe<Integer> field;

        public void setField(Maybe<Integer> field) {
            this.field = field;
        }

    }

    @Test
    public void canMapGenericField() {
        final SourceWithField src = new SourceWithField();
        src.field = Maybe.just("1");
        final TargetWithField got = mapper.map(src, TargetWithField.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.value());
    }

    @Test
    public void canMapGenericWithMethods() {
        final SourceWithAccessor src = new SourceWithAccessor();
        src.field = Maybe.just("1");
        final TargetWithMutator got = mapper.map(src, TargetWithMutator.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.value());
    }

}
