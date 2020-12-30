package mod.grimmauld.catowers.generator;

import mod.grimmauld.catowers.util.AllOffsets;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import java.util.*;
import java.util.function.Predicate;

public class Generator {
	public static final int rangeMultiplier = 5;
	public static final double fillPercent = 3 / 8.;

	public static void generate(IWorld world, BlockPos start, Random random) {
		int range = rangeMultiplier + (int) (-rangeMultiplier * Math.log10(random.nextDouble()));
		Predicate<Vec3i> canBeReplaced = offset -> world.getBlockState(start.add(offset)).isAir(world, start.add(offset));
		Set<Vec3i> currentLayer = Shape.getRandomShape(random).generate(range, () -> random.nextDouble() < fillPercent, random.nextBoolean(), canBeReplaced);
		List<Set<Vec3i>> layers = new ArrayList<>();

		boolean shouldCountSelfAsNeighbor = random.nextBoolean();
		Rule rule = Rule.getRandomRule(random);

		while (!currentLayer.isEmpty()) {
			layers.add(currentLayer);
			final List<Vec3i> nextLayer = new ArrayList<>();
			currentLayer.forEach(pos -> {
				for (Vec3i offset : AllOffsets.offsets) {
					Vec3i newPos = new Vec3i(pos.getX() +  offset.getX(), pos.getY() - 1, pos.getZ() + offset.getZ());
					if (canBeReplaced.test(newPos))
						nextLayer.add(newPos);
				}
			});
			Set<Vec3i> nextLayerSet = new HashSet<>();
			Set<Vec3i> finalCurrentLayer = currentLayer;
			nextLayer.forEach(pos -> {
				if (nextLayerSet.contains(pos))
					return;
				boolean previouslyAlive = finalCurrentLayer.contains(pos.offset(Direction.UP, 1));
				long count = nextLayer.stream().filter(pos::equals).count();

				if (previouslyAlive && !shouldCountSelfAsNeighbor)
					count--;

				if (rule.rule.test(count, previouslyAlive))
					nextLayerSet.add(pos);
			});
			currentLayer = nextLayerSet;

			// this'd be more efficient than buffering everything, but doesn't allow for post generation logic
			// currentLayer.forEach(pos -> world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3));
		}
		layers.forEach(l -> l.forEach(pos -> world.setBlockState(start.add(pos), Blocks.STONE.getDefaultState(), 3)));

		// mirror upwards
		System.out.println(0.5 / Math.log(layers.size()));
		if (start.getY() + layers.size() < 256 && layers.size() > 1 && 0.5 / Math.log(layers.size()) > random.nextDouble()) {
			layers.forEach(l -> l.stream().map(pos -> start.add(pos.getX(), -pos.getY(), pos.getZ())).filter(canBeReplaced).forEach(pos ->
				world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3)));
		}
	}
}
