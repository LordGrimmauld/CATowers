package mod.grimmauld.catowers.generator;

import mod.grimmauld.catowers.CATowers;
import mod.grimmauld.catowers.decorator.StructureMetaInf;
import mod.grimmauld.catowers.util.AllOffsets;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;

import java.util.*;
import java.util.function.Predicate;

public class Generator {
	public static final int rangeMultiplier = 6;
	public static final double seedDensity = .4;
	private static final ITextComponent noRuleFound = new TranslationTextComponent(CATowers.MODID + ".no_rule_found");

	public static StructureMetaInf generate(CommandSource source, BlockPos start, Random random) {
		IWorld world = source.getWorld();
		StructureMetaInf seed = new StructureMetaInf(world, start);

		// CA Rule
		Rule rule = Rule.getRandomRule(random);
		if (rule == null) {
			source.sendErrorMessage(noRuleFound);
			return seed;
		}
		// sendFeedback.accept(new StringTextComponent("Generating structure with rule set " + rule));

		// replace solid blocks?
		boolean replaceSolidBlocks = false;
		Predicate<Vec3i> canBeReplaced = replaceSolidBlocks ? offset -> true : offset -> world.getBlockState(start.add(offset)).isAir(world, start.add(offset));

		// radius of the seed
		int radius = rangeMultiplier + (int) (-rangeMultiplier * Math.log10(random.nextDouble()));
		// seed
		StructureMetaInf currentLayer = Shape.getRandomShape(random).generate(radius, () -> random.nextDouble() < seedDensity, random.nextBoolean(), canBeReplaced, seed);


		// generate
		List<StructureMetaInf> layers = new ArrayList<>();
		while (!currentLayer.isEmpty()) {
			layers.add(currentLayer);
			Map<Vec3i, Integer> countMap = new HashMap<>();
			currentLayer.forEach(pos -> AllOffsets.offsets.stream()
				.map(offset -> new Vec3i(pos.getX() + offset.getX(), pos.getY() - 1, pos.getZ() + offset.getZ()))
				.filter(canBeReplaced).forEach(offset -> countMap.put(offset, countMap.getOrDefault(offset, 0) + (!pos.down().equals(offset) ? 1 : 0))));
			currentLayer = StructureMetaInf.toStructureMetaInf(countMap.keySet().stream().filter(pos -> rule.rule.test(countMap.get(pos), layers, pos)), world, start);
		}

		StructureMetaInf structure = new StructureMetaInf(world, start);
		layers.forEach(structure::addAll);

		// mirror upwards
		if (rule.canMirror && layers.size() > 1 && .7 / Math.log(layers.size()) > random.nextDouble())
			layers.forEach(l -> l.stream().map(offset -> new Vec3i(offset.getX(), -offset.getY(), offset.getZ())).filter(canBeReplaced).forEach(structure::add));

		return structure;
	}
}
