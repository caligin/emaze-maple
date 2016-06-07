package net.emaze.maple.proxies;

import org.springframework.cglib.proxy.Enhancer;


public class SpringCglibProxyInspector implements ProxyInspector {

    @Override
    public boolean supports(Object suspect) {
        return Enhancer.isEnhanced(suspect.getClass());
    }

    @Override
    public Class<?> inspect(Object suspect) {
        return suspect.getClass().getSuperclass();
    }

}
