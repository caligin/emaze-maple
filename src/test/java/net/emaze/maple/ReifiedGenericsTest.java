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
public class ReifiedGenericsTest {

    ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());

    public static class SourceWithField<T> {

        public Maybe<T> field;
    }

    public static class ReifiedSourceWithField extends SourceWithField<String> {
    }

    public static class TargetWithField<T> {

        public Maybe<T> field;
    }

    public static class ReifiedTargetWithField extends TargetWithField<Integer> {
    }

    public static class SourceWithAccessor<T> {

        Maybe<T> field;

        public Maybe<T> getField() {
            return field;
        }

    }

    public static class ReifiedSourceWithAccessor extends SourceWithAccessor<String> {
    }

    public static class TargetWithMutator<T> {

        Maybe<T> field;

        public void setField(Maybe<T> field) {
            this.field = field;
        }

    }

    public static class ReifiedTargetWithMutator extends TargetWithMutator<Integer> {
    }

    @Test
    public void canMapReifiedGenericField() {
        final ReifiedSourceWithField src = new ReifiedSourceWithField();
        src.field = Maybe.just("1");
        final ReifiedTargetWithField got = mapper.map(src, ReifiedTargetWithField.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.value());
    }

    @Test
    @Ignore
    public void canMapReifiedGenericWithMethods() {
        final ReifiedSourceWithAccessor src = new ReifiedSourceWithAccessor();
        src.field = Maybe.just("1");
        final ReifiedTargetWithMutator got = mapper.map(src, ReifiedTargetWithMutator.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.value());
    }

}