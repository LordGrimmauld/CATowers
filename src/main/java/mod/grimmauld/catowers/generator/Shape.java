package mod.grimmauld.catowers.generator;

import mod.grimmauld.catowers.decorator.StructureMetaInf;
import mod.grimmauld.catowers.util.AllOffsets;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.TriPredicate;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum Shape {
	CIRCLE((x, y, r) -> x * x + y * y < r * r),
	SQUARE,
	TRIANGLE((x, y, r) -> x + y < r * 2);

	private static final Shape[] values = values(); // buffer bc efficiency
	private static final int length = values.length;

	private final TriPredicate<Integer, Integer, Integer> isSquareValid;

	Shape() {
		this((x, y, r) -> true);
	}

	Shape(TriPredicate<Integer, Integer, Integer> isSquareValid) {
		this.isSquareValid = isSquareValid;
	}

	public static Shape getRandomShape(Random random) {
		return values[random.nextInt(length)];
	}

	private static void addIf(Supplier<Boolean> shouldGenerateSquare, boolean filterInPost, Vec3i offset, StructureMetaInf offsets, Predicate<Vec3i> canBeReplaced) {
		if ((filterInPost ? shouldGenerateSquare.get() : true) && canBeReplaced.test(offset))
			offsets.add(offset);
	}

	public StructureMetaInf generate(int range, Supplier<Boolean> shouldGenerateSquare, boolean filterInPost, Predicate<Vec3i> canBeReplaced, StructureMetaInf structure) {
		// filterInPost is true if everything should be filtered on its own and false if it should be filtered symmetrically
		for (int x = 0; x < range; x++) {
			for (int z = 0; z < range; z++) {
				if (isSquareValid.test(x, z, range) && (filterInPost || shouldGenerateSquare.get())) {
					for (Vec3i offset : AllOffsets.straightOffsets) {
						addIf(shouldGenerateSquare, filterInPost, new Vec3i(x * offset.getX(), 0, z * offset.getZ()), structure, canBeReplaced);
					}
				}
			}
		}
		return structure;
	}
}
