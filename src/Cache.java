import java.util.HashMap;
import java.util.HashSet;

public class Cache {

	// Cache properties
	private long ass; // Associativity, I just don't like having to type it all out all the time
	private long index_size;
	private long tag_size;
	private long offset_bits;

	// Hashmap for storing addresses to access
	private HashMap<Long, long[]> cache_mappings;

	// Metadata for cache
	private long cache_hits;
	private long cache_comp_misses;
	private long cache_conf_misses;
	private HashSet<Long> seenBlocks = new HashSet<>();

	public Cache(long ass, long index_size, long tag_size, long offset_bits) {
		// Initialize passed values
		this.ass = ass;
		this.index_size = index_size;
		this.tag_size = tag_size;
		this.offset_bits = offset_bits;

		// Initialize HashMap
		cache_mappings = new HashMap<Long, long[]>();

		// Initialize metadata
		cache_hits = 0;
		cache_comp_misses = 0;
		cache_conf_misses = 0;
	}

	public void access(long address) {

		long tag = address >> (index_size + offset_bits);
		long index = (address >> offset_bits) & ((1L << index_size) - 1);

		// Block identifier for compulsory miss tracking
		long block = address >> offset_bits;
		boolean firstEver = !seenBlocks.contains(block);
		if (firstEver) {
			seenBlocks.add(block);
		}

		long[] set = cache_mappings.get(index);

		// First time this set is touched, THIS is a compulsory miss. Otherwise it's a
		// conflict miss.
		// Previous version did not save the cache set touch at all, resulting in false
		// hits and conflicts.
		if (set == null) {
			set = new long[(int) ass];
			for (int i = 0; i < ass; i++)
				set[i] = -1;
			set[0] = tag;

			if (firstEver)
				cache_comp_misses++;
			else
				cache_conf_misses++;

			cache_mappings.put(index, set);
			return;
		}

		// Check for hit
		for (int i = 0; i < ass; i++) {
			if (set[i] == tag) {
				cache_hits++;
				return;
			}
		}

		// Miss: check for empty slot first
		for (int i = 0; i < ass; i++) {
			if (set[i] == -1) {
				set[i] = tag;

				if (firstEver)
					cache_comp_misses++;
				else
					cache_conf_misses++;
				return;
			}
		}

		// If cache is full, flush out some!
		for (int i = 1; i < ass; i++) {
			set[i - 1] = set[i];
		}
		set[(int) ass - 1] = tag;

		if (firstEver)
			cache_comp_misses++;
		else
			cache_conf_misses++;
	}

	public long get_hits() {
		return cache_hits;
	}

	public long get_comp_misses() {
		return cache_comp_misses;
	}

	public long get_conf_misses() {
		return cache_conf_misses;
	}

	public void reset() {
		cache_mappings.clear();
		seenBlocks.clear();
		cache_hits = 0;
		cache_comp_misses = 0;
		cache_conf_misses = 0;
	}
}
