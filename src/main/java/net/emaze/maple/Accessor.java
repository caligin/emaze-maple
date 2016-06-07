package net.emaze.maple;

import org.springframework.core.ResolvableType;


public interface Accessor {

    public Object access(Object self);

    public String name();

    public ResolvableType type(ResolvableType containingType);
}
