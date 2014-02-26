package net.emaze.maple;

import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public interface Mutator {

    public void mutate(Object self, Object value);

    public String name();

    public MapleType type(MapleType containingType);
}
