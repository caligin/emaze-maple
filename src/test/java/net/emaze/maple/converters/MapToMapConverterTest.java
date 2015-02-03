package net.emaze.maple.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import net.emaze.maple.ResolvingMapper;
import org.junit.Assert;
import org.junit.Test;

public class MapToMapConverterTest {

    public static class SourceBean {

        public Map<String, String> map;
    }

    public static class TargetBean {

        public Map<String, String> map;
    }

    @Test
    public void canMapAMapContainingNulls() {
        final String key = "asd";
        final ResolvingMapper mapper = ResolvingMapper.Builder.defaults().build();
        SourceBean sourceBean = new SourceBean();
        sourceBean.map = new HashMap<>();
        sourceBean.map.put(key, null);
        TargetBean target = mapper.map(sourceBean, TargetBean.class);
        Assert.assertTrue(target.map.containsKey(key) && target.map.get(key) == null);
    }

    public static class SourceOrderBean {

        public SortedMap<String, String> map;
    }

    public static class TargetOrderBean {

        public SortedMap<String, String> map;
    }

    public static class ReverseNaturalComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return -1 * o1.compareTo(o2);
        }
    }

    @Test
    public void mappingSortedMapWithCustomComparatorPreservesOrderWhenSourceUsesNaturalOrdering() {
        final List<String> keys = Arrays.asList("a", "b");
        final ResolvingMapper mapper = ResolvingMapper.Builder.defaults().build();
        SourceOrderBean sourceBean = new SourceOrderBean();
        sourceBean.map = new TreeMap<>();
        for (String key : keys) {
            sourceBean.map.put(key, key);
        }
        TargetOrderBean target = mapper.map(sourceBean, TargetOrderBean.class);
        Assert.assertEquals(new ArrayList(target.map.keySet()), new ArrayList(sourceBean.map.keySet()));
    }

    @Test
    public void mappingSortedMapWithCustomComparatorPreservesOrderWhenSourceHasCustomComparator() {
        final List<String> keys = Arrays.asList("a", "b");
        final ResolvingMapper mapper = ResolvingMapper.Builder.defaults().build();
        SourceOrderBean sourceBean = new SourceOrderBean();
        sourceBean.map = new TreeMap<>(new ReverseNaturalComparator());
        for (String key : keys) {
            sourceBean.map.put(key, key);
        }
        TargetOrderBean target = mapper.map(sourceBean, TargetOrderBean.class);
        Assert.assertEquals(new ArrayList(target.map.keySet()), new ArrayList(sourceBean.map.keySet()));
    }
}
