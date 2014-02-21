package net.emaze.maple.beans;

import net.emaze.maple.beans.Beans;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.Accessor;
import net.emaze.maple.FieldAccessor;
import net.emaze.maple.FieldMutator;
import net.emaze.maple.MethodAccessor;
import net.emaze.maple.MethodMutator;
import net.emaze.maple.Mutator;

/**
 *
 * @author rferranti
 */
public class NonCachingBeans implements Beans {

    @Override
    public Map<String, Accessor> accessors(Class<?> cls) {
        final Map<String, Accessor> r = new HashMap<>();
        for (Field field : cls.getFields()) {
            final FieldAccessor fa = new FieldAccessor(cls, field);
            r.put(fa.name(), fa);
        }
        for (Method method : cls.getMethods()) {
            if (method.isSynthetic()) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                continue;
            }
            if (!method.getName().startsWith("is") && !method.getName().startsWith("get")) {
                continue;
            }
            final MethodAccessor ma = new MethodAccessor(cls, method);
            r.put(ma.name(), ma);
        }
        return r;
    }

    @Override
    public Map<String, Mutator> mutators(Class<?> cls) {
        final Map<String, Mutator> r = new HashMap<>();
        for (Field field : cls.getFields()) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            final FieldMutator fm = new FieldMutator(cls, field);
            r.put(fm.name(), fm);
        }
        for (Method method : cls.getMethods()) {
            if (method.isSynthetic()) {
                continue;
            }
            if (method.getParameterTypes().length != 1) {
                continue;
            }
            if (!method.getName().startsWith("set")) {
                continue;
            }
            final MethodMutator mm = new MethodMutator(cls, method);
            r.put(mm.name(), mm);
        }
        return r;

    }

    @Override
    public Maybe<Constructor> constructor(Class<?> cls) {
        for (Constructor ctor : cls.getConstructors()) {
            if (ctor.getParameterTypes().length == 0) {
                return Maybe.just(ctor);
            }
        }
        return Maybe.nothing();
    }
}
