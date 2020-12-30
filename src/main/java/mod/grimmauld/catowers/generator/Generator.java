package mod.grimmauld.catowers.generator;

import mod.grimmauld.catowers.util.AllOffsets;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Generator {
	public static final int rangeMultiplier = 8;
	public static final double fillPercent = .5;

	public static void generate(IWorld world, BlockPos start, Random random) {
		int range = rangeMultiplier + (int) (-rangeMultiplier * Math.log10(random.nextDouble()));
		Predicate<Vec3i> canBeReplaced = offset -> world.getBlockState(start.add(offset)).isAir(world, start.add(offset));
		Set<Vec3i> currentLayer = Shape.getRandomShape(random).generate(range, () -> random.nextDouble() < fillPercent, random.nextBoolean(), canBeReplaced);
		List<Set<Vec3i>> layers = new ArrayList<>();

		boolean shouldCountSelfAsNeighbor = random.nextBoolean();
		Rule rule = Rule.getRandomRule(random);

		while (!currentLayer.isEmpty()) {
			layers.add(currentLayer);
			Map<Vec3i, Integer> countMap = new HashMap<>();
			currentLayer.forEach(pos -> AllOffsets.offsets.stream()
				.map(offset -> new Vec3i(pos.getX() + offset.getX(), pos.getY() - 1, pos.getZ() + offset.getZ()))
				.filter(canBeReplaced).forEach(offset -> countMap.put(offset, countMap.getOrDefault(offset, 0) + (shouldCountSelfAsNeighbor || !pos.down().equals(offset) ? 1 : 0))));
			currentLayer = countMap.keySet().stream().filter(pos -> rule.rule.test(countMap.get(pos), layers, pos)).collect(Collectors.toSet());
		}
		layers.forEach(l -> l.forEach(pos -> world.setBlockState(start.add(pos), Blocks.STONE.getDefaultState(), 3)));

		// mirror upwards
		System.out.println(0.5 / Math.log(layers.size()));
		if (rule.canMirror && start.getY() + layers.size() < 256 && layers.size() > 1 && .7 / Math.log(layers.size()) > random.nextDouble()) {
			layers.forEach(l -> l.stream().map(pos -> start.add(pos.getX(), -pos.getY(), pos.getZ())).filter(canBeReplaced).forEach(pos ->
				world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3)));
		}
	}
}
