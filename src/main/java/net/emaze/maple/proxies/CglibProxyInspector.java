package net.emaze.maple.proxies;

import net.sf.cglib.proxy.Enhancer;

/**
 *
 * @author rferranti
 */
public class CglibProxyInspector implements ProxyInspector {

    @Override
    public boolean supports(Object suspect) {
        return Enhancer.isEnhanced(suspect.getClass());
    }

    @Override
    public Class<?> inspect(Object suspect) {
        return suspect.getClass().getSuperclass();
    }

}
