package mitzi;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * After creating a new <code>Position</code> instance, use this class to cache
 * it for later lookup of moves, score, etc…
 * 
 */
public class IPositionCache {

	private static final int MAX_ENTRIES = 100000;

	/**
	 * A map from the Position's <code>hashCode</code> to a set of Positions.
	 */
	private static LinkedHashMap<Integer, SoftReference<IPosition>> position_cache = new LinkedHashMap<Integer, SoftReference<IPosition>>(
			MAX_ENTRIES + 1, 1) {

		private static final long serialVersionUID = 4582735742585308092L;

		protected boolean removeEldestEntry(
				Map.Entry<Integer, SoftReference<IPosition>> eldest) {
			return size() > MAX_ENTRIES;
		}
	};

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

	public static int getSize() {
		return position_cache.size();
	}
}
