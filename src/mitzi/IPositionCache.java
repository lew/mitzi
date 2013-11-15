package mitzi;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * After creating a new <code>Position</code> instance, use this class to cache
 * it for later lookup of moves, score, etc…
 * 
 */
public class IPositionCache {

	/**
	 * A map from the Position's <code>hashCode</code> to a set of Positions.
	 */
	private static HashMap<Integer, SoftReference<IPosition>> position_cache = new HashMap<Integer, SoftReference<IPosition>>(
			200000);

	/**
	 * Cannot be instantiated. For access to the static cache use
	 * <code>IPositionCache.getPosition(p)</code>.
	 */
	private IPositionCache() {
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
	public static IPosition getPosition(IPosition lookup) {
		int hash = lookup.hashCode();
		SoftReference<IPosition> sr = position_cache.get(hash);
		if (sr == null) {
			position_cache.put(hash, new SoftReference<IPosition>(lookup));
			return lookup;
		} else {
			IPosition result = sr.get();
			// replace the old value, if there is a collision between the
			// hashkeys
			if (!lookup.equals(result)) {
				position_cache.put(hash, new SoftReference<IPosition>(lookup));
				return lookup;
			}
			return result;
		}
	}
}
