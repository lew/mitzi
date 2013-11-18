package mitzi;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * After creating a new <code>AnalysisResult</code> instance, use this class to
 * cache it for later lookup of moves, score, etc. The AnalysisResults are
 * indexed by the HashCode of the corresponding position, therefore it can
 * happen, that different AnalysisResults have the same HashCode. In such a
 * case, the old values get overridden. The AnalysisResults store a different
 * hashvalue to reduce the probability of using a wrong AnalysisResult, if two
 * positions have the same HashCode.
 * 
 */
public class ResultCache {

	private static final int MAX_ENTRIES = 200000;

	/**
	 * A map from the Position's <code>hashCode</code> to the AnalysisResult.
	 * The size of the table is limited with <code>MAX_ENTRIES</code>
	 */
	private static LinkedHashMap<Integer, AnalysisResult> position_cache = new LinkedHashMap<Integer, AnalysisResult>(
			MAX_ENTRIES + 1, 1) {

		private static final long serialVersionUID = 4582735742585308092L;

		protected boolean removeEldestEntry(
				Map.Entry<Integer, AnalysisResult> eldest) {
			return size() > MAX_ENTRIES;
		}
	};

	/**
	 * Cannot be instantiated. For access to the static cache use
	 * <code>ResultCache.getPosition(p)</code>.
	 */
	private ResultCache() {
	}

	/**
	 * Looks up a <code>Position</code> in the cache and returns the saved value
	 * if found and with coinciding second hashvalue. otherwise null.
	 * 
	 * @param lookup
	 *            the <code>Position</code> to look up in the cache
	 * @return a previously cached <code>AnalysisResult</code> if available,
	 *         null otherwise.
	 */
	public static AnalysisResult getResult(IPosition lookup) {
		int hash = lookup.hashCode();
		AnalysisResult ce = position_cache.get(hash);
		if (ce == null || lookup.hashCode2() != ce.hashvalue)
			return null;
		else
			return ce;
	}

	/**
	 * stores a AnalysisResult corresponding to a Position. The second hashvalue is automatically set here.
	 * @param pos the position corresponding to the AnalysisResult
	 * @param ce the AnalysisResult
	 */
	public static void setResult(IPosition pos, AnalysisResult ce) {
		ce.hashvalue = pos.hashCode2();
		int hash = pos.hashCode();
		position_cache.put(hash, ce);

	}
	
	/**
	 * 
	 * @return the number of stored results in this cache
	 */
	public static int size() {
		return position_cache.size();
	}

	/** 
	 * @return the hash is x permill full
	 */
	public static int getHashfull() {
		return (int) ((double) position_cache.size() / MAX_ENTRIES * 1000);
	}
}
