package net.emaze.maple;

import java.lang.reflect.Field;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public class FieldAccessor implements Accessor {

    private final Field field;

    public FieldAccessor(Field field) {
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
    public MapleType type(MapleType containingType) {
        return MapleType.forField(field, containingType);
    }

}
