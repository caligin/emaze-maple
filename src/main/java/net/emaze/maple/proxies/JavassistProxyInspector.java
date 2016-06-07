package net.emaze.maple.proxies;

import javassist.util.proxy.ProxyFactory;


public class JavassistProxyInspector implements ProxyInspector {

    @Override
    public boolean supports(Object suspect) {
        return ProxyFactory.isProxyClass(suspect.getClass());
    }

    @Override
    public Class<?> inspect(Object suspect) {
        return suspect.getClass().getSuperclass();
    }

}
