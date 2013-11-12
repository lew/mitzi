package mitzi;

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TranspositionTable extends AbstractMap<IBoard, Variation> {

	private final HashMap<IBoard, SoftReference<Variation>> cache = new HashMap<IBoard, SoftReference<Variation>>();

	public Variation get(IBoard key) {
		SoftReference<Variation> sr = cache.get(key);
		if (sr == null)
			return null;
		return sr.get();
	}

	public Variation put(IBoard key, Variation value) {
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
	public Set<Entry<IBoard, Variation>> entrySet() {
		Set<Entry<IBoard, SoftReference<Variation>>> set = cache.entrySet();
		Set<Entry<IBoard, Variation>> new_set = new HashSet<Entry<IBoard, Variation>>();
		for (Entry<IBoard, SoftReference<Variation>> entry : set) {
			SoftReference<Variation> sr = entry.getValue();
			if (sr == null)
				set.remove(entry);
			else {
				Entry<IBoard, Variation> new_entry = new SimpleEntry<IBoard, Variation>(
						entry.getKey(), sr.get());
				new_set.add(new_entry);
			}
		}
		return new_set;
	}

}
