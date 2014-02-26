package net.emaze.maple;

import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public interface Accessor {

    public Object access(Object self);

    public String name();

    public MapleType type(MapleType containingType);
}
