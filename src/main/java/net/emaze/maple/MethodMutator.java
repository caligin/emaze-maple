package net.emaze.maple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class MethodMutator implements Mutator {

    private final Class<?> containingClass;
    private final Method method;

    public MethodMutator(Class<?> containingClass, Method method) {
        this.containingClass = containingClass;
        this.method = method;
    }

    @Override
    public void mutate(Object self, Object value) {
        try {
            method.invoke(self, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String name() {
        final String methodName = method.getName();
        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }

    @Override
    public ResolvableType type() {
        return ResolvableType.forMethodParameter(method, 0, containingClass);
    }
}
