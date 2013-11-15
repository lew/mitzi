package mitzi;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * After creating a new <code>Position</code> instance, use this class to cache
 * it for later lookup of moves, score, etc…
 * 
 */
public class PositionCache {

	/**
	 * A map from the Position's <code>hashCode</code> to a set of Positions.
	 */
	private static HashMap<Integer, SoftReference<Position>> position_cache = new HashMap<Integer, SoftReference<Position>>();

	/**
	 * Cannot be instantiated. For access to the static cache use
	 * <code>PositionCache.getPosition(p)</code>.
	 */
	private PositionCache() {
	}

	/**
	 * Looks up a <code>Position</code> in the cache and returns the saved value
	 * if found, the specified <code>Position</code> otherwise. If the
	 * <code>Position</code> is not yet in the cache it gets added in the
	 * process.
	 * 
	 * @param lookup
	 *            the <code>Position</code> to look up in the cache
	 * @return a previously cached <code>Position</code> if available, otherwise
	 *         the same object again
	 */
	public static Position getPosition(Position lookup) {
		int hash = lookup.hashCode();
		SoftReference<Position> sr = position_cache.get(hash);
		if (sr == null) {
			Position new_pos = new Position();
			position_cache.put(hash,
					new SoftReference<Position>(new_pos));
			return lookup;
		} else {
			Position result = sr.get();
			
			//replace the old value, if there is a collision between the hashkeys
			if(!lookup.equals(result)){
				position_cache.put(hash,
						new SoftReference<Position>(lookup));
				return lookup;
			}	
			return result;
		}
	}
}
