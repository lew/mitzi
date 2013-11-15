package mitzi;

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TranspositionTable extends AbstractMap<IPosition, Variation> {

	private final HashMap<IPosition, SoftReference<Variation>> cache = new HashMap<IPosition, SoftReference<Variation>>();

	public Variation get(IPosition key) {
		SoftReference<Variation> sr = cache.get(key);
		if (sr == null)
			return null;
		return sr.get();
	}

	public Variation put(IPosition key, Variation value) {
		SoftReference<Variation> sr = cache.put(key,
				new SoftReference<Variation>(value));
		if (sr == null)
			return null;
		return sr.get();
	}

	public int size() {
		return cache.size();
	}

	@Override
	public Set<Entry<IPosition, Variation>> entrySet() {
		Set<Entry<IPosition, SoftReference<Variation>>> set = cache.entrySet();
		Set<Entry<IPosition, Variation>> new_set = new HashSet<Entry<IPosition, Variation>>();
		for (Entry<IPosition, SoftReference<Variation>> entry : set) {
			SoftReference<Variation> sr = entry.getValue();
			if (sr == null)
				set.remove(entry);
			else {
				Entry<IPosition, Variation> new_entry = new SimpleEntry<IPosition, Variation>(
						entry.getKey(), sr.get());
				new_set.add(new_entry);
			}
		}
		return new_set;
	}

}
