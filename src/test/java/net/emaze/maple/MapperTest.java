package net.emaze.maple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.emaze.maple.beans.NonCachingBeans;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class MapperTest {

    ResolvingMapper mapper = ResolvingMapper.create(new NonCachingBeans(), new HashSet<Class<?>>());

    public static class SourceWithCollectionField {

        public Map<String, String> aMap;
    }

    public static class TargetWithCollectionField {

        public Map<String, String> aMap;
    }

    public static class TargetWithDifferentCollectionField {

        public Map<String, Integer> aMap;
    }

    public static class SimpleSource {

        public String field;
    }

    public static class SimpleTarget {

        public String field;
    }

    @Test
    public void mappingNullYieldsNull() throws Exception {
        SimpleSource got = mapper.map(null, SimpleSource.class);
        Assert.assertNull(got);
    }

    @Test
    public void canMapPublicFieldToSelf() throws Exception {
        final SimpleSource src = new SimpleSource();
        src.field = "test";
        final SimpleSource got = mapper.map(src, SimpleSource.class);
        Assert.assertEquals(src.field, got.field);
    }

    @Test
    public void canMapPublicFieldToOther() throws Exception {
        final SimpleSource src = new SimpleSource();
        src.field = "test";
        final SimpleTarget got = mapper.map(src, SimpleTarget.class);
        Assert.assertEquals(src.field, got.field);
    }

    @Test
    public void canMapMapToMap() throws Exception {
        final SourceWithCollectionField src = new SourceWithCollectionField();
        src.aMap = new HashMap<>();
        src.aMap.put("key", "1");
        final TargetWithCollectionField got = mapper.map(src, TargetWithCollectionField.class);
        Assert.assertEquals(src.aMap, got.aMap);
    }

    @Test
    public void canMapMapToDifferentMap() throws Exception {
        final SourceWithCollectionField src = new SourceWithCollectionField();
        src.aMap = new HashMap<>();
        src.aMap.put("key", "1");
        final TargetWithDifferentCollectionField got = mapper.map(src, TargetWithDifferentCollectionField.class);
        Assert.assertEquals(got.aMap.get("key"), Integer.valueOf(1));
    }

    @Test
    public void canMapIntegerToString() {
        String got = mapper.map(Integer.valueOf(1), String.class);
        Assert.assertEquals("1", got);
    }

    @Test
    public void canMapByteToString() {
        String got = mapper.map((byte) 1, String.class);
        Assert.assertEquals("1", got);
    }

    @Test
    public void canMapShortToString() {
        String got = mapper.map((short) 1, String.class);
        Assert.assertEquals("1", got);
    }

    @Test
    public void canMapIntToString() {
        String got = mapper.map(1, String.class);
        Assert.assertEquals("1", got);
    }

    @Test
    public void canMapLongToString() {
        String got = mapper.map(1l, String.class);
        Assert.assertEquals("1", got);
    }

    @Test
    public void canMapFloatToString() {
        String got = mapper.map(1f, String.class);
        Assert.assertEquals("1.0", got);
    }

    @Test
    public void canMapDoubleToString() {
        String got = mapper.map(1d, String.class);
        Assert.assertEquals("1.0", got);
    }

    @Test
    public void canMapStringToByte() {
        Byte got = mapper.map("1", Byte.class);
        Assert.assertEquals(Byte.valueOf((byte) 1), got);
    }

    @Test
    public void canMapStringToShort() {
        Short got = mapper.map("1", Short.class);
        Assert.assertEquals(Short.valueOf((short) 1), got);
    }

    @Test
    public void canMapStringToInteger() {
        Integer got = mapper.map("1", Integer.class);
        Assert.assertEquals(Integer.valueOf(1), got);
    }

    @Test
    public void canMapStringToLong() {
        Long got = mapper.map("1", Long.class);
        Assert.assertEquals(Long.valueOf(1), got);
    }

    @Test
    public void canMapStringToFloat() {
        Float got = mapper.map("1", Float.class);
        Assert.assertEquals(Float.valueOf(1), got);
    }

    @Test
    public void canMapStringToDouble() {
        Double got = mapper.map("1", Double.class);
        Assert.assertEquals(Double.valueOf(1), got);
    }

    @Test
    public void canMapStringTobyte() {
        byte got = mapper.map("1", byte.class);
        Assert.assertEquals((byte) 1, got);
    }

    @Test
    public void canMapStringToshort() {
        short got = mapper.map("1", short.class);
        Assert.assertEquals((short) 1, got);
    }

    @Test
    public void canMapStringToint() {
        int got = mapper.map("1", int.class);
        Assert.assertEquals((int) 1, got);
    }

    @Test
    public void canMapStringTolong() {
        long got = mapper.map("1", long.class);
        Assert.assertEquals(1l, got);
    }

    @Test
    public void canMapStringTofloat() {
        float got = mapper.map("1", float.class);
        Assert.assertEquals(1f, got, 0.1);
    }

    @Test
    public void canMapStringTodouble() {
        double got = mapper.map("1", double.class);
        Assert.assertEquals(1d, got, 0.1);
    }
}
