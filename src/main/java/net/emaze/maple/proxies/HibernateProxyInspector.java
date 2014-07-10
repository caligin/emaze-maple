package net.emaze.maple.proxies;

import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author rferranti
 */
public class HibernateProxyInspector implements ProxyInspector {

    @Override
    public boolean supports(Object suspect) {
        return suspect instanceof HibernateProxy;
    }

    @Override
    public Class<?> inspect(Object suspect) {
        return ((HibernateProxy) suspect).getHibernateLazyInitializer().getPersistentClass();
    }

}
