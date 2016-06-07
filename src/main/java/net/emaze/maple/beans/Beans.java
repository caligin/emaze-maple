package net.emaze.maple.beans;

import java.lang.reflect.Constructor;
import java.util.Map;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Accessor;
import net.emaze.maple.Mutator;


public interface Beans {

    Map<String, Accessor> accessors(Class<?> cls);

    Map<String, Mutator> mutators(Class<?> cls);

    Maybe<Constructor> constructor(Class<?> cls);

}
