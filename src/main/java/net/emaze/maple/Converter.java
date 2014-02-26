package net.emaze.maple;

import net.emaze.dysfunctional.options.Maybe;
import net.emaze.maple.types.MapleType;

/**
 *
 * @author rferranti
 */
public interface Converter {

    public boolean canConvert(Converters converters, MapleType sourceType, Object source, MapleType targetType);

    public Maybe<?> convert(Converters converters, MapleType sourceType, Object source, MapleType targetType);
}
