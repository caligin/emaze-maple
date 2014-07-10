package net.emaze.maple.proxies;

/**
 *
 * @author rferranti
 */
public interface ProxyInspector {

    public boolean supports(Object suspect);

    public Class<?> inspect(Object suspect);
}
