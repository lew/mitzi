package mitzi;

import java.util.Collections;
import java.util.HashSet;

public class Variation implements Comparable<Variation> {

	private IMove move;

	private int value;

	private HashSet<Variation> sub_variations = new HashSet<Variation>();

	Variation(IMove move, int value) {
		this.move = move;
		this.value = value;
	}

	/**
	 * @return the value of the (principal) variation
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 
	 * @return the first move of the variation
	 */
	public IMove getMove() {
		return move;
	}

	/**
	 * Adds a subvariation to this Variation.
	 * 
	 * @param variation
	 */
	public void addSubVariation(Variation variation) {
		sub_variations.add(variation);
	}

	/**
	 * Deletes all but the best and worst subvariation.
	 */
	public void clearSubMoves() {
		Variation min = Collections.min(sub_variations);
		Variation max = Collections.max(sub_variations);
		HashSet<Variation> new_sub_variations = new HashSet<Variation>();
		new_sub_variations.add(min);
		new_sub_variations.add(max);
		sub_variations = new_sub_variations;

	}

	public Variation getPrincipalVariation(Side to_move) {
		return null; // TODO
	}

	/**
	 * Compares two Variation objects by their value.
	 * 
	 * @param anotherVariation
	 *            the Variation to be compared.
	 * 
	 * @return the value 0 if this Variation is equal to the argument Variation;
	 *         a value less than 0 if this Variation is in value less than the
	 *         argument Variation; and a value greater than 0 if this Variation
	 *         is in value greater than the argument Variation (signed
	 *         comparison).
	 */
	@Override
	public int compareTo(Variation anotherVariation) {
		return Integer.compare(anotherVariation.getValue(), this.getValue());
	}
}
