package mitzi;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * After creating a new <code>Position</code> instance, use this class to cache
 * it for later lookup of moves, score, etc…
 * 
 */
public class PositionCache {

	/**
	 * A map from the Position's <code>hashCode</code> to a set of Positions.
	 */
	private static HashMap<Integer, SoftReference<Set<Position>>> position_cache = new HashMap<Integer, SoftReference<Set<Position>>>();

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
		SoftReference<Set<Position>> sr = position_cache.get(hash);
		if (sr == null) {
			Set<Position> new_set = new HashSet<Position>();
			new_set.add(lookup);
			position_cache.put(hash, new SoftReference<Set<Position>>(new_set));
			return lookup;
		} else {
			Set<Position> result_set = sr.get();
			for (Position p : result_set) {
				if (p.equals(lookup)) {
					return p;
				}
			}
			result_set.add(lookup);
			return lookup;
		}
	}
}
