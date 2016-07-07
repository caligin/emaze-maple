package net.emaze.maple;

import org.junit.Assert;
import net.emaze.dysfunctional.options.Maybe;
import org.junit.Test;


public class GenericsTest {

    final ResolvingMapper mapper = ResolvingMapper.Builder.defaults().build();

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
        Assert.assertEquals(Integer.valueOf(1), got.field.get());
    }

    @Test
    public void canMapGenericWithMethods() {
        final SourceWithAccessor src = new SourceWithAccessor();
        src.field = Maybe.just("1");
        final TargetWithMutator got = mapper.map(src, TargetWithMutator.class);
        Assert.assertEquals(Integer.valueOf(1), got.field.get());
    }

}
