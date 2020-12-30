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
	PYRAMID((c, a) -> c == 3 || c == 4 || (a && c == 2), false),

	GRIMM2((x, layers, offset) -> {
		boolean a = isAlive(layers, offset, 1);
		boolean c = isAlive(layers, offset, 3);
		boolean d = isAlive(layers, offset, 4);
		return (x == 2 && a) || (x == 3) || (x == 4 && c) || (x == 5 && d);
	}, false),

	GRIMM3((x, layers, offset) -> {
		boolean a = isAlive(layers, offset, 1);
		boolean b = isAlive(layers, offset, 2);
		boolean c = isAlive(layers, offset, 3);
		boolean d = isAlive(layers, offset, 4);
		return (x == 2 && a) || (x == 3) || (x == 4 && b) || (x == 5 && c) || (x == 6 && d);
	}, false);


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
		try {
			return layers.get(layers.size() - yOffset).contains(test.down(-yOffset));
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
}
