package net.emaze.maple.converters;

import java.util.HashMap;
import java.util.Map;
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
   public void canMapAMapContainingNulls(){
        final String key = "asd";
        final ResolvingMapper mapper = ResolvingMapper.Builder.defaults().build();
        SourceBean sourceBean = new SourceBean();
        sourceBean.map = new HashMap<>();
        sourceBean.map.put(key, null);
        TargetBean target = mapper.map(sourceBean, TargetBean.class);
        Assert.assertTrue(target.map.containsKey(key) && target.map.get(key) == null);
   }

}
