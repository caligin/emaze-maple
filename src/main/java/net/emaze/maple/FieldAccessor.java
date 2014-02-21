package net.emaze.maple;

import java.lang.reflect.Field;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class FieldAccessor implements Accessor {

    private final Class<?> containingClass;
    private final Field field;

    public FieldAccessor(Class<?> containingClass, Field field) {
        this.containingClass = containingClass;
        this.field = field;
    }

    @Override
    public Object access(Object self) {
        try {
            return field.get(self);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String name() {
        return field.getName();
    }

    @Override
    public ResolvableType type() {
        return ResolvableType.forField(field, containingClass);
    }

}
