package net.emaze.maple;

import org.springframework.core.ResolvableType;


public interface Mutator {

    public void mutate(Object self, Object value);

    public String name();

    public ResolvableType type(ResolvableType containingType);
}
