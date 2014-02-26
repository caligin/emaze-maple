package net.emaze.maple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class MethodAccessor implements Accessor {

    private final Method method;

    public MethodAccessor(Method method) {
        this.method = method;
    }

    @Override
    public Object access(Object self) {
        try {
            return method.invoke(self);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String name() {
        final String methodName = method.getName();
        return methodName.startsWith("i")
                ? Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3)
                : Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }

    @Override
    public MapleType type(MapleType containingType) {
        return MapleType.forMethodReturnType(method, containingType.getRawClass());
    }

}
