package net.emaze.maple;

import java.lang.reflect.Field;
import org.springframework.core.ResolvableType;


public class FieldMutator implements Mutator {

    private final Field field;

    public FieldMutator(Field field) {
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
    public ResolvableType type(ResolvableType containingType) {
        return ResolvableType.forField(field, containingType);
    }

}
