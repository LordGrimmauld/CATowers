package mod.grimmauld.catowers.generator;

import mod.grimmauld.catowers.CATowers;
import mod.grimmauld.catowers.util.AllOffsets;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Generator {
	private static final ITextComponent noRuleFound = new TranslationTextComponent(CATowers.MODID + ".no_rule_found");
	public static final int rangeMultiplier = 6;
	public static final double fillPercent = .4;

	public static Set<Vec3i> generate(CommandSource source, BlockPos start, Random random) {
		// CA Rule
		Rule rule = Rule.getRandomRule(random);
		if (rule == null) {
			source.sendErrorMessage(noRuleFound);
			return new HashSet<>();
		}
		// sendFeedback.accept(new StringTextComponent("Generating structure with rule set " + rule));

		// replace solid blocks?
		boolean replaceSolidBlocks = false;
		IWorld world = source.getWorld();
		Predicate<Vec3i> canBeReplaced = replaceSolidBlocks ? offset -> true : offset -> world.getBlockState(start.add(offset)).isAir(world, start.add(offset));

		// radius of the seed
		int radius = rangeMultiplier + (int) (-rangeMultiplier * Math.log10(random.nextDouble()));
		// seed
		Set<Vec3i> currentLayer = Shape.getRandomShape(random).generate(radius, () -> random.nextDouble() < fillPercent, random.nextBoolean(), canBeReplaced);


		// generate
		List<Set<Vec3i>> layers = new ArrayList<>();
		while (!currentLayer.isEmpty()) {
			layers.add(currentLayer);
			Map<Vec3i, Integer> countMap = new HashMap<>();
			currentLayer.forEach(pos -> AllOffsets.offsets.stream()
				.map(offset -> new Vec3i(pos.getX() + offset.getX(), pos.getY() - 1, pos.getZ() + offset.getZ()))
				.filter(canBeReplaced).forEach(offset -> countMap.put(offset, countMap.getOrDefault(offset, 0) + (!pos.down().equals(offset) ? 1 : 0))));
			currentLayer = countMap.keySet().stream().filter(pos -> rule.rule.test(countMap.get(pos), layers, pos)).collect(Collectors.toSet());
		}
		Set<Vec3i> structure = new HashSet<>();
		layers.forEach(structure::addAll);

		// mirror upwards
		if (rule.canMirror && layers.size() > 1 && .7 / Math.log(layers.size()) > random.nextDouble())
			layers.forEach(l -> l.stream().map(offset -> new Vec3i(offset.getX(), -offset.getY(), offset.getZ())).filter(canBeReplaced).forEach(structure::add));
		return structure;
	}
}
