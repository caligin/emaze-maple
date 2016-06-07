package net.emaze.maple.proxies;

import java.util.ArrayList;
import java.util.List;


public class ProxyInspectors {

    private final ProxyInspector[] inspectors;

    public ProxyInspectors(ProxyInspector[] inspectors) {
        this.inspectors = inspectors;
    }

    public static ProxyInspectors detect() {
        final List<ProxyInspector> inspectors = new ArrayList<>();
        if (isPresent("org.hibernate.proxy.HibernateProxy")) {
            inspectors.add(new HibernateProxyInspector());
        }
        if (isPresent("javassist.util.proxy.ProxyFactory")) {
            inspectors.add(new JavassistProxyInspector());
        }
        if (isPresent("org.springframework.cglib.proxy.Enhancer")) {
            inspectors.add(new SpringCglibProxyInspector());
        }
        if(isPresent("net.sf.cglib.proxy.Enhancer")){
            inspectors.add(new CglibProxyInspector());
        }
        return new ProxyInspectors(inspectors.toArray(new ProxyInspector[0]));
    }

    public <T> Class<?> inspect(Object source) {
        if (source == null) {
            return null;
        }
        for (ProxyInspector inspector : inspectors) {
            if (inspector.supports(source)) {
                return inspector.inspect(source);
            }
        }
        return source.getClass();
    }

    private static boolean isPresent(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ex) {
            try {
                final int lastDot = name.lastIndexOf('.');
                if (lastDot == -1) {
                    return false;
                }
                Class.forName(name.substring(0, lastDot) + '$' + name.substring(lastDot + 1));
                return true;
            } catch (ClassNotFoundException ex2) {
                return false;
            }
        }
    }

}
