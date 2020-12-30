package mod.grimmauld.catowers.generator;

import java.util.Random;
import java.util.function.BiPredicate;

public enum Rule {
	CONWAYS((c, a) -> c == 3 || (a && c == 2)),
	HIGHLIFE((c, a) -> c == 3 || (!a && c == 6) || (a && c == 2)),
	SUDDEN_DEATH((c, a) -> c == 3),
	GRIMM((c, a) -> c == 3 || c == 4 || (a && c == 2));

	private static final Rule[] values = values(); // buffer bc efficiency
	private static final int length = values.length;
	public final BiPredicate<Long, Boolean> rule;

	Rule(BiPredicate<Long, Boolean> rule) {
		this.rule = rule;
	}

	public static Rule getRandomRule(Random random) {
		return values[random.nextInt(length)];
	}
}
