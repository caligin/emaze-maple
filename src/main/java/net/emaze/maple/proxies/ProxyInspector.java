package net.emaze.maple.proxies;


public interface ProxyInspector {

    public boolean supports(Object suspect);

    public Class<?> inspect(Object suspect);
}
