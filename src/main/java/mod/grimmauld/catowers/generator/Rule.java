package mod.grimmauld.catowers.generator;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.TriPredicate;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiPredicate;

public enum Rule {
	CONWAYS((c, a) -> c == 3 || (a && c == 2), true),
	HIGHLIFE((c, a) -> c == 3 || (!a && c == 6) || (a && c == 2), true),
	// SUDDEN_DEATH((c, a) -> c == 3, true),
	GRIMM((c, a) -> c == 3 || c == 4 || (a && c == 2), false);

	/*
	CONWAYS((c, layers, offset) -> {
		boolean a = isAlive(layers, offset, 1);
		return c == 3 || (a && c == 2);
	});
	*/

	private static final Rule[] values = values(); // buffer bc efficiency
	private static final int length = values.length;
	public final TriPredicate<Integer, List<Set<Vec3i>>, Vec3i> rule;
	public boolean canMirror;

	Rule(TriPredicate<Integer, List<Set<Vec3i>>, Vec3i> rule, boolean canMirror) {
		this.rule = rule;
		this.canMirror = canMirror;
	}

	Rule(BiPredicate<Integer, Boolean> rule, boolean canMirror) {
		this((c, layers, offset) -> rule.test(c, isAlive(layers, offset, 1)), canMirror);
	}

	public static Rule getRandomRule(Random random) {
		return values[random.nextInt(length)];
	}

	static boolean isAlive(List<Set<Vec3i>> layers, Vec3i test, int yOffset) {
		return layers.size() - yOffset >= 0 && layers.get(layers.size() - yOffset).contains(test.down(-yOffset));
	}
}
