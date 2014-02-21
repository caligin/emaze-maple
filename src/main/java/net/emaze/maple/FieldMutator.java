package net.emaze.maple;

import java.lang.reflect.Field;
import org.springframework.core.ResolvableType;

/**
 *
 * @author rferranti
 */
public class FieldMutator implements Mutator {
    
    private final Class<?> containingClass;
    private final Field field;

    public FieldMutator(Class<?> containingClass, Field field) {
        this.containingClass = containingClass;
        this.field = field;
    }
    
    @Override
    public void mutate(Object self, Object value) {
        try {
            field.set(self, value);
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
