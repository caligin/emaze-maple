package net.emaze.maple;

import net.emaze.maple.proxies.ProxyInspectors;
import org.junit.Assert;
import org.junit.Test;


public class ProxiesTest {

    public static class Bean {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    final ProxyInspectors pi = ProxyInspectors.detect();

    @Test
    public void cglib() {
        final Bean enhanced = (Bean) net.sf.cglib.proxy.Enhancer.create(Bean.class, new net.sf.cglib.proxy.NoOp() {
            //does nothing
        });
        Assert.assertEquals(Bean.class, pi.inspect(enhanced));
    }

    @Test
    public void springCglib() {
        final Bean enhanced = (Bean) org.springframework.cglib.proxy.Enhancer.create(Bean.class, new org.springframework.cglib.proxy.NoOp() {
            //does nothing
        });
        Assert.assertEquals(Bean.class, pi.inspect(enhanced));
    }

    @Test
    public void hibernate() throws NoSuchMethodException {

        org.hibernate.proxy.HibernateProxy enhanced = org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer.getProxy(
                "bean",
                Bean.class,
                new Class[]{
                    org.hibernate.proxy.HibernateProxy.class
                },
                Bean.class.getMethod("getId"),
                Bean.class.getMethod("setId", int.class),
                null,
                null,
                null
        );
        Assert.assertEquals(Bean.class, pi.inspect(enhanced));
    }

    @Test
    public void javassist() throws Exception {
        final javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
        factory.setSuperclass(Bean.class);
        final Class<?> clazz = factory.createClass();
        final Object enhanced = clazz.newInstance();
        Assert.assertEquals(Bean.class, pi.inspect(enhanced));
    }

}
