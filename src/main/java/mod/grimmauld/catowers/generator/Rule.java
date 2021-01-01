package mod.grimmauld.catowers.generator;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.TriPredicate;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;

public class Rule {
	private static final Map<Rule, Integer> RULES = new HashMap<>();

	public final TriPredicate<Integer, List<Set<Vec3i>>, Vec3i> rule;
	public boolean canMirror;

	Rule(TriPredicate<Integer, List<Set<Vec3i>>, Vec3i> rule, boolean canMirror) {
		this.rule = rule;
		this.canMirror = canMirror;
	}

	Rule(BiPredicate<Integer, Boolean> rule, boolean canMirror) {
		this((c, layers, offset) -> rule.test(c, isAlive(layers, offset, 1)), canMirror);
	}

	@Nullable
	public static Rule getRandomRule(Random random) {
		int choice = random.nextInt(RULES.values().stream().mapToInt(Integer::intValue).sum());
		for (Map.Entry<Rule, Integer> rule : RULES.entrySet()) {
			choice -= rule.getValue();
			if (choice < 0)
				return rule.getKey();
		}
		return null;
	}

	static boolean isAlive(List<Set<Vec3i>> layers, Vec3i test, int yOffset) {
		try {
			return layers.get(layers.size() - yOffset).contains(test.down(-yOffset));
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public final Rule register(int weight) {
		RULES.put(this, weight);
		return this;
	}
}
